package com.WarehouseAPI.WarehouseAPI.socket;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class AsyncConfig {
    // Configuration class to enable asynchronous processing
}
