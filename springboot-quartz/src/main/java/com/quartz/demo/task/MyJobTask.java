package com.quartz.demo.task;

import com.quartz.demo.service.MyTestService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @类 名 : MyJobTask
 * @功能描述 : 
 * @作 者 : inunda
 * @创建时间: 2020-08-03 15:40
 */
@Service
public class MyJobTask implements Job {
    @Autowired
    private MyTestService myTestService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            myTestService.myTest();
        } catch (Exception e) {
            JobExecutionException e2 = new JobExecutionException(e);
            //休眠10秒
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            //如果任务启动失败，休眠10秒后重启任务
            e2.setRefireImmediately(true);
        }
    }
}
