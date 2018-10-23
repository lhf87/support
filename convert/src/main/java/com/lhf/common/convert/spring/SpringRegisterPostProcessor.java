package com.lhf.common.convert.spring;

import com.lhf.common.convert.register.RegisterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * spring扫包注册
 *
 * @author lhf
 * @date 2018/7/12
 */

@Component
public class SpringRegisterPostProcessor {

    @Autowired
    private ApplicationContext context;

    /**
     * 避免重复扫包导致的多次执行
     */
    private static final AtomicBoolean IS_EXECUTED = new AtomicBoolean(false);

    @PostConstruct
    public void initComponent() {
        if (IS_EXECUTED.compareAndSet(false, true)) {
            Map<String, SpringRegister> beans = context.getBeansOfType(SpringRegister.class);
            beans.forEach((key, value) -> RegisterFactory.springRegister(value));
        }
    }
}
