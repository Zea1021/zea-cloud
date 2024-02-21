package com.zea.cloud.common.utils;

import com.zea.cloud.tool.utils.SpringBeanUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;


/**
 * 本地化工具
 */
public class LocaleCodeUtil {

    /**
     *  根据code获取本地化字符串
     * @param code  字符串编码
     * @return      本地化字符串
     */
    public static String getCode(String code) {
        return getCode(code, (Object) null);
    }

    /**
     *  根据code获取本地化字符串, args 参数替换本地字符串中的占位符例如{0},{1}
     * @param code  字符串编码
     * @param args  参数
     * @return      本地化字符串
     */
    public static String getCode(String code, Object... args) {
        MessageSource messageSource = SpringBeanUtil.getBean(MessageSource.class);
        if (messageSource != null) {
            return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
        }
        return null;
    }

}
