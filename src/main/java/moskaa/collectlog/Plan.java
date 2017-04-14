package moskaa.collectlog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Plan {

	static ArrayList<Map<String, ArrayList<JobDetail>>> jobAndTriggerMapList = new ArrayList<Map<String, ArrayList<JobDetail>>>();
	static ArrayList<Map<String, Integer>> unitTimeOutList = new ArrayList<Map<String,Integer>>();
	static SchedulerFactory schedFact = new StdSchedulerFactory();
	static Scheduler sched_collect;		//自动采集计划任务
	static Scheduler sched;				//强制更新超时的采集任务为失败状态，心跳测试
	public static String topNameList = "";
	public static String serverList = "";
	
	/**
	 * 传入分钟数
	 * @param m
	 * @param recordsPath
	 * @param logPath
	 */
	public Plan(String m, String recordsPath, String logPath) {
		
		try {
			sched = schedFact.getScheduler();
			sched.start();
    	    //经过每m分钟采集一次
			String express = " 0 0/M * * * ? ";
			express = express.replace("M", m);
			JobDetail jobDetail = new JobDetail("collectLog", "collectLog", PlanJob.class);
			//设置任务执行时的参数
			jobDetail.getJobDataMap().put("recordsPath", recordsPath);
			jobDetail.getJobDataMap().put("logPath", logPath);
			CronTrigger trigger = new CronTrigger("collectLog", "collectLog");
			trigger.setCronExpression(express);
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			CollectLog.LOG.debug("任务计划出错->Exception,{}", e.getMessage());
		}
	}
}
