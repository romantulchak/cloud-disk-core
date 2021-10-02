package com.romantulchak.clouddisk.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

public class FileConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/cloud-disk-files/**")
                .addResourceLocations("file:E:\\cloud-disk-files")
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
