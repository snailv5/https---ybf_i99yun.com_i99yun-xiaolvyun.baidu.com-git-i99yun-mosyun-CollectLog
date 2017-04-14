package moskaa.collectlog;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class PlanJob implements Job{
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			String instName = context.getJobDetail().getName();
			String instGroup = context.getJobDetail().getGroup();
			String recordsPath = (String) context.getJobDetail().getJobDataMap().get("recordsPath");
			String logPath = (String) context.getJobDetail().getJobDataMap().get("logPath");
			
			CollectLog.LOG.debug(" Instance:" + instName + " instGroup:" + instGroup + " recordsPath:" + recordsPath + " recordsPath:" + recordsPath);
			//在目录下寻找各物料号，对各物料目录下的日志进行采集
			ScanAllMaterials scanAllMaterials = new ScanAllMaterials(recordsPath, logPath);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
