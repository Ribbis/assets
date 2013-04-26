package com.wisecode.model.common.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Collections 工具集
 * 在jdk的Collections和Guava的Collections2后
 */
@SuppressWarnings("rawtypes")
public class Collections {


    public static String extractToString(final Collection collection,final String propertyName,final String separator){
        List list = extractToList(collection,propertyName);
        return StringUtils.join(list,separator);

    }

    @SuppressWarnings("unchecked")
    public static List extractToList(final Collection collection,final String propertyName){
        List  list = new ArrayList(collection.size());
        try {
            for (Object obj : collection){
                     list.add(PropertyUtils.getProperty(obj,propertyName));
            }
        } catch (Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }
        return list;
    }
}
