package cn.xjbpm.ultron.id.generator.service.impl;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.xjbpm.ultron.id.generator.service.GlobalIdService;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author 黄川 huchuc@vip.qq.com 2020/9/30
 */
@Slf4j
public class SnowflakeIdServiceImpl implements GlobalIdService {

	/**
	 * 2020-11-30 21:06
	 */
	private final long twepoch = 1606741544090L;

	private final long workerIdBits = 9L;

	private final long datacenterIdBits = 5L;

	/**
	 * 最大支持机器节点数0~255，一共511个, 符合IP最大值,如果使用8L则缺少1位255IP存在问题
	 */
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

	/**
	 * 最大支持数据中心节点数0~31，一共32个
	 */
	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

	/**
	 * 序列号12位
	 */
	private final long sequenceBits = 12L;

	/**
	 * 机器节点左移12位
	 */
	private final long workerIdShift = sequenceBits;

	/**
	 * 数据中心节点左移17位
	 */
	private final long datacenterIdShift = sequenceBits + workerIdBits;

	/**
	 * 时间毫秒数左移22位
	 */
	private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

	/**
	 * 4095
	 */
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);

	private long workerId;

	private long datacenterId;

	private long sequence = 0L;

	private long lastTimestamp = -1L;

	private boolean useSystemClock;

	public SnowflakeIdServiceImpl() {
		this.datacenterId = 1;
		// 取IP末尾数字来当workerId,ip最大255，当前允许为511
		Long workerId = Long.parseLong(NetUtil.getLocalhostStr().split("\\.")[3]);
		log.debug("Init GlobalIdService by workerId:{} dataCenterId:{}", workerId, this.datacenterId);
		this.workerId = workerId;
		this.useSystemClock = false;
		verification();
	}

	public SnowflakeIdServiceImpl(long datacenterId, long workerId) {
		this.datacenterId = datacenterId;
		this.workerId = workerId;
		this.useSystemClock = false;
		verification();
	}

	private void verification() {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(
					StrUtil.format("worker Id can't be greater than {} or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(
					StrUtil.format("datacenter Id can't be greater than {} or less than 0", maxDatacenterId));
		}
	}

	/**
	 * 根据Snowflake的ID，获取机器id
	 * @param id snowflake算法生成的id
	 * @return 所属机器的id
	 */
	public long getWorkerId(long id) {
		return id >> workerIdShift & ~(-1L << workerIdBits);
	}

	/**
	 * 根据Snowflake的ID，获取数据中心id
	 * @param id snowflake算法生成的id
	 * @return 所属数据中心
	 */
	public long getDataCenterId(long id) {
		return id >> datacenterIdShift & ~(-1L << datacenterIdBits);
	}

	/**
	 * 根据Snowflake的ID，获取生成时间
	 * @param id snowflake算法生成的id
	 * @return 生成的时间
	 */
	public long getGenerateDateTime(long id) {
		return (id >> timestampLeftShift & ~(-1L << 41L)) + twepoch;
	}

	/**
	 * 通过hostName最后数字来进行设置 workerId 要求机器名称最后以数字结束,一个机器部署一个实例，例如：生产-中台-流程中心-01
	 * @return
	 */
	public synchronized Long getWorkerIdByHostName() {
		Long workerId;
		String hostName = null;
		try {
			InetAddress address = InetAddress.getLocalHost();
			hostName = address.getHostName();
			workerId = Long.valueOf(hostName.replace(hostName.replaceAll("\\d+$", ""), ""));
		}
		catch (UnknownHostException e) {
			throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
		}
		catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					String.format("Wrong hostname:%s, hostname must be end with number!", hostName));
		}
		log.debug("hostName:{} workerId:{}", hostName, workerId);
		return workerId;
	}

	/**
	 * 下一个ID
	 * @return ID
	 */
	@Override
	public synchronized long nextLongId() {
		long timestamp = genTime();
		if (timestamp < lastTimestamp) {
			// 如果服务器时间有问题(时钟后退) 报错。
			throw new IllegalStateException(StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms",
					lastTimestamp - timestamp));
		}
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		}
		else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
				| (workerId << workerIdShift) | sequence;
	}

	/**
	 * 下一个ID（字符串形式）
	 * @return ID 字符串形式
	 */
	@Override
	public synchronized String nextStringId() {
		return Long.toString(nextLongId());
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// Private method start

	/**
	 * 循环等待下一个时间
	 * @param lastTimestamp 上次记录的时间
	 * @return 下一个时间
	 */
	private long tilNextMillis(long lastTimestamp) {
		long timestamp = genTime();
		while (timestamp <= lastTimestamp) {
			timestamp = genTime();
		}
		return timestamp;
	}

	/**
	 * 生成时间戳
	 * @return 时间戳
	 */
	private long genTime() {
		return this.useSystemClock ? SystemClock.now() : System.currentTimeMillis();
	}
	// ------------------------------------------------------------------------------------------------------------------------------------
	// Private method end

}
