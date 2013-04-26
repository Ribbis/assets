package com.wisecode.model.common.config;

import com.wisecode.model.common.utils.PropertiesLoader;

/**
 * 全局配置类
 */
public class Global {

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader propertiesLoader;

    /**
     * 设置管理客户端访问路径（ADMIN_PATH或FROM_PATH可以允许一个为空）
     */
    public static final String Assets_PATH = "/abc";

    /**
     * 设置网站前端路径（ADMIN_PATH或FROM_PATH可以允许一个为空）
     */
    public static final String FROM_PATH = "/abd";

    /**
     * 设置访问URL后缀
     */
    public static final String URL_SUFFIX = ".html";

    public static String getConfig(String key){
        if(propertiesLoader == null){
            propertiesLoader = new PropertiesLoader("application.properties");
        }
        return propertiesLoader.getProperty(key);
    }


    public static String getAssetsPath(){
        return Assets_PATH;
    }
    public static String getFromPath(){
        return FROM_PATH;
    }
    public  static String getUrlSuffix(){
        return URL_SUFFIX;
    }

}
