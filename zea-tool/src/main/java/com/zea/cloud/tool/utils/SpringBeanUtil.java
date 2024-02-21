package com.zea.cloud.tool.utils;

import org.springframework.beans.BeansException;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;

/**
 * SpringBeanUtil
 * @author jfzou
 * @since 2024/1/4
 */
@Component
public final class SpringBeanUtil implements ApplicationContextAware {
    /**
     * 应用上下文
     */
    private static ApplicationContext context;

    /**
     * 设置应用上下文
     * @param applicationContext  应用上下文
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 获取bean
     *
     * @param beanName bean名称
     * @param clazz    bean的class对象
     * @return         bean对象
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        if (context == null) {
            return null;
        }
        return context.getBean(beanName, clazz);
    }

    /**
     * 获取bean
     *
     * @param beanName  bean名称
     * @return          bean对象
     */
    public static Object getBean(String beanName) {
        if (context == null) {
            return null;
        }
        return context.getBean(beanName);
    }

    /**
     * 获取Bean
     *
     * @param clazz bean的class对象
     * @return      bean对象
     */
    public static <T> T getBean(Class<T> clazz) {
        if (context == null) {
            return null;
        }
        return context.getBean(clazz);
    }

    /**
     * 浅拷贝对象
     *
     * @param source  源对象
     * @param target  需要赋值的对象
     */
    public static void copy(Object source, Object target) {
        BeanCopier copier = BeanCopier
                .create(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }

    /**
     * 获取配置文件配置项的值
     * @param key 配置项key
     */
    public static String getEnvironmentProperty(String key) {
        return context.getEnvironment().getProperty(key);
    }

    /**
     * 获取spring.profiles.active的环境配置：即dev,test,prod
     */
    public static String getActiveProfile() {
        // 以启动参数spring.profiles.active为准
        String property = System.getProperty("spring.profiles.active");
        if (property != null) {
            String[] split = property.split(",");
            for (String s : split) {
                if ("dev".equals(s) || "test".equals(s) || "prod".equals(s)) {
                    return s;
                }
            }
        }

        // 如果没有启动参数，以配置文件中spring.profiles.active为准
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        for (String activeProfile : activeProfiles) {
            if ("dev".equals(activeProfile) || "test".equals(activeProfile) || "prod".equals(activeProfile)) {
                return activeProfile;
            }
        }
        return null;
    }

}
