package com.romantulchak.clouddisk.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class FileConfig implements WebMvcConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileConfig.class);

    @Value("${cloud.disk.path.pattern}")
    private String pathPattern;

    @Value("${cloud.disk.files.location}")
    private String location;

    @Value("${cloud.disk.files.folder}")
    private String drivePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        createMainFolder();
        registry.addResourceHandler(pathPattern)
                .addResourceLocations(location)
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new CustomPathResourceResolver());
    }

    private void createMainFolder(){
        File file = new File(drivePath);
        if (!file.exists()){
            boolean isDriveCreated = file.mkdir();
            LOGGER.debug("Is drive created ? {}", isDriveCreated);
        }
    }
}
