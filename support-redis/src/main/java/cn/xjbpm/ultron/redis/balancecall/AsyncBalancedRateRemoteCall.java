package cn.xjbpm.ultron.redis.balancecall;

import cn.hutool.core.thread.ThreadUtil;
import cn.xjbpm.ultron.common.component.threadpool.BusinessCommonTaskExecutorContextHolder;
import cn.xjbpm.ultron.redis.util.RedisTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author 黄川 huchuc@vip.qq.com 异步平衡远程调用
 */
@Component
@Slf4j
public class AsyncBalancedRateRemoteCall implements BalancedRateRemoteCall, InitializingBean {

	/**
	 * 限流脚本
	 */
	private final RedisScript<Long> tokenBucketLimitRedisScript;

	/**
	 * 内部暂存队列
	 */
	private final BlockingQueue<BalancedRateRemoteCallEvent> blockingQueue = new LinkedBlockingQueue<>();

	@Autowired
	public AsyncBalancedRateRemoteCall(RedisScript<Long> tokenBucketLimitRedisScript) {
		this.tokenBucketLimitRedisScript = tokenBucketLimitRedisScript;
	}

	@Override
	public void publishEvent(BalancedRateRemoteCallEvent balancedRateRemoteCallEvent) {
		blockingQueue.add(balancedRateRemoteCallEvent);
	}

	/***
	 * 消费
	 */
	private void consume(BalancedRateRemoteCallEvent peek) {
		try {
			long now = System.currentTimeMillis();
			// 应该移除的分值区间
			long removeScore = now - peek.limitWindowTime();
			Long num = RedisTemplateUtil.execute(tokenBucketLimitRedisScript, Arrays.asList(peek.getRedisTokenKey()),
					now, peek.limitWindowTime(), removeScore, peek.allowedMax());
			// num 大于0表示令牌放入成功可以执行业务。 num等于0则表示令牌桶满了，放不下，当前业务不执行
			if (Objects.nonNull(num) && num > 0) {
				Optional.ofNullable(blockingQueue.poll()).ifPresent(balancedRateRemoteCallEvent -> {
					log.info("异步平衡远程调用消费,队列中还剩:{}", blockingQueue.size());
					BusinessCommonTaskExecutorContextHolder.execute(balancedRateRemoteCallEvent::call);
				});
			}
		}
		catch (Exception e) {
			log.error("异步平衡远程调用消费方法出现异常：", e);
		}
	}

	@Override
	@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
	public void afterPropertiesSet() {
		Thread thread = new Thread("AsyncBalancedRateRemoteCallBlockingQueueListener") {
			@Override
			public void run() {
				while (true) {
					Optional.ofNullable(blockingQueue.peek()).ifPresent(balancedRateRemoteCallEvent -> {
						try {
							consume(balancedRateRemoteCallEvent);
						}
						catch (Exception e) {
							e.printStackTrace();
							log.error("消费消息方法出现异常：", e);
						}
					});
					ThreadUtil.sleep(0, TimeUnit.MILLISECONDS);
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
	}

}
