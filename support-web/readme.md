### 配置描述

```yaml
ultron:
  mvc:
    xss:
      enable: true
      excludes: /notXss/*,/demo/*
    cors-configurations:
      "[/**]":
        allowedOrigins: "*"
        allowedMethods: "*"
        allowedHeaders: "*"
        allowCredentials: true
```
