package cn.xjbpm.common.component.threadpool;

import cn.xjbpm.common.constant.TaskExecutorBeanNameConstant;
import cn.xjbpm.common.util.BeanContextUtil;
import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/12 TransmittableThreadLocal实现父子线程之间的数据传递
 */
public abstract class BusinessCommonTaskExecutorContextHolder {

	private static final ThreadPoolTaskExecutor threadPoolTaskExecutor;

	static {
		threadPoolTaskExecutor = BeanContextUtil.getBeanOfNameAndType(TaskExecutorBeanNameConstant.BUSINESS_COMMON,
				ThreadPoolTaskExecutor.class);
	}

	public static void execute(Runnable runnable) {
		threadPoolTaskExecutor.execute(TtlRunnable.get(runnable));
	}

	public static void execute(Runnable runnable, long startTimeout) {
		threadPoolTaskExecutor.execute(TtlRunnable.get(runnable), startTimeout);
	}

	public static Future<?> submit(Runnable runnable) {
		return threadPoolTaskExecutor.submit(TtlRunnable.get(runnable));
	}

	public static <T> Future<T> submit(Callable<T> callable) {
		return threadPoolTaskExecutor.submit(TtlCallable.get(callable));
	}

	public static ListenableFuture<?> submitListenable(Runnable task) {
		return threadPoolTaskExecutor.submitListenable(TtlRunnable.get(task));
	}

	public static <T> ListenableFuture<T> submitListenable(Callable<T> task) {
		return threadPoolTaskExecutor.submitListenable(TtlCallable.get(task));
	}

}
