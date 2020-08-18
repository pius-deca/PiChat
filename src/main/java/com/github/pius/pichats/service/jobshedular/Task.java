package com.github.pius.pichats.service.jobshedular;

import com.github.pius.pichats.service.implementation.NotificationQueueServiceImpl;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Task implements Job {
    private Logger log = LoggerFactory.getLogger(Task.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info(new NotificationQueueServiceImpl().receiveTromQueue());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}