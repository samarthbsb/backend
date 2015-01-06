package com.sv.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by samarth on 06/01/15.
 */
@Component
public class SVBeanProcessors implements BeanPostProcessor {

    @Override public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("Before Init BeanName :  "+ beanName);
        return bean;
    }

    @Override public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("After init BeanName :  "+ beanName);
        return bean;
    }
}
