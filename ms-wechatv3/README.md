# 1. 引言

本專案為 API 微服務, 對應 微信支付. 主要用於以下場景:

1. 門市 CRM 系統調用 微信支付
# 2. 目錄

- [1. 引言](#1-引言)
- [2. 目錄](#2-目錄)
- [3. 開發手冊](#3-開發手冊)
  - [3.1. 系統](#31-系統)
  - [3.2. 安裝步驟](#32-安裝步驟)
    - [3.2.1. 中間件](#321-中間件)
      - [Nacos](#nacos)
  - [3.3. 開發配置](#33-開發配置)
  - [3.4. 分支管理](#34-分支管理)
    - [3.4.1. 分支的定義](#341-分支的定義)

# 3. 開發手冊

## 3.1. 系統

URL:

- (生產) <https://pay.hk.chinamobile.com/api/wechatv3/>
- (測試) <https://pay-uat.hk.chinamobile.com/api/wechatv3/>
- (本地開發/測試) <http://localhost:8082/>

API:

**Swagger:**

- API: <https://pay-uat.hk.chinamobile.com/api/wechatv3/doc/>
- API (YAML): <https://pay-uat.hk.chinamobile.com/api/wechatv3/doc.yaml>
- UI: <https://pay-uat.hk.chinamobile.com/api/wechatv3/doc.html>

**API:**

- 檢查訂單: {url}/{merchantCode}/transaction/check
- 創建訂單, 生成二維碼: {url}/{merchantCode}/transaction/native
- 取消訂單: {url}/{merchantCode}/transaction/close

**Microservice Port:**
- 8080: Stable
- 8081: Beta

## 3.2. 安裝步驟

### 3.2.1. 中間件

#### Nacos

此微服務依賴 Nacos 作為配置及發現中心

有關本地架設, 開發等請參閱 [這裏](http://10.0.27.225/payments/zt-payment/mw-nacos)

## 3.3. 開發配置

**支付中台 - 數據表依賴:**

- cc_payment_merchant
- cc_payment_merchant_method
- cc_payment_param

## 3.4. 分支管理

> This repository is based on [OneFlow](https://www.endoflineblog.com/oneflow-a-git-branching-model-and-workflow). (Develop + Main) Please check official documents if you're not familiy on this.

### 3.4.1. 分支的定義

- main: 生產環境代碼, 應與生產環境無限接近同步.
- feature: 日常開發的分支. 必須從 *main* 分支出來. 建議命名以 **feature/xxxxx** 作前綴.
- release: 部署使用的排版分支, 從 *main* 分支出來. 部署完成後合併到 **main**.
- hotfix: hotfix 分支 (**hotfix/xxxxx**), 為生產環境作出緊急修改. (不建議使用)
- bugfix: bugfix 分支 (**bugfix/xxxxx**), AR部署後再進行修改. (不建議使用)

> Relaese 分支

- 取得已更新的文件列表


```sh
git diff main --name-only
```
