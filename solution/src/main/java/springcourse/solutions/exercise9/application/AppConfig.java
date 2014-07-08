package springcourse.solutions.exercise9.application;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author Amit Tal
 * @since 3/24/14
 */
@Configuration
@EnableMBeanExport
@ComponentScan({"springcourse.solutions.exercise9.dao", "springcourse.solutions.exercise9.service", "springcourse.solutions.exercise9.util"})
@PropertySource({"classpath:books-analyzer.properties", "classpath:members.properties"})
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer configurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
