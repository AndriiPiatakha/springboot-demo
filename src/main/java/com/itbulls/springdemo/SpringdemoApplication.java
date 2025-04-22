package com.itbulls.springdemo;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;

@SpringBootApplication
@EnableAsync // Включаем поддержку асинхронных методов
public class SpringdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringdemoApplication.class, args);
	}
	
//	@Bean(name = "taskExecutor")
//    public Executor taskExecutor() {
//        return Executors.newVirtualThreadPerTaskExecutor();
//    }
	
//	@Bean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
//	public AsyncTaskExecutor asyncTaskExecutor() {
//	  return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
//	}
//
//	@Bean
//	public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
//	  return protocolHandler -> {
//	    protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
//	  };
//	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable();
        return http.build();
    }

}
