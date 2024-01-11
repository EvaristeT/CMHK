# 1. 引言

URL:
- (生產) 
- (測試) 
- (本地開發/測試) 

# 2. 目錄

- [1. 引言](#1-引言)
- [2. 目錄](#2-目錄)
- [3. 開發手冊](#3-開發手冊)
  - [3.1. 系統](#31-系統)
  - [3.2. 安裝步驟](#32-安裝步驟)
    - [3.2.1. 加入本地開發域名](#321-加入本地開發域名)
    - [3.2.2. (建議) Docker](#322-建議-docker)
  - [3.3. 解除安裝](#33-解除安裝)
    - [3.3.1. Docker](#331-docker)
    - [3.3.2. Host](#332-host)
  - [3.4. 分支管理](#34-分支管理)
    - [3.4.1. 分支的定義:](#341-分支的定義)

# 3. 開發手冊

## 3.1. 系統


## 3.2. 安裝步驟

### 3.2.1. 加入本地開發域名


### 3.2.2. (建議) Docker

## 3.3. 解除安裝

### 3.3.1. Docker


### 3.3.2. Host

把 *appei.local* 刪除, 文件路俓請參看: 加入本地開發域名

## 3.4. 分支管理

> This repository is based on [OneFlow](https://www.endoflineblog.com/oneflow-a-git-branching-model-and-workflow). (Develop + Main) Please check official documents if you're not familiy on this.

### 3.4.1. 分支的定義:

- main: 生產環境代碼, 應與生產環境無限接近同步.
- develop: 開發環境代碼, 提交到此分支的代碼必須完備上線條件. 未完成測試前請保留在功能分支上. 
- feature: 日常開發的分支. 必須從 *develop* 分支出來. 建議命名以 **feature/xxxxx** 作前綴.
- release: 部署使用的排版分支, 從 *develop* 分支出來. 部署完成後合併到 **main**, 如有修改也需要合併回 **develop**.
- hotfix: hotfix 分支 (**hotfix/xxxxx**), 為生產環境作出緊急修改. (不建議使用)
- bugfix: bugfix 分支 (**bugfix/xxxxx**), AR部署後再進行修改. (不建議使用)

> Relaese 分支

- 取得已更新的文件列表


```sh
git diff main --name-only
```
