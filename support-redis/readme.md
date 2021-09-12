## redis 工具包

```properties
# 只有以下配置分布式锁才会生效
redisson.address=
```

#### 单机模式

```properties
# redisson lock
redisson.address=redis://10.18.75.115:6379
redisson.password=

#这里如果不加redis://前缀会报URI构建错误，
#Caused by: java.net.URISyntaxException: Illegal character in scheme name at index 0
```

#### 哨兵模式

```properties
redisson.master-name=mymaster
redisson.password=xxxx
redisson.sentinel-addresses=10.47.91.83:26379,10.47.91.83:26380,10.47.91.83:26381
```

#### Spring缓存配置(默认)

```properties
#spring缓存，默认失效时间
ultron.redis.expiresMinutes=10
#缓存前缀
ultron.redis.prefixFormat="SpringCache:%s:"
```
