-- 时间窗口限流
local key = KEYS[1]
local count = tonumber(ARGV[1])
local period = tonumber(ARGV[2])

local c = redis.call('get',key)
if c and tonumber(c) > count then
return c;
end
c = redis.call('incr',KEYS[1])
if tonumber(c) == 1 then
redis.call('expire',key,period)
end
return c;
