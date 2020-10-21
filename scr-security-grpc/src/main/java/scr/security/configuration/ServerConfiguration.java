package scr.security.configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.channel.EventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.nio.NioEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.socket.nio.NioServerSocketChannel;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import scr.sds.RestAsyncClient;

@Configuration
public class ServerConfiguration {

	@Bean
	public GrpcServerConfigurer keepAliveServerConfigurer() {
	    return serverBuilder -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workGroup = new NioEventLoopGroup(4);
	        if (serverBuilder instanceof NettyServerBuilder) {
	            ((NettyServerBuilder) serverBuilder)
		                .keepAliveTime(30, TimeUnit.SECONDS)
		                .keepAliveTimeout(5, TimeUnit.SECONDS)
		                .bossEventLoopGroup(bossGroup)
		                .workerEventLoopGroup(workGroup)
		                .permitKeepAliveTime(30, TimeUnit.SECONDS)
		                .permitKeepAliveWithoutCalls(true)
		                .channelType(NioServerSocketChannel.class);
	        }
	    };
	}
	
    @Bean("sds_client")
    public RestAsyncClient getRestAsyncClient() {
        RestAsyncClient client = new RestAsyncClient("localhost", 8090, 10, false, 4);
        client.start();
        return client;
    }
    
    @Bean("sds_executor")
    public static ThreadPoolExecutor getThreadPoolExecutor() {

    	return new ThreadPoolExecutor(
                4, 40, 5, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(5000)
        );
    }
	
	@Bean
	public RestTemplate getRestTemplate() {
	      return new RestTemplate();
	}
	
	@Bean
	public AsyncRestTemplate getRestTemplateAsync() {
	      return new AsyncRestTemplate();
	}
	
	@Bean(name= "repoExecutor")
	public Executor taskExecutor() {
		
	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	    executor.setCorePoolSize(10);
	    executor.setMaxPoolSize(10);
	    executor.setQueueCapacity(50);
	    executor.setThreadNamePrefix("myExecutorThread-");
	    executor.initialize();
	    return executor;
	}
}
