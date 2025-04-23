package com.itbulls.springdemo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;

@SpringBootApplication
@EnableAsync // Enable support for asynchronous methods
@EnableMethodSecurity
@EnableWebSecurity
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
	
	
	
//	@Bean
//	public JwtDecoder jwtDecoder() {
//	    return token -> {
//	        Jwt jwt = Jwt.withTokenValue(token)
//	            .header("alg", "none")
//	            .claim("sub", "demo-user")
//	            .claim("permissions", List.of("read:payments"))
//	            .build();
//
//	        return jwt;
//	    };
//	}
//	
//	@Bean
//	public JwtDecoder jwtDecoder() {
//	    String secret = "secret"; // <-- это твой ключ
//	    SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
//	    return NimbusJwtDecoder.withSecretKey(key).build();
//	}
	
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
	    // Конвертер для извлечения authorities из claim "scope"
	    JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
	    authoritiesConverter.setAuthorityPrefix(""); // Убрать префикс "SCOPE_"
	    authoritiesConverter.setAuthoritiesClaimName("scope"); // Указать нужный claim
	    System.out.println("====================================");
	    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
	    jwtConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
	    return jwtConverter;
	}
	
	
	// Disable Spring Security Login When Needed. 
//	@Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http
//		    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//		    .csrf(csrf -> csrf.disable())
//		    .formLogin(form -> form.disable())
//		    .httpBasic(httpBasic -> httpBasic.disable());
//        return http.build();
//    }
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
	        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
	    return http.build();
	}

}
