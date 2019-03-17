package edu.simrobot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 配置类
 * 就是注入了 ObjectMapper 对象
 * */

@Configuration
public class JacksonConfigure {
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
