# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.6.0] - 2021-08-19

### Add

- 微信 H5 功能 #6
- AR2108089, 優化解耦外部訂單流程 #7

## [2.5.0] - 2021-06-09

### Fix  

- Fix, 发现香港支付宝加签无法除去换行符\r，System.getProperty("line.separator")替换无效

### Add  

- Add, 添加香港支付宝RSA唤起功能

### Changed

- 添加黑白名单过滤功能 #4
- 系統消息推送從 **Telegram** 更改成 **FeiShu** #5

## [2.4.2] - 2021-05-26

### Added

- Refine gitlab-ci.yml
- Dockerize ci flow

## [2.4.1] - 2021-05-26

### Changed

- WeChatPay icon Modify

## [2.4.0] - 2021-05-24

### Changed

- WeChat Pay Convert mapper: Remove *mchid* from *notify_url* tail

## [2.3.3] - 2021-05-19

### Changed

- Changed, 修改wechatdatafeed notify_url, 兼容下划线与url参数情况

## [2.3.2] - 2021-05-15 fix underline bug

### Fixed

- Bugfix, 添加mybatisplus配置，下划线转驼峰

## [2.3.1] - 2021-05-14

### Fixed

- hoxfix, 如果alihkdatafeed partner为空，则使用默认的partner

## [2.3.0] - 2021-05-14

### Changed

- Changed, 清理冗余文件
  
### Fixed

- Bugfix, 修复维护日期设置不生效问题
