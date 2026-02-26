#!/bin/bash

# Nginx 一键部署脚本
# 用于测试 COS 自定义域名的自签名证书和双向认证

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
CERT_DIR="$PROJECT_DIR/certs"
NGINX_CONF="$PROJECT_DIR/nginx/cos-proxy.conf"

# 默认配置
COS_BUCKET="${COS_BUCKET:-mobile-ut-1253960454}"
COS_REGION="${COS_REGION:-ap-guangzhou}"

echo "========================================"
echo "COS 自定义域名 SSL 测试环境部署脚本"
echo "========================================"

# 检查是否安装了 nginx
if ! command -v nginx &> /dev/null; then
    echo "错误: 未安装 nginx"
    echo ""
    echo "安装方法:"
    echo "  macOS:  brew install nginx"
    echo "  Ubuntu: sudo apt install nginx"
    echo "  CentOS: sudo yum install nginx"
    exit 1
fi

# 检查证书是否存在
if [ ! -f "$CERT_DIR/server.crt" ]; then
    echo "证书不存在，正在生成..."
    bash "$SCRIPT_DIR/generate-certs.sh"
fi

# 获取 nginx 配置目录
NGINX_CONF_DIR=""
if [ -d "/usr/local/etc/nginx" ]; then
    # macOS (Homebrew)
    NGINX_CONF_DIR="/usr/local/etc/nginx"
elif [ -d "/opt/homebrew/etc/nginx" ]; then
    # macOS (Homebrew ARM)
    NGINX_CONF_DIR="/opt/homebrew/etc/nginx"
elif [ -d "/etc/nginx" ]; then
    # Linux
    NGINX_CONF_DIR="/etc/nginx"
else
    echo "错误: 找不到 nginx 配置目录"
    exit 1
fi

echo ""
echo "Nginx 配置目录: $NGINX_CONF_DIR"
echo "证书目录: $CERT_DIR"
echo ""

# 创建配置文件
CONFIG_FILE="$NGINX_CONF_DIR/servers/cos-proxy.conf"
if [ ! -d "$NGINX_CONF_DIR/servers" ]; then
    CONFIG_FILE="$NGINX_CONF_DIR/conf.d/cos-proxy.conf"
fi

echo "生成 Nginx 配置文件: $CONFIG_FILE"
echo ""
echo "请输入你的 COS bucket 名称 (默认: $COS_BUCKET):"
read -r input_bucket
if [ -n "$input_bucket" ]; then
    COS_BUCKET="$input_bucket"
fi

echo "请输入你的 COS region (默认: $COS_REGION):"
read -r input_region
if [ -n "$input_region" ]; then
    COS_REGION="$input_region"
fi

# 生成配置
cat > /tmp/cos-proxy.conf << EOF
# COS 自定义域名 SSL 测试配置
# 自动生成于: $(date)

# ============================================================
# 场景1: 自签名证书 - 单向 SSL
# 端口: 8443
# ============================================================
server {
    listen 8443 ssl;
    server_name localhost;

    ssl_certificate     $CERT_DIR/server.crt;
    ssl_certificate_key $CERT_DIR/server.key;

    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    access_log /tmp/cos-proxy-simple.log;
    error_log  /tmp/cos-proxy-simple-error.log;

    location / {
        proxy_pass https://$COS_BUCKET.cos.$COS_REGION.myqcloud.com;
        proxy_set_header Host $COS_BUCKET.cos.$COS_REGION.myqcloud.com;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
        proxy_pass_request_headers on;
        
        client_max_body_size 5G;
        proxy_request_buffering off;
        proxy_connect_timeout 60s;
        proxy_send_timeout 300s;
        proxy_read_timeout 300s;
        
        proxy_ssl_server_name on;
        proxy_ssl_protocols TLSv1.2 TLSv1.3;
    }
}

# ============================================================
# 场景2: 双向认证 (mTLS)
# 端口: 8444
# ============================================================
server {
    listen 8444 ssl;
    server_name localhost;

    ssl_certificate     $CERT_DIR/server.crt;
    ssl_certificate_key $CERT_DIR/server.key;
    ssl_client_certificate $CERT_DIR/ca.crt;
    ssl_verify_client on;

    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    access_log /tmp/cos-proxy-mtls.log;
    error_log  /tmp/cos-proxy-mtls-error.log;

    location / {
        proxy_pass https://$COS_BUCKET.cos.$COS_REGION.myqcloud.com;
        proxy_set_header Host $COS_BUCKET.cos.$COS_REGION.myqcloud.com;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
        proxy_set_header X-SSL-Client-CN \$ssl_client_s_dn;
        proxy_pass_request_headers on;
        
        client_max_body_size 5G;
        proxy_request_buffering off;
        proxy_connect_timeout 60s;
        proxy_send_timeout 300s;
        proxy_read_timeout 300s;
        
        proxy_ssl_server_name on;
        proxy_ssl_protocols TLSv1.2 TLSv1.3;
    }
}
EOF

echo ""
echo "配置预览:"
echo "========================================"
cat /tmp/cos-proxy.conf
echo "========================================"
echo ""
echo "是否应用此配置? (y/n)"
read -r confirm

if [ "$confirm" = "y" ] || [ "$confirm" = "Y" ]; then
    # 创建目录
    sudo mkdir -p "$(dirname "$CONFIG_FILE")"
    
    # 复制配置
    sudo cp /tmp/cos-proxy.conf "$CONFIG_FILE"
    
    # 测试配置
    echo "测试 Nginx 配置..."
    sudo nginx -t
    
    # 启动或重载 nginx
    echo "启动/重载 Nginx..."
    
    # 检查 nginx 是否正在运行
    if pgrep -x "nginx" > /dev/null; then
        echo "Nginx 正在运行，执行重载..."
        sudo nginx -s reload
    else
        echo "Nginx 未运行，正在启动..."
        sudo nginx
    fi
    
    echo ""
    echo "========================================"
    echo "部署完成！"
    echo "========================================"
    echo ""
    echo "测试命令:"
    echo ""
    echo "# 场景1: 自签名证书 (端口 8443)"
    echo "curl --cacert $CERT_DIR/ca.crt https://localhost:8443/"
    echo ""
    echo "# 场景2: 双向认证 (端口 8444)"
    echo "curl --cacert $CERT_DIR/ca.crt \\"
    echo "     --cert $CERT_DIR/client.crt \\"
    echo "     --key $CERT_DIR/client.key \\"
    echo "     https://localhost:8444/"
    echo ""
    echo "Android SDK 测试:"
    echo "  自定义域名设置为: https://你的电脑IP:8443 或 8444"
    echo ""
else
    echo "已取消"
    exit 0
fi
