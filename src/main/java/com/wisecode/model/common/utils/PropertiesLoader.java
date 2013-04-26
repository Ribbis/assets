package com.wisecode.model.common.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;


/**
 * Properties 文件载入工具，可载入多个 Properties 文件，相同的属性在最后载入的文件中的值将会覆盖之前的值，但以System的Property优先级
 */
public class PropertiesLoader {

    private static Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    private final Properties properties;

    public PropertiesLoader(String... resourcePath) {
        properties = loadProperties(resourcePath);
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * 取出 String 类型的 Property,但是以System的Property优先，如果都为Null则抛出异常
     * @param key
     * @return
     */
    public String getProperty(String key){
        String value = getValue(key);
        if (value == null){
                throw new NoSuchElementException();
        }
        return value;
    }


    private String getValue(String key){
        String systemProperty = System.getProperty(key);
        if (systemProperty != null){
            return systemProperty;
        }
        return properties.getProperty(key);
    }

    /**
     * 载入多个文件，文件路径使用Spring Resource格式
     * @param resourcePath
     * @return
     */
    private Properties loadProperties(String... resourcePath){
        Properties pros = new Properties();
        for (String location : resourcePath) {
            InputStream is = null;
            try {
                Resource resource = resourceLoader.getResource(location);
                is =  resource.getInputStream();
                pros.load(is);
            } catch (Exception e) {
                logger.info("Could not load properties from path:" + location +","+e.getMessage());
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
        return pros;
    }

}