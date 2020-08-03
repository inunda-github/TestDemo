package com.quartz.demo.config;

import com.quartz.demo.constant.CronSet;
import com.quartz.demo.task.MyJobTask;
import com.quartz.demo.task.SpringQuartzJobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Objects;

@Configuration
public class QuartzConfig {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SpringQuartzJobFactory springQuartzJobFactory;

    /**
     *  配置任务
     * @param
     * @return
     */
    @Bean
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        //关联我们自己的Job类
        factory.setJobClass(MyJobTask.class);
        return factory;
    }

    /**
     *  触发器
     * @param
     * @return
     */
    @Bean
    public CronTriggerFactoryBean cronTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean) {
        CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
        factory.setJobDetail(Objects.requireNonNull(jobDetailFactoryBean.getObject()));
        factory.setCronExpression(CronSet.CRON);
        return factory;
    }

    /**
     * 调度工厂
     * @param jobTrigger 触发器
     * @return
     */
    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactory(CronTriggerFactoryBean jobTrigger) {

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        factoryBean.setOverwriteExistingJobs(true);

        // 延时启动，应用启动3秒后
        factoryBean.setStartupDelay(3);

        // 注册触发器
        factoryBean.setTriggers(jobTrigger.getObject());

        // Spring依赖注入为null
        springQuartzJobFactory.setApplicationContext(applicationContext);
        factoryBean.setJobFactory(springQuartzJobFactory);
        return factoryBean;
    }

}
