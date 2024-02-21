package com.zea.cloud.common.lock4j;

import com.baomidou.lock.LockFailureStrategy;
import com.zea.cloud.common.exception.ErrorCode;
import com.zea.cloud.common.exception.MyLockException;
import com.zea.cloud.common.utils.LocaleCodeUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 获取不到锁给出的提示
 */
@Component
public class MyLockFailureStrategy implements LockFailureStrategy {
    @Override
    public void onLockFailure(String key, Method method, Object[] arguments) {
        throw new MyLockException(ErrorCode.LOCK_ERROR, LocaleCodeUtil.getCode("lock.obtain.error"));
    }
}
