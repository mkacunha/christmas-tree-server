package br.com.db1.christmastree.domain.infraestrutura;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncBean {

	private static final Integer FIX_THREADS = Runtime.getRuntime().availableProcessors() * 2;

	@Bean
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(FIX_THREADS);
		executor.setQueueCapacity(500);
		executor.initialize();
		return executor;
	}

}
