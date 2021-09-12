package cn.xjbpm.ultron.mybaitsplus;

import cn.xjbpm.common.yaml.YamlPropertySourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/12
 */
@Slf4j
@Configuration
@ComponentScan(basePackageClasses = MybaitsPlusAutoConfig.class)
@EnableTransactionManagement
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:mybatis-plus.yml")
public class MybaitsPlusAutoConfig {

}
