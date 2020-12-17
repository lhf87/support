package com.lhf.common.spring;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.lang.annotation.Annotation;

/**
 * AnnotationAdvisingBeanPostProcessor
 *
 * @author lhf
 * @date 2020/12/16
 */
@SuppressWarnings("unchecked")
public class AnnotationAdvisingBeanPostProcessor extends AbstractAdvisingBeanPostProcessor {

    public AnnotationAdvisingBeanPostProcessor(String annotationName, MethodInterceptor methodInterceptor, Integer order) {
        Class<? extends Annotation> annotation = null;
        try {
            annotation = (Class<? extends Annotation>) Class.forName(annotationName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        final Pointcut pointcut = new AnnotationMatchingPointcut(annotation, true);

        advisor = new AbstractPointcutAdvisor() {

            @Override
            public Pointcut getPointcut() {
                return pointcut;
            }

            @Override
            public Advice getAdvice() {
                return methodInterceptor;
            }
        };

        if (order != null) {
            this.setOrder(order);
            ((AbstractPointcutAdvisor) advisor).setOrder(order);
        }
    }
}
