#!/bin/bash

# 证书生成脚本
# 生成用于测试的自签名证书和双向认证证书

CERT_DIR="$(dirname "$0")/../certs"
mkdir -p "$CERT_DIR"
cd "$CERT_DIR"

echo "========================================"
echo "生成 CA 根证书"
echo "========================================"

# 生成 CA 私钥
openssl genrsa -out ca.key 4096

# 生成 CA 证书
openssl req -new -x509 -days 3650 -key ca.key -out ca.crt \
    -subj "/C=CN/ST=Shanghai/L=Shanghai/O=TestCA/OU=IT/CN=Test Root CA"

echo "========================================"
echo "生成服务端证书（自签名）"
echo "========================================"

# 生成服务端私钥
openssl genrsa -out server.key 2048

# 创建服务端证书配置文件
cat > server.cnf << EOF
[req]
distinguished_name = req_distinguished_name
req_extensions = v3_req
prompt = no

[req_distinguished_name]
C = CN
ST = Shanghai
L = Shanghai
O = TestOrg
OU = IT
CN = localhost

[v3_req]
basicConstraints = CA:FALSE
keyUsage = nonRepudiation, digitalSignature, keyEncipherment
subjectAltName = @alt_names

[alt_names]
DNS.1 = localhost
DNS.2 = *.cos.ap-shanghai.myqcloud.com
DNS.3 = cos.ap-shanghai.myqcloud.com
DNS.4 = *.myqcloud.com
IP.1 = 127.0.0.1
IP.2 = 10.0.2.2
EOF

# 生成服务端证书请求
openssl req -new -key server.key -out server.csr -config server.cnf

# 使用 CA 签发服务端证书
openssl x509 -req -days 365 -in server.csr -CA ca.crt -CAkey ca.key \
    -CAcreateserial -out server.crt -extensions v3_req -extfile server.cnf

echo "========================================"
echo "生成客户端证书（用于双向认证）"
echo "========================================"

# 生成客户端私钥
openssl genrsa -out client.key 2048

# 创建客户端证书配置文件
cat > client.cnf << EOF
[req]
distinguished_name = req_distinguished_name
req_extensions = v3_req
prompt = no

[req_distinguished_name]
C = CN
ST = Shanghai
L = Shanghai
O = TestClient
OU = IT
CN = test-client

[v3_req]
basicConstraints = CA:FALSE
keyUsage = nonRepudiation, digitalSignature, keyEncipherment
extendedKeyUsage = clientAuth
EOF

# 生成客户端证书请求
openssl req -new -key client.key -out client.csr -config client.cnf

# 使用 CA 签发客户端证书
openssl x509 -req -days 365 -in client.csr -CA ca.crt -CAkey ca.key \
    -CAcreateserial -out client.crt -extensions v3_req -extfile client.cnf

echo "========================================"
echo "生成 Android 可用的证书格式"
echo "========================================"

# 生成 PKCS12 格式的客户端证书（Android 可直接导入）
openssl pkcs12 -export -in client.crt -inkey client.key -out client.p12 \
    -name "client" -password pass:123456

# 生成 DER 格式的 CA 证书（某些场景需要）
openssl x509 -in ca.crt -out ca.der -outform DER

echo "========================================"
echo "生成 BKS 格式证书（Android KeyStore）"
echo "========================================"

# 检查是否有 bcprov jar 包，用于生成 BKS
BCPROV_JAR="bcprov-jdk15on-1.70.jar"
if [ ! -f "$BCPROV_JAR" ]; then
    echo "下载 Bouncy Castle Provider..."
    curl -L -o "$BCPROV_JAR" "https://repo1.maven.org/maven2/org/bouncycastle/bcprov-jdk15on/1.70/bcprov-jdk15on-1.70.jar" 2>/dev/null || true
fi

if [ -f "$BCPROV_JAR" ]; then
    # 生成客户端 BKS 证书（包含私钥）
    keytool -importkeystore \
        -srckeystore client.p12 \
        -srcstoretype PKCS12 \
        -srcstorepass 123456 \
        -destkeystore client.bks \
        -deststoretype BKS \
        -deststorepass 123456 \
        -provider org.bouncycastle.jce.provider.BouncyCastleProvider \
        -providerpath "$BCPROV_JAR" 2>/dev/null || echo "BKS 转换可能需要手动处理"

    # 生成 CA 证书的 BKS（用于信任服务端）
    keytool -importcert \
        -file ca.crt \
        -keystore ca-trust.bks \
        -storetype BKS \
        -storepass 123456 \
        -alias ca \
        -noprompt \
        -provider org.bouncycastle.jce.provider.BouncyCastleProvider \
        -providerpath "$BCPROV_JAR" 2>/dev/null || echo "BKS 转换可能需要手动处理"
else
    echo "跳过 BKS 生成（缺少 Bouncy Castle）"
    echo "可手动下载: https://repo1.maven.org/maven2/org/bouncycastle/bcprov-jdk15on/1.70/bcprov-jdk15on-1.70.jar"
fi

echo "========================================"
echo "证书生成完成！"
echo "========================================"
echo ""
echo "生成的文件列表："
ls -la "$CERT_DIR"
echo ""
echo "文件说明："
echo "  CA 证书:       ca.crt, ca.key"
echo "  服务端证书:    server.crt, server.key"
echo "  客户端证书:    client.crt, client.key"
echo "  客户端 P12:    client.p12 (密码: 123456)"
echo "  CA DER:        ca.der"
echo ""
echo "Android 使用说明："
echo "  1. 自签名证书场景: 需要信任 ca.crt"
echo "  2. 双向认证场景:   需要将 client.p12 或 client.bks 提供给 Android 客户端"
echo ""
