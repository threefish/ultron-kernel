package cn.xjbpm.ultron.redis.util;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis 帮助类 参考 https://github.com/iyayu/RedisUtil
 *
 * @author 黄川 huchuc@vip.qq.com
 */
public class RedisTemplateUtil {

	private static RedisTemplate<String, Serializable> redisTemplate;

	public static void setRedisTemplate(RedisTemplate<String, Serializable> redisTemplate) {
		RedisTemplateUtil.redisTemplate = redisTemplate;
		Assert.notNull(RedisTemplateUtil.redisTemplate, "redisTemplate can not be null");
	}

	/** -------------------key相关操作--------------------- */

	/**
	 * 删除key
	 * @param key
	 */
	public static void delete(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 批量删除key
	 * @param keys
	 */
	public static void delete(Collection<String> keys) {
		redisTemplate.delete(keys);
	}

	/**
	 * 序列化key
	 * @param key
	 * @return
	 */
	public static byte[] dump(String key) {
		return redisTemplate.dump(key);
	}

	/**
	 * 是否存在key
	 * @param key
	 * @return
	 */
	public static Boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 设置过期时间
	 * @param key
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public static Boolean expire(String key, long timeout, TimeUnit unit) {
		return redisTemplate.expire(key, timeout, unit);
	}

	/**
	 * 设置过期时间
	 * @param key
	 * @param date
	 * @return
	 */
	public static Boolean expireAt(String key, Date date) {
		return redisTemplate.expireAt(key, date);
	}

	/**
	 * 查找匹配的key
	 * @param pattern
	 * @return
	 */
	public static Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	/**
	 * 将当前数据库的 key 移动到给定的数据库 db 当中
	 * @param key
	 * @param dbIndex
	 * @return
	 */
	public static Boolean move(String key, int dbIndex) {
		return redisTemplate.move(key, dbIndex);
	}

	/**
	 * 移除 key 的过期时间，key 将持久保持
	 * @param key
	 * @return
	 */
	public static Boolean persist(String key) {
		return redisTemplate.persist(key);
	}

	/**
	 * 返回 key 的剩余的过期时间
	 * @param key
	 * @param unit
	 * @return
	 */
	public static Long getExpire(String key, TimeUnit unit) {
		return redisTemplate.getExpire(key, unit);
	}

	/**
	 * 返回 key 的剩余的过期时间
	 * @param key
	 * @return
	 */
	public static Long getExpire(String key) {
		return redisTemplate.getExpire(key);
	}

	/**
	 * 从当前数据库中随机返回一个 key
	 * @return
	 */
	public static String randomKey() {
		return redisTemplate.randomKey();
	}

	/**
	 * 修改 key 的名称
	 * @param oldKey
	 * @param newKey
	 */
	public static void rename(String oldKey, String newKey) {
		redisTemplate.rename(oldKey, newKey);
	}

	/**
	 * 仅当 newkey 不存在时，将 oldKey 改名为 newkey
	 * @param oldKey
	 * @param newKey
	 * @return
	 */
	public static Boolean renameIfAbsent(String oldKey, String newKey) {
		return redisTemplate.renameIfAbsent(oldKey, newKey);
	}

	/**
	 * 返回 key 所储存的值的类型
	 * @param key
	 * @return
	 */
	public static DataType type(String key) {
		return redisTemplate.type(key);
	}

	/** -------------------string相关操作--------------------- */

	/**
	 * 设置指定 key 的值
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 获取指定 key 的值
	 * @param key
	 * @return
	 */
	public static Serializable get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * 返回 key 中字符串值的子字符
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static String getRange(String key, long start, long end) {
		return redisTemplate.opsForValue().get(key, start, end);
	}

	/**
	 * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
	 * @param key
	 * @param value
	 * @return
	 */
	public static Serializable getAndSet(String key, String value) {
		return redisTemplate.opsForValue().getAndSet(key, value);
	}

	/**
	 * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)
	 * @param key
	 * @param offset
	 * @return
	 */
	public static Boolean getBit(String key, long offset) {
		return redisTemplate.opsForValue().getBit(key, offset);
	}

	/**
	 * 批量获取
	 * @param keys
	 * @return
	 */
	public static List<Serializable> multiGet(Collection<String> keys) {
		return redisTemplate.opsForValue().multiGet(keys);
	}

	/**
	 * 设置ASCII码, 字符串'a'的ASCII码是97, 转为二进制是'01100001', 此方法是将二进制第offset位值变为value
	 * @param key
	 * @param offset 位置
	 * @param value 值,true为1, false为0
	 * @return
	 */
	public static boolean setBit(String key, long offset, boolean value) {
		return redisTemplate.opsForValue().setBit(key, offset, value);
	}

	/**
	 * 将值 value 关联到 key ，并将 key 的过期时间设为 timeout
	 * @param key
	 * @param value
	 * @param timeout 过期时间
	 * @param unit 时间单位, 天:TimeUnit.DAYS 小时:TimeUnit.HOURS 分钟:TimeUnit.MINUTES
	 * 秒:TimeUnit.SECONDS 毫秒:TimeUnit.MILLISECONDS
	 */
	public static void setEx(String key, String value, long timeout, TimeUnit unit) {
		redisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	/**
	 * 只有在 key 不存在时设置 key 的值
	 * @param key
	 * @param value
	 * @return 之前已经存在返回false, 不存在返回true
	 */
	public static boolean setIfAbsent(String key, String value) {
		return redisTemplate.opsForValue().setIfAbsent(key, value);
	}

	/**
	 * 只有在 key 不存在时设置 key 的值
	 * @param key
	 * @param value
	 * @return 之前已经存在返回false, 不存在返回true
	 */
	public static boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit) {
		return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
	}

	/**
	 * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始
	 * @param key
	 * @param value
	 * @param offset 从指定位置开始覆写
	 */
	public static void setRange(String key, String value, long offset) {
		redisTemplate.opsForValue().set(key, value, offset);
	}

	/**
	 * 获取字符串的长度
	 * @param key
	 * @return
	 */
	public static Long size(String key) {
		return redisTemplate.opsForValue().size(key);
	}

	/**
	 * 批量添加
	 * @param maps
	 */
	public static void multiSet(Map<String, String> maps) {
		redisTemplate.opsForValue().multiSet(maps);
	}

	/**
	 * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在
	 * @param maps
	 * @return 之前已经存在返回false, 不存在返回true
	 */
	public static boolean multiSetIfAbsent(Map<String, String> maps) {
		return redisTemplate.opsForValue().multiSetIfAbsent(maps);
	}

	/**
	 * 增加(自增长), 负数则为自减
	 * @param key
	 * @param increment
	 * @return
	 */
	public static Long incrBy(String key, long increment) {
		return redisTemplate.opsForValue().increment(key, increment);
	}

	/**
	 * @param key
	 * @param increment
	 * @return
	 */
	public static Double incrByFloat(String key, double increment) {
		return redisTemplate.opsForValue().increment(key, increment);
	}

	/**
	 * 追加到末尾
	 * @param key
	 * @param value
	 * @return
	 */
	public static Integer append(String key, String value) {
		return redisTemplate.opsForValue().append(key, value);
	}

	/** --------------------set相关操作-------------------------- */

	/**
	 * set添加元素
	 * @param key
	 * @param values
	 * @return
	 */
	public static Long sAdd(String key, String... values) {
		return redisTemplate.opsForSet().add(key, values);
	}

	/**
	 * set移除元素
	 * @param key
	 * @param values
	 * @return
	 */
	public static Long sRemove(String key, Object... values) {
		return redisTemplate.opsForSet().remove(key, values);
	}

	/**
	 * 移除并返回集合的一个随机元素
	 * @param key
	 * @return
	 */
	public static Serializable sPop(String key) {
		return redisTemplate.opsForSet().pop(key);
	}

	/**
	 * 将元素value从一个集合移到另一个集合
	 * @param key
	 * @param value
	 * @param destKey
	 * @return
	 */
	public static Boolean sMove(String key, String value, String destKey) {
		return redisTemplate.opsForSet().move(key, value, destKey);
	}

	/**
	 * 获取集合的大小
	 * @param key
	 * @return
	 */
	public static Long sSize(String key) {
		return redisTemplate.opsForSet().size(key);
	}

	/**
	 * 判断集合是否包含value
	 * @param key
	 * @param value
	 * @return
	 */
	public static Boolean sIsMember(String key, Object value) {
		return redisTemplate.opsForSet().isMember(key, value);
	}

	/**
	 * 获取两个集合的交集
	 * @param key
	 * @param otherKey
	 * @return
	 */
	public static Set<Serializable> sIntersect(String key, String otherKey) {
		return redisTemplate.opsForSet().intersect(key, otherKey);
	}

	/**
	 * 获取key集合与多个集合的交集
	 * @param key
	 * @param otherKeys
	 * @return
	 */
	public static Set<Serializable> sIntersect(String key, Collection<String> otherKeys) {
		return redisTemplate.opsForSet().intersect(key, otherKeys);
	}

	/**
	 * key集合与otherKey集合的交集存储到destKey集合中
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public static Long sIntersectAndStore(String key, String otherKey, String destKey) {
		return redisTemplate.opsForSet().intersectAndStore(key, otherKey, destKey);
	}

	/**
	 * key集合与多个集合的交集存储到destKey集合中
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public static Long sIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
		return redisTemplate.opsForSet().intersectAndStore(key, otherKeys, destKey);
	}

	/**
	 * 获取两个集合的并集
	 * @param key
	 * @param otherKeys
	 * @return
	 */
	public static Set<Serializable> sUnion(String key, String otherKeys) {
		return redisTemplate.opsForSet().union(key, otherKeys);
	}

	/**
	 * 获取key集合与多个集合的并集
	 * @param key
	 * @param otherKeys
	 * @return
	 */
	public static Set<Serializable> sUnion(String key, Collection<String> otherKeys) {
		return redisTemplate.opsForSet().union(key, otherKeys);
	}

	/**
	 * key集合与otherKey集合的并集存储到destKey中
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public static Long sUnionAndStore(String key, String otherKey, String destKey) {
		return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
	}

	/**
	 * key集合与多个集合的并集存储到destKey中
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public static Long sUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
		return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
	}

	/**
	 * 获取两个集合的差集
	 * @param key
	 * @param otherKey
	 * @return
	 */
	public static Set<Serializable> sDifference(String key, String otherKey) {
		return redisTemplate.opsForSet().difference(key, otherKey);
	}

	/**
	 * 获取key集合与多个集合的差集
	 * @param key
	 * @param otherKeys
	 * @return
	 */
	public static Set<Serializable> sDifference(String key, Collection<String> otherKeys) {
		return redisTemplate.opsForSet().difference(key, otherKeys);
	}

	/**
	 * key集合与otherKey集合的差集存储到destKey中
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public static Long sDifference(String key, String otherKey, String destKey) {
		return redisTemplate.opsForSet().differenceAndStore(key, otherKey, destKey);
	}

	/**
	 * key集合与多个集合的差集存储到destKey中
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public static Long sDifference(String key, Collection<String> otherKeys, String destKey) {
		return redisTemplate.opsForSet().differenceAndStore(key, otherKeys, destKey);
	}

	/**
	 * 获取集合所有元素
	 * @param key
	 * @return
	 */
	public static Set<Serializable> setMembers(String key) {
		return redisTemplate.opsForSet().members(key);
	}

	/**
	 * 随机获取集合中的一个元素
	 * @param key
	 * @return
	 */
	public static Serializable sRandomMember(String key) {
		return redisTemplate.opsForSet().randomMember(key);
	}

	/**
	 * 随机获取集合中count个元素
	 * @param key
	 * @param count
	 * @return
	 */
	public static List<Serializable> sRandomMembers(String key, long count) {
		return redisTemplate.opsForSet().randomMembers(key, count);
	}

	/**
	 * 随机获取集合中count个元素并且去除重复的
	 * @param key
	 * @param count
	 * @return
	 */
	public static Set<Serializable> sDistinctRandomMembers(String key, long count) {
		return redisTemplate.opsForSet().distinctRandomMembers(key, count);
	}

	/**
	 * @param key
	 * @param options
	 * @return
	 */
	public static Cursor<Serializable> sScan(String key, ScanOptions options) {
		return redisTemplate.opsForSet().scan(key, options);
	}

	/** ------------------zSet相关操作-------------------------------- */

	/**
	 * 添加元素,有序集合是按照元素的score值由小到大排列
	 * @param key
	 * @param value
	 * @param score
	 * @return
	 */
	public static Boolean zAdd(String key, String value, double score) {
		return redisTemplate.opsForZSet().add(key, value, score);
	}

	/**
	 * @param key
	 * @param values
	 * @return
	 */
	public static Long zAdd(String key, Set<ZSetOperations.TypedTuple<Serializable>> values) {
		return redisTemplate.opsForZSet().add(key, values);
	}

	/**
	 * @param key
	 * @param values
	 * @return
	 */
	public static Long zRemove(String key, Object... values) {
		return redisTemplate.opsForZSet().remove(key, values);
	}

	/**
	 * 增加元素的score值，并返回增加后的值
	 * @param key
	 * @param value
	 * @param delta
	 * @return
	 */
	public static Double zIncrementScore(String key, String value, double delta) {
		return redisTemplate.opsForZSet().incrementScore(key, value, delta);
	}

	/**
	 * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
	 * @param key
	 * @param value
	 * @return 0表示第一位
	 */
	public static Long zRank(String key, Object value) {
		return redisTemplate.opsForZSet().rank(key, value);
	}

	/**
	 * 返回元素在集合的排名,按元素的score值由大到小排列
	 * @param key
	 * @param value
	 * @return
	 */
	public static Long zReverseRank(String key, Object value) {
		return redisTemplate.opsForZSet().reverseRank(key, value);
	}

	/**
	 * 获取集合的元素, 从小到大排序
	 * @param key
	 * @param start 开始位置
	 * @param end 结束位置, -1查询所有
	 * @return
	 */
	public static Set<Serializable> zRange(String key, long start, long end) {
		return redisTemplate.opsForZSet().range(key, start, end);
	}

	/**
	 * 获取集合元素, 并且把score值也获取
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static Set<ZSetOperations.TypedTuple<Serializable>> zRangeWithScores(String key, long start, long end) {
		return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
	}

	/**
	 * 根据Score值查询集合元素
	 * @param key
	 * @param min 最小值
	 * @param max 最大值
	 * @return
	 */
	public static Set<Serializable> zRangeByScore(String key, double min, double max) {
		return redisTemplate.opsForZSet().rangeByScore(key, min, max);
	}

	/**
	 * 根据Score值查询集合元素, 从小到大排序
	 * @param key
	 * @param min 最小值
	 * @param max 最大值
	 * @return
	 */
	public static Set<ZSetOperations.TypedTuple<Serializable>> zRangeByScoreWithScores(String key, double min,
			double max) {
		return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
	}

	/**
	 * @param key
	 * @param min
	 * @param max
	 * @param start
	 * @param end
	 * @return
	 */
	public static Set<ZSetOperations.TypedTuple<Serializable>> zRangeByScoreWithScores(String key, double min,
			double max, long start, long end) {
		return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, start, end);
	}

