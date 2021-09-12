package cn.xjbpm.ultron.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Slf4j
@Configuration
@ComponentScan(basePackageClasses = JpaAutoConfig.class)
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaAutoConfig {

}
