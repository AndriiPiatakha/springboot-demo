package com.itbulls.springdemo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.lettuce.core.RedisClient;

import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.redisson.RedissonBucket4jExtension;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableAsync // Enable support for asynchronous methods
@EnableMethodSecurity
public class SpringdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringdemoApplication.class, args);
	}

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

	// Disable Spring Security Login When Needed. For JMeter test
//	@Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http
//		    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//		    .csrf(csrf -> csrf.disable())
//		    .formLogin(form -> form.disable())
//		    .httpBasic(httpBasic -> httpBasic.disable());
//        return http.build();
//    }

	
	 // 1. Filter for /payments — JWT only
    @Bean
    @Order(1)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/payments", "/debug") // только для /payments
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt());

        return http.build();
    }

    // 2. Filter for everything else — login form
    @Bean
    @Order(2)
    public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .oauth2Login(Customizer.withDefaults());

        return http.build();
    }
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	    return builder.build(); // Micrometer automatically wraps with traceability
	}
	
	
	// =================== Rate Limiting Demo
	
//    @Bean
//    public RedisClient redisClient() {
//        return RedisClient.create("redis://localhost:6379");
//    }
//
//    @Bean
//    public RedisBucketBuilder bucketBuilder(RedisClient redisClient) {
//        return Bucket4j.extension(io.github.bucket4j.redis.lettuce.RedisBucket4jExtension.getDefault())
//                .builder()
//                .withClient(redisClient);
//    }
	
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        return Redisson.create(config);
    }

    @Bean
    public ProxyManager<String> proxyManager(RedissonClient redissonClient) {
        return new RedissonBucket4jExtension()
                .wrap(redissonClient.getMap("buckets"));
    }
}
