package com.zea.cloud.basic.util;

import org.springframework.beans.BeansException;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanUtil implements ApplicationContextAware {
    /**
     * ApplicationContext
     */
    private static ApplicationContext context;

    /**
     * setApplicationContext
     *
     * @param applicationContext ApplicationContext
     * @throws BeansException BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 获取bean
     *
     * @param bean bean String
     * @return bean对象
     */
    public static <T> T getBean(String bean, Class<T> clazz) {
        if (context == null) {
            return null;
        }
        return context.getBean(bean, clazz);
    }

    /**
     * 获取bean
     *
     * @param objectName
     * @return
     */
    public static Object getBean(String objectName) {
        if (context == null) {
            return null;
        }
        return context.getBean(objectName);
    }

    /**
     * 获取Bean
     *
     * @param clazz bean String
     * @return bean对象
     */
    public static <T> T getBean(Class<T> clazz) {
        if (context == null) {
            return null;
        }
        return context.getBean(clazz);
    }

    /**
     * 拷贝对象
     *
     * @param src  源对象
     * @param dist 需要赋值的对象
     */
    public static void copy(Object src, Object dist) {
        BeanCopier copier = BeanCopier
                .create(src.getClass(), dist.getClass(), false);

        copier.copy(src, dist, null);
    }
}