	/**
	 * 获取集合的元素, 从大到小排序
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static Set<Serializable> zReverseRange(String key, long start, long end) {
		return redisTemplate.opsForZSet().reverseRange(key, start, end);
	}

	/**
	 * 获取集合的元素, 从大到小排序, 并返回score值
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static Set<ZSetOperations.TypedTuple<Serializable>> zReverseRangeWithScores(String key, long start,
			long end) {
		return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
	}

	/**
	 * 根据Score值查询集合元素, 从大到小排序
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public static Set<Serializable> zReverseRangeByScore(String key, double min, double max) {
		return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
	}

	/**
	 * 根据Score值查询集合元素, 从大到小排序
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public static Set<ZSetOperations.TypedTuple<Serializable>> zReverseRangeByScoreWithScores(String key, double min,
			double max) {
		return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
	}

	/**
	 * @param key
	 * @param min
	 * @param max
	 * @param start
	 * @param end
	 * @return
	 */
	public static Set<Serializable> zReverseRangeByScore(String key, double min, double max, long start, long end) {
		return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, start, end);
	}

	/**
	 * 根据score值获取集合元素数量
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public static Long zCount(String key, double min, double max) {
		return redisTemplate.opsForZSet().count(key, min, max);
	}

	/**
	 * 获取集合大小
	 * @param key
	 * @return
	 */
	public static Long zSize(String key) {
		return redisTemplate.opsForZSet().size(key);
	}

	/**
	 * 获取集合大小
	 * @param key
	 * @return
	 */
	public static Long zZCard(String key) {
		return redisTemplate.opsForZSet().zCard(key);
	}

	/**
	 * 获取集合中value元素的score值
	 * @param key
	 * @param value
	 * @return
	 */
	public static Double zScore(String key, Object value) {
		return redisTemplate.opsForZSet().score(key, value);
	}

	/**
	 * 移除指定索引位置的成员
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static Long zRemoveRange(String key, long start, long end) {
		return redisTemplate.opsForZSet().removeRange(key, start, end);
	}

	/**
	 * 根据指定的score值的范围来移除成员
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public static Long zRemoveRangeByScore(String key, double min, double max) {
		return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
	}

	/**
	 * 获取key和otherKey的并集并存储在destKey中
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public static Long zUnionAndStore(String key, String otherKey, String destKey) {
		return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
	}

	/**
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public static Long zUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
		return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
	}

	/**
	 * 交集
	 * @param key
	 * @param otherKey
	 * @param destKey
	 * @return
	 */
	public static Long zIntersectAndStore(String key, String otherKey, String destKey) {
		return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
	}

	/**
	 * 交集
	 * @param key
	 * @param otherKeys
	 * @param destKey
	 * @return
	 */
	public static Long zIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
		return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
	}

	/**
	 * @param key
	 * @param options
	 * @return
	 */
	public static Cursor<ZSetOperations.TypedTuple<Serializable>> zScan(String key, ScanOptions options) {
		return redisTemplate.opsForZSet().scan(key, options);
	}

	/**
	 * 执行脚本
	 * @param script
	 * @param keys
	 * @param args
	 * @param <T>
	 * @return
	 */
	public static <T> T execute(RedisScript<T> script, List<String> keys, Object... args) {
		return redisTemplate.execute(script, keys, args);
	}

	public static <T> T execute(RedisCallback<T> action) {
		return redisTemplate.execute(action);
	}

	public static <T> T execute(RedisCallback<T> action, boolean exposeConnection) {
		return execute(action, exposeConnection, false);
	}

	public static <T> T execute(RedisCallback<T> action, boolean exposeConnection, boolean pipeline) {
		return redisTemplate.execute(action, exposeConnection, pipeline);
	}

	public static <T> T execute(SessionCallback<T> session) {
		return redisTemplate.execute(session);
	}

	public static <T> T execute(RedisScript<T> script, RedisSerializer<?> argsSerializer,
			RedisSerializer<T> resultSerializer, List<String> keys, Object... args) {
		return redisTemplate.execute(script, argsSerializer, resultSerializer, keys, args);
	}

	/**
	 * 操作 pipelined
	 * @param session
	 * @return
	 */
	public static List<Object> executePipelined(SessionCallback<?> session) {
		return redisTemplate.executePipelined(session);
	}

	public static List<Object> executePipelined(SessionCallback<?> session, RedisSerializer<?> resultSerializer) {
		return redisTemplate.executePipelined(session, resultSerializer);
	}

	public static List<Object> executePipelined(RedisCallback<?> action) {
		return redisTemplate.executePipelined(action);
	}

	public static List<Object> executePipelined(RedisCallback<?> action, RedisSerializer<?> resultSerializer) {
		return redisTemplate.executePipelined(action, resultSerializer);
	}

}
