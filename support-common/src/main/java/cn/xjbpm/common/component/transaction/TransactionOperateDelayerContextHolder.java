package cn.xjbpm.common.component.transaction;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalApplicationListenerAdapter;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/3/4 14:29 事务后置处理器
 * @see TransactionalApplicationListenerAdapter
 */
@Component
@Slf4j
public class TransactionOperateDelayerContextHolder {

	/***
	 * 事务线程对应延迟操作事件绑定
	 */
	private static final ThreadLocal<Map<String, BlockingQueue<CallableEvent>>> AFTER_TRANSACTION_OPTS_THREAD_LOCAL = new NamedThreadLocal<>(
			"事务交易成功后执行操作");

	/**
	 * spring 事务默认为全类名加方法名
	 * <p>
	 * 编程事务可能会null
	 * @return
	 */
	private static String getTransactionName() {
		String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
		if (StrUtil.isBlank(transactionName)) {
			return "default-transaction-name";
		}
		return transactionName;
	}

	/**
	 * 事务提交成功后执行
	 * @param executable
	 */
	public static void executeAfterTransactionCommit(CallableEvent executable) {
		if (TransactionSynchronizationManager.isSynchronizationActive()
				&& TransactionSynchronizationManager.isActualTransactionActive()) {
			// 当前操作在活跃的事务中
			TransactionSynchronizationManager.registerSynchronization(AfterTransactionSynchronizationAdapter.INSTANCE);
			BlockingQueue<CallableEvent> eventExecutables = getExecutablesCreateIfNecessary();
			eventExecutables.add(executable);
		}
		else {
			throw new RuntimeException("事务后置操作必须在一个活跃的事务中");
		}
	}

	/**
	 * 从threadlocal获取执行器栈,如果没有拿到,就创建一个 要根据事务名称区分事务
	 * @return
	 */
	private static BlockingQueue<CallableEvent> getExecutablesCreateIfNecessary() {
		Map<String, BlockingQueue<CallableEvent>> eventExecutableMap = AFTER_TRANSACTION_OPTS_THREAD_LOCAL.get();
		String transactionName = getTransactionName();
		if (eventExecutableMap == null) {
			eventExecutableMap = new HashMap<>(10);
			AFTER_TRANSACTION_OPTS_THREAD_LOCAL.set(eventExecutableMap);
		}
		BlockingQueue<CallableEvent> executables = eventExecutableMap.get(transactionName);
		if (executables == null) {
			executables = new LinkedBlockingQueue<>();
			eventExecutableMap.put(transactionName, executables);
		}
		return executables;
	}

	/**
	 * 事务同步回调 事务提交之后,执行延迟操作
	 */
	@Slf4j
	private final static class AfterTransactionSynchronizationAdapter implements TransactionSynchronization {

		public static final AfterTransactionSynchronizationAdapter INSTANCE = new AfterTransactionSynchronizationAdapter();

		private AfterTransactionSynchronizationAdapter() {
		}

		/**
		 * 事务完成之后(包括回滚), 清除绑定的操作
		 * @param status
		 */
		@Override
		public void afterCompletion(int status) {
			Map<String, BlockingQueue<CallableEvent>> map = AFTER_TRANSACTION_OPTS_THREAD_LOCAL.get();
			if (map != null) {
				map.remove(getTransactionName());
			}
		}

		/**
		 * 事务提交成功后 如果事务没有成功提交,绑定的操作会在afterCompletion方法中清除
		 */
		@Override
		public void afterCommit() {
			Map<String, BlockingQueue<CallableEvent>> BlockingQueueMap = AFTER_TRANSACTION_OPTS_THREAD_LOCAL.get();
			String transactionName = getTransactionName();
			BlockingQueue<CallableEvent> eventExecutables = BlockingQueueMap.get(transactionName);
			if (Objects.nonNull(eventExecutables) && !eventExecutables.isEmpty()) {
				while (!eventExecutables.isEmpty()) {
					CallableEvent eventExecutable = eventExecutables.poll();
					if (Objects.nonNull(eventExecutable)) {
						try {
							eventExecutable.exec();
						}
						catch (Throwable e) {
							log.error("执行事务后置操作出错, transactionName={}, msg={}", transactionName, e.getMessage(), e);
						}
					}
				}
				eventExecutables.clear();
			}
		}

	}

}
