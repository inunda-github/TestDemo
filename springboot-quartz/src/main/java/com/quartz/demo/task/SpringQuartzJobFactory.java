package com.quartz.demo.task;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@Component("springQuartzJobFactory")
public class SpringQuartzJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(final ApplicationContext context) throws BeansException {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    /*
     * 描述:该方法需要将实例化的任务对象手动的添加到springIOC容器中并且完成对象的注入
     * @Param :[bundle]
     * @author :inunda
     * @date :2020/8/3 15:41
     * @return :java.lang.Object
     * @throws :
     **/
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        //调用父类的方法
        Object obj = super.createJobInstance(bundle);
        //将obj对象添加Spring IOC容器中，并完成注入
        beanFactory.autowireBean(obj);
        return obj;
    }
}