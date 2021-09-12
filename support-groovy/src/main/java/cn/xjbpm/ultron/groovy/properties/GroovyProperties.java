package cn.xjbpm.ultron.groovy.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/7/30
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "groovy.script")
public class GroovyProperties {

	/**
	 * 检查刷新的时间
	 */
	private Long refreshCheckDelaySecond = TimeUnit.SECONDS.toMillis(30);

	/**
	 * 是否启用脚本扫秒
	 */
	private Boolean enableScanScript = true;

	/**
	 * 扫描间隔
	 */
	private Long scanScriptSecond = TimeUnit.SECONDS.toMillis(30);

	/**
	 * 是否是来源于数据库，否则就是 class:groovy 目录下
	 */
	private Boolean dataBaseScriptSource;

}
