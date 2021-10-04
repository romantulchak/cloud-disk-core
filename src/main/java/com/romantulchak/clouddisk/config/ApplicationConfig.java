package com.romantulchak.clouddisk.config;

import com.mapperDTO.mapper.EntityMapper;
import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.utils.FileUtils;
import com.romantulchak.clouddisk.utils.FolderUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.mapperDTO")
public class ApplicationConfig {

    @Value("${cloud.disk.files.folder}")
    private String drivePath;

    @Value("${cloud.disk.host}")
    private String host;


    @Bean
    public EntityMapper newEntityMapper(){
        return new EntityMapper();
    }
    @Bean
    public EntityMapperInvoker<Object, Object> newEntityMapperInvoker(){
        return new EntityMapperInvoker<>();
    }

    @Bean
    public FolderUtils newFileUtils(){
        return new FolderUtils(drivePath, host);
    }
}
