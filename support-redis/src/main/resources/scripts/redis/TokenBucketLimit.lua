-- 令牌桶限流
local key = KEYS[1]
local now = tonumber(ARGV[1])
local limitWindowTime = tonumber(ARGV[2])
local removeScore = tonumber(ARGV[3])
local allowedMax = tonumber(ARGV[4])

-- 移除过期分数
redis.call('ZREMRANGEBYSCORE',key,0,removeScore)
-- 计算集合中元素的数量
local current = tonumber(redis.call('ZCARD',key))
local next = current+1
-- 如果增加一个令牌判断是否会超出最大值，超出则返回0
if next > allowedMax then
    return 0
else
-- 放入一个令牌桶，并调整key超时时间
    redis.call('ZADD',key,now,now)
    redis.call('PEXPIRE',key,limitWindowTime)
    return next
end
