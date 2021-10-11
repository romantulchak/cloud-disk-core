package com.romantulchak.clouddisk.config;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolver;

import java.io.IOException;


public class CustomPathResourceResolver extends PathResourceResolver implements ResourceResolver {

    @Override
    protected Resource getResource(String resourcePath, Resource location) throws IOException {
        resourcePath = resourcePath.replace("%2520","%20");
        return super.getResource(resourcePath, location);
    }
}
