package cn.xjbpm.ultron.web.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/5/29 当前系统的中的异步线程池指标情况
 */
@RestController
@RequestMapping("/mvc/api/v1/threadPool")
public class ThreadPoolController {

	private List<Executor> executorList;

	@Lazy
	@Autowired
	public void setExecutorList(List<Executor> executorList) {
		this.executorList = executorList;
	}

	@GetMapping("/list")
	public List<Map<String, Object>> list() {
		List<Map<String, Object>> list = new ArrayList<>();
		executorList.stream().filter(executor -> executor instanceof ThreadPoolTaskExecutor).forEach(executor -> {
			ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor) executor;
			ThreadPoolExecutor poolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("ThreadNamePrefix（线程名前缀）", threadPoolTaskExecutor.getThreadNamePrefix());
			map.put("CorePoolSize（核心线程数）", poolExecutor.getCorePoolSize());
			map.put("MaxPoolSize（最大线程池大小）", threadPoolTaskExecutor.getMaxPoolSize());
			map.put("ActiveCount（正在执行任务的线程数）", poolExecutor.getActiveCount());
			map.put("TaskCount（计划执行的任务总数）", poolExecutor.getTaskCount());
			map.put("CompletedTaskCount（已完成任务总数）", poolExecutor.getCompletedTaskCount());
			map.put("MaximumPoolSize（允许的最大线程数）", poolExecutor.getMaximumPoolSize());
			map.put("LargestPoolSize（历史峰值线程数）", poolExecutor.getLargestPoolSize());
			map.put("PoolSize（当前池中的线程数）", poolExecutor.getPoolSize());
			map.put("KeepAliveTime（空闲时间）", poolExecutor.getKeepAliveTime(TimeUnit.SECONDS));
			map.put("Queue.size（当前任务队列中积压任务的总数）", poolExecutor.getQueue().size());
			map.put("RemainingCapacity（队列大小）", poolExecutor.getQueue().remainingCapacity());
			list.add(map);
		});
		executorList.stream().filter(executor -> executor instanceof ThreadPoolTaskScheduler).forEach(executor -> {
			ThreadPoolTaskScheduler threadPoolTaskExecutor = (ThreadPoolTaskScheduler) executor;
			ScheduledThreadPoolExecutor poolExecutor = threadPoolTaskExecutor.getScheduledThreadPoolExecutor();
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("ThreadNamePrefix（线程名前缀）", threadPoolTaskExecutor.getThreadNamePrefix());
			map.put("CorePoolSize（核心线程数）", poolExecutor.getCorePoolSize());
			map.put("ActiveCount（正在执行任务的线程数）", poolExecutor.getActiveCount());
			map.put("TaskCount（计划执行的任务总数）", poolExecutor.getTaskCount());
			map.put("CompletedTaskCount（已完成任务总数）", poolExecutor.getCompletedTaskCount());
			map.put("MaximumPoolSize（允许的最大线程数）", poolExecutor.getMaximumPoolSize());
			map.put("LargestPoolSize（历史峰值线程数）", poolExecutor.getLargestPoolSize());
			map.put("PoolSize（当前池中的线程数）", poolExecutor.getPoolSize());
			map.put("KeepAliveTime（空闲时间）", poolExecutor.getKeepAliveTime(TimeUnit.SECONDS));
			map.put("Queue.size（当前任务队列中积压任务的总数）", poolExecutor.getQueue().size());
			map.put("RemainingCapacity（队列大小）", poolExecutor.getQueue().remainingCapacity());
			list.add(map);
		});
		return list;
	}

}
