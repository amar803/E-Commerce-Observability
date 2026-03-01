package com.amar.lab.order;

import com.amar.lab.common.web.TraceIdFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<TraceIdFilter> traceIdFilter() {
        FilterRegistrationBean<TraceIdFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new TraceIdFilter());
        bean.setOrder(1);
        return bean;
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(2))
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 1500);

        return builder.clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }
}
