package moskaa.collectlog;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import moskaa.mongodao.MongoInit;

public class CollectStart {

	
	public static void main(String[] args) {
		
		PropertyConfigurator.configure("config/log4j.properties");
		Configuration config = null;
		try {
			config = new XMLConfiguration("config/config.xml");
		} catch (ConfigurationException e) {
			CollectLog.LOG.debug("读取config/config.xml出错->" + e.getMessage());
		}
		
		//授权验证
		String mongoURL = config.getString("mongoURL");
		String accessKey = config.getString("accessKey");
		if (!authorize(mongoURL, accessKey)) {
			CollectLog.LOG.debug("服务因授权失败退出！");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		//创建KaaClient
		CollectLog collectLog = new CollectLog();
		
		String recordsPath = config.getString("recordsPath");
		String logPath = config.getString("logPath");
		String timer = config.getString("timer");
		
		//开启计划，每timer分钟自动执行一次采集
		Plan plan = new Plan(timer, recordsPath, logPath);
		
		//在目录下寻找各物料号，对各物料目录下的日志进行采集
		//ScanAllMaterials scanAllMaterials = new ScanAllMaterials(recordsPath, logPath);
		
		//CollectLog.kaaClient.stop();
		CollectLog.LOG.debug("日志采集服务已成功启动，请勿关闭该窗口...");
	}
	
	/**
	 * 连接数据库,进行授权验证
	 * @param mongoURL	mongo连接串
	 * @param accessKey	授权验证所需的密钥
	 * @return
	 */
	public static Boolean authorize(String mongoURL, String accessKey) {
		MongoClient client = null;
		try {
			String databaseName = "kaa";
			MongoInit mongoInit = new MongoInit();
			client = mongoInit.getMongoClient(mongoURL, databaseName);
			MongoDatabase dataBase = client.getDatabase(databaseName);
			MongoCollection<Document> collection = dataBase.getCollection("company");
			
			FindIterable<Document> findIterable = collection.find();  
			MongoCursor<Document> mongoCursor = findIterable.iterator();
			List<String> accessKeys = new LinkedList<>();
			while(mongoCursor.hasNext()){  
				Document document = mongoCursor.next();
				accessKeys.add((String)document.get("accessKey"));
				//System.out.println(document.get("accessKey"));  
			}
			
			if (accessKeys.contains(accessKey)) {
				CollectLog.LOG.debug("授权成功！");
				return true;
			} else {
				throw new Exception("accessKey未经授权，请仔细核对配置文件中的accessKey值！");
			}
			
        } catch (Exception e) {  
        	CollectLog.LOG.debug("授权过程失败->" + e.getMessage());
        	return false;
        } finally {
        	client.close();
		}
	}

}
