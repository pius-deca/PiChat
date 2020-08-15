package com.github.pius.pichats;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import com.github.pius.pichats.service.jobshedular.Task;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PichatsApplication {

  public static void main(String[] args) {
    SpringApplication.run(PichatsApplication.class, args);

    // try {
    // JobDetail job =
    // JobBuilder.newJob().ofType(Task.class).storeDurably().withIdentity("taskJob")
    // .withDescription("Invoke task Job service...").build();

    // Trigger trigger = TriggerBuilder.newTrigger()
    // .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(30).repeatForever()).build();

    // // schedule the job
    // SchedulerFactory schFactory = new StdSchedulerFactory();
    // Scheduler sch = schFactory.getScheduler();
    // sch.start();
    // sch.scheduleJob(job, trigger);
    // } catch (SchedulerException e) {
    // e.printStackTrace();
    // }
  }

  @PostConstruct
  public void started() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

}
