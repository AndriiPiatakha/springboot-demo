package com.itbulls.springdemo;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
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
	
    @Bean
    public RedisClient redisClient() {
        return RedisClient.create("redis://localhost:6379");
    }

    @Bean
    public ProxyManager<String> proxyManager(RedisClient redisClient) {
        StatefulRedisConnection<String, byte[]> connection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
        return LettuceBasedProxyManager.builderFor(connection).build();
    }
    
    @Bean
    public Supplier<BucketConfiguration> bucketConfiguration() {
        return () -> {
            Bandwidth limit = Bandwidth.builder()
                    .capacity(10)
                    .refillGreedy(10, Duration.ofMinutes(1))
                    .build();

            return BucketConfiguration.builder()
                    .addLimit(limit)
                    .build();
        };
    }
	
}
