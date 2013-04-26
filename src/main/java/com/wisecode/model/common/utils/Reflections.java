package com.wisecode.model.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *  反射工具类
 *  提供调用 getter/setter方法，调用私有变量，调用私有方法，获取泛型类型的Class，被AOP过的真是类等工具
 */
@SuppressWarnings("rawtypes")
public class Reflections {

    private static final String SETTER_PREFIX = "set";

    private static final String GETTER_PREFIX = "get";

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static Logger logger = LoggerFactory.getLogger(Reflections.class);



    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处
     * 如无法找到, 返回Object.class.
     * eg.
     * public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenricType(final Class clazz){
        return getClassGenricType(clazz,0);
    }


    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     * 如public UserDao extends HibernateDao<User,Long>
     * @param clazz
     * @param index
     * @return
     */
    public static Class getClassGenricType(final Class clazz,final int index){

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)){
            logger.warn(clazz.getSimpleName()+",superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();

        if(index >= params.length || index < 0){
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception ex){
        if (ex instanceof IllegalAccessException || ex instanceof IllegalArgumentException
                || ex instanceof NoSuchMethodException){
            return new IllegalArgumentException(ex);
        }else if(ex instanceof InvocationTargetException){
            return new RuntimeException(((InvocationTargetException)ex).getTargetException());
        }else if(ex instanceof RuntimeException){
            return (RuntimeException)ex;
        }
        return new RuntimeException("Unexpected Checked Exception ...",ex);
    }

}
