# COS 自定义域名 SSL 测试环境

Nginx 反向代理（推荐）

这种方式会将请求转发到真实的 COS 服务器，完全模拟真实的自定义域名场景。

### 快速部署

```bash
cd /Users/jordanqin/qcloud-sdk-android-woa/QCloudCosXml/ssl-test-server

# 1. 生成证书
bash scripts/generate-certs.sh

# 2. 一键部署 Nginx（会提示输入 bucket 和 region）
bash scripts/deploy-nginx.sh
```

### 手动部署

1. **生成证书**
```bash
bash scripts/generate-certs.sh
```

2. **编辑 Nginx 配置**

复制 `nginx/cos-proxy.conf` 到 Nginx 配置目录，并修改：
- 证书路径（`ssl_certificate` 等）
- COS bucket 名称（`$cos_bucket`）
- COS region（`$cos_region`）

```bash
# macOS
sudo cp nginx/cos-proxy.conf /usr/local/etc/nginx/servers/

# Linux  
sudo cp nginx/cos-proxy.conf /etc/nginx/conf.d/
```

3. **重启 Nginx**
```bash
# macOS
brew services restart nginx

# Linux
sudo systemctl restart nginx
```

### 测试

```bash
# 场景1: 自签名证书
curl --cacert certs/ca.crt https://localhost:8443/

# 场景2: 双向认证
curl --cacert certs/ca.crt \
     --cert certs/client.crt \
     --key certs/client.key \
     https://localhost:8444/
```

---

---

## 目录结构

```
ssl-test-server/
├── certs/                  # 证书目录（自动生成）
│   ├── ca.crt             # CA 证书
│   ├── ca.key             # CA 私钥
│   ├── server.crt         # 服务端证书
│   ├── server.key         # 服务端私钥
│   ├── client.crt         # 客户端证书
│   ├── client.key         # 客户端私钥
│   ├── client.p12         # PKCS12 格式客户端证书
│   └── ca.der             # DER 格式 CA 证书
├── nginx/
│   └── cos-proxy.conf     # Nginx 配置模板
├── scripts/
│   ├── generate-certs.sh  # 证书生成脚本
│   └── deploy-nginx.sh    # Nginx 部署脚本
```

## 端口说明

| 端口 | 场景 | 说明 |
|------|------|------|
| **8443** | 自签名证书 | 单向 SSL，客户端需信任 CA |
| **8444** | 双向认证 | mTLS，客户端需提供证书 |

---

## Android SDK 配置

### 场景1: 自签名证书

**方式1: 绕过 SSL 验证（仅测试）**
```java
CosXmlServiceConfig config = new CosXmlServiceConfig.Builder()
    .setRegion("ap-shanghai")
    .setVerifySSLEnable(false)
    .builder();
```

**方式2: 自定义 TrustManager**
```java
// 加载 CA 证书
InputStream caInput = getAssets().open("ca.crt");
CertificateFactory cf = CertificateFactory.getInstance("X.509");
Certificate ca = cf.generateCertificate(caInput);

KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
keyStore.load(null, null);
keyStore.setCertificateEntry("ca", ca);

TrustManagerFactory tmf = TrustManagerFactory.getInstance(
    TrustManagerFactory.getDefaultAlgorithm());
tmf.init(keyStore);
TrustManager[] trustManagers = tmf.getTrustManagers();

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, trustManagers, new SecureRandom());

CosXmlServiceConfig config = new CosXmlServiceConfig.Builder()
    .setRegion("ap-shanghai")
    .setCustomSSLContext(sslContext, (X509TrustManager) trustManagers[0])
    .builder();
```

### 场景2: 双向认证

```java
// 1. 加载 CA 证书（信任服务端）
InputStream caInput = getAssets().open("ca.crt");
CertificateFactory cf = CertificateFactory.getInstance("X.509");
Certificate ca = cf.generateCertificate(caInput);

KeyStore caKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
caKeyStore.load(null, null);
caKeyStore.setCertificateEntry("ca", ca);

TrustManagerFactory tmf = TrustManagerFactory.getInstance(
    TrustManagerFactory.getDefaultAlgorithm());
tmf.init(caKeyStore);

// 2. 加载客户端证书
InputStream clientInput = getAssets().open("client.p12");
KeyStore clientKeyStore = KeyStore.getInstance("PKCS12");
clientKeyStore.load(clientInput, "123456".toCharArray());

KeyManagerFactory kmf = KeyManagerFactory.getInstance(
    KeyManagerFactory.getDefaultAlgorithm());
kmf.init(clientKeyStore, "123456".toCharArray());

// 3. 创建 SSLContext
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

// 4. 配置 SDK
CosXmlServiceConfig config = new CosXmlServiceConfig.Builder()
    .setRegion("ap-shanghai")
    .setCustomSSLContext(sslContext, (X509TrustManager) tmf.getTrustManagers()[0])
    .builder();
```

---

## 常见问题

### Q: Android 模拟器如何连接？

获取电脑 IP 后，使用该 IP 作为服务器地址：
```bash
# macOS
ipconfig getifaddr en0

# Linux
hostname -I | awk '{print $1}'
```

### Q: curl 测试报证书错误？

确保使用 `--cacert certs/ca.crt` 参数信任 CA 证书。

### Q: Nginx 转发后 COS 返回 403？

检查：
1. Authorization 头是否正确传递
2. Host 头是否设置为 COS 域名
3. 签名是否使用了正确的域名

### Q: 如何更换 bucket？

- **Nginx 方式**: 修改配置文件中的 `$cos_bucket` 和 `$cos_region`，然后 `nginx -s reload`
- **部署脚本**: 重新运行 `bash scripts/deploy-nginx.sh`

---

## 证书密码

所有证书密码统一为: `123456`
