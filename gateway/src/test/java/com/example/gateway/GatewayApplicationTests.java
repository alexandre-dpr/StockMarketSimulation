package com.example.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.cloud.consul.enabled=false", "spring.cloud.consul.discovery.enabled=false", "spring.cloud.consul.config.enabled=false"})
class GatewayApplicationTests {

    @Test
    void contextLoads() {
    }

}
