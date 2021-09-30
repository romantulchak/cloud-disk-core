package com.romantulchak.clouddisk.config;

import com.mapperDTO.mapper.EntityMapper;
import com.mapperDTO.mapper.EntityMapperInvoker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.mapperDTO")
public class ApplicationConfig {
    @Bean
    public EntityMapper newEntityMapper(){
        return new EntityMapper();
    }
    @Bean
    public EntityMapperInvoker<Object, Object> newEntityMapperInvoker(){
        return new EntityMapperInvoker<>();
    }
}
