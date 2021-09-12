package cn.xjbpm.common.config;

import cn.xjbpm.common.component.threadpool.MDCThreadPoolTaskDecorator;
import cn.xjbpm.common.constant.TaskExecutorBeanNameConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 黄川 2021/1/15
 */
@EnableAsync
@EnableScheduling
@Configuration
public class ThreadPoolTaskExecutorConfig implements AsyncConfigurer {

	private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

	/**
	 * CPU密集型：核心线程数 = CPU核数 + 1 IO密集型：核心线程数 = CPU核数 * 2
	 * <p>
	 * https://www.jianshu.com/p/171e51e13129
	 */
	private static final int CORE_POOL_SIZE = PROCESSORS + 1;

	private static final int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE * 2;

	/**
	 * 队列最大长度
	 */
	private static final int QUEUE_CAPACITY = 1024;

	/**
	 * 线程池维护线程所允许的空闲时间
	 */
	private static final int KEEP_ALIVE_SECONDS = Math.toIntExact(Duration.ofMinutes(1).getSeconds());

	/**
	 * 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
	 */
	private static final int AWAIT_TERMINATION_SECONDS = Math.toIntExact(Duration.ofMinutes(1).getSeconds());

	/**
	 * 此处很关键 设置默认使用 asyncTaskExecutor 的bean对象
	 * @return
	 */
	@Override
	public Executor getAsyncExecutor() {
		return asyncTaskExecutor();
	}

	/**
	 * async 异步线程池
	 * @return
	 */
	@Lazy
	@Bean(name = TaskExecutorBeanNameConstant.ASYNC_TASK_EXECUTOR)
	@ConditionalOnMissingBean(name = TaskExecutorBeanNameConstant.ASYNC_TASK_EXECUTOR)
	public AsyncTaskExecutor asyncTaskExecutor() {
		return buildThreadPoolTaskExecutor("async-thread-pool-excutor-");
	}

	/**
	 * 定时任务线程池
	 * @return
	 */
	@Lazy
	@Bean(name = TaskExecutorBeanNameConstant.TASK_SCHEDULER)
	@ConditionalOnMissingBean(name = TaskExecutorBeanNameConstant.TASK_SCHEDULER)
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
		executor.setThreadNamePrefix("taskscheduler-thread-pool-excutor-");
		executor.setPoolSize(PROCESSORS);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(AWAIT_TERMINATION_SECONDS);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return executor;
	}

	/**
	 * 系统业务统一使用的异步线程池
	 * @return
	 */
	@Lazy
	@Bean(name = TaskExecutorBeanNameConstant.BUSINESS_COMMON)
	@ConditionalOnMissingBean(name = TaskExecutorBeanNameConstant.BUSINESS_COMMON)
	public Executor commonTaskExecutor() {
		return buildThreadPoolTaskExecutor("business-common-thread-pool-excutor-");
	}

	/**
	 * 啥也没做就是给默认的 task 线程池加个名称
	 * @return
	 */
	@Bean
	public TaskExecutorCustomizer taskExecutorCustomizer() {
		return executor -> executor.setThreadNamePrefix("定时任务线程池,已被覆盖，默认这里是不会有任何执行记录");
	}

	private ThreadPoolTaskExecutor buildThreadPoolTaskExecutor(String threadNamePrefix) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix(threadNamePrefix);
		executor.setCorePoolSize(CORE_POOL_SIZE);
		executor.setMaxPoolSize(MAXIMUM_POOL_SIZE);
		executor.setQueueCapacity(QUEUE_CAPACITY);
		executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(AWAIT_TERMINATION_SECONDS);
		executor.setTaskDecorator(new MDCThreadPoolTaskDecorator());
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}

}
