package moskaa.collectlog;

import java.awt.Container;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import moskaa.entity.Company;
import moskaa.entity.CompanyDao;
import moskaa.file.FileViewer;
import moskaa.mongodao.MD5AndKL;
import moskaa.mongodao.MongoInit;


public class TestMain {

	
	public static void main(String[] args) throws Exception {
		
		PropertyConfigurator.configure("config/log4j.properties");
		Configuration config = new XMLConfiguration("config/config.xml");
		
		String p1 = config.getString("recordsPath");
		String p2 = config.getString("testLogPath");
		String mongoURL = config.getString("mongoURL");
		
		CollectLog.LOG.debug(p1 + "," + p2 + "," + mongoURL);
		
		//测试数据库
//		String databaseName = "kaa";
//		MongoInit mongoInit = new MongoInit();
//		MongoClient client = mongoInit.getMongoClient(mongoURL, databaseName);
//		MongoDatabase dataBase = client.getDatabase(databaseName);
//		MongoCollection<Document> collection = dataBase.getCollection("company");
//		
//		FindIterable<Document> findIterable = collection.find();  
//		MongoCursor<Document> mongoCursor = findIterable.iterator();
//		List<String> accessKeys = new LinkedList<>();
//		while(mongoCursor.hasNext()){  
//			Document document = mongoCursor.next();
//			accessKeys.add((String)document.get("accessKey"));
//			System.out.println(document.get("accessKey"));  
//		}
//		
//		MongoCredential credential0 = MongoCredential.createCredential("mosyun", "kaa", "mongo141421".toCharArray());
//		MongoCredential credential1 = MongoCredential.createCredential("mosyun", "yuzhiguang", "mongo141421".toCharArray()); 
//		ServerAddress serverAddress = new ServerAddress("112.124.110.91", 10023); 
//		List<MongoCredential> credentials = new LinkedList<>();
//		credentials.add(credential0);
//		credentials.add(credential1);
//		MongoClient mongoClient = new MongoClient(serverAddress, credentials); 
//		DB db = mongoClient.getDB("yuzhiguang");  
//	   
//        DBCollection table = db.getCollection("user");  
//   
//        BasicDBObject document = new BasicDBObject();  
//        document.put("name", "mkyong");  
//        table.insert(document);  
//   
//        System.out.println("Login is successful!");  
		
		
  
//		MongoCredential.createCredential("mosyun", "myMongo", "mongo141421".toCharArray());
//		MongoDatabase database2 = client.getDatabase("myMongo");
//		database2.createCollection("user");
//		MongoCollection<Document> collection2 = database2.getCollection("user");
//		Document d = new Document("test", "11111");
//		collection2.insertOne(d);
//		
//		System.out.print(database2.getName());
		
		
		
		HashMap<String, String> materials2recordLog = new HashMap<String, String>();
		
		String dateStr = " 2015/05/29  04:18:07";   
        Date date = new Date();   
        
        //注意format的格式要与日期String的格式相匹配   
        DateFormat sdf = new SimpleDateFormat(" yyyy/MM/dd  HH:mm:ss");   
        
        
        try {   
            date = sdf.parse(dateStr);   
            System.out.println(date.toString());
            long t = date.getTime();
            System.out.println(1476564651000L);
            
            
            Timestamp ts = new Timestamp(1476564651000L);
            Date d = new Date();
            d = ts;
            DateFormat sdff = new SimpleDateFormat(" yyyy-MM-dd  HH:mm:ss");
            TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
            sdff.setTimeZone(timeZone);
            
            String beginTime = sdff.format(d);
    	    
            //String beginTime = ts.toString().substring(0, 19);
            System.out.println(beginTime);
            beginTime = beginTime.replace("-", "").replace(" ", "").replace(":", "");
            System.out.println(beginTime);
            Integer company_id = 1001;
            Integer machine = 15;
            String id = company_id.toString() + format4Bits(machine.toString()) + beginTime;
            
            System.out.println("_id=" + id);
        } catch (Exception e) {   
            e.printStackTrace();   
        }  
		
//		Timestamp ts = new Timestamp(System.currentTimeMillis());   
//        String tsStr = " 2015-05-29 04:01:15";   
//        try {   
//            ts = Timestamp.valueOf(tsStr);   
//            System.out.println(Long)ts);   
//        } catch (Exception e) {   
//            e.printStackTrace();   
//        }  
		
		//Timestamp ts = new Timestamp(System.currentTimeMillis());   
        //String tsStr = "";   
        //DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");   
//        try {   
//            //方法一   
//            //tsStr = sdf.format(ts);   
//            //System.out.println(tsStr);   
//            //方法二   
//            tsStr = ts.toString();   
//            System.out.println(tsStr);   
//        } catch (Exception e) {   
//            e.printStackTrace();   
//        }  
		
//		AppendToFile appendToFile = new AppendToFile("/Users/befu/work/other/宇之光飞针测试日志/4v2fq00h-1/logs/cache", "天天向上22\r\n");
		
//		ReadLog readLog = new ReadLog();
//		readLog.readTxtFile("/Users/befu/work/other/宇之光飞针测试日志/4v2fq00h-1/logs/cache");
//		for (int i = 0; i < readLog.txtlines.size(); i++) {
//			CollectLog.LOG.debug(readLog.txtlines.get(i) + "\r\n");
//		}

//		log.info(p1);
//		log.info(p2);
//		FileViewer fileViewer = new FileViewer();
//		List<String> fiList = new LinkedList<>();
//		fiList = fileViewer.getListFiles(p1, "", false);
//		for (int i = 0; i < fiList.size(); i++) {
//			String fileName = fiList.get(i);
//			if (fileName.length() != 14 || fileName.indexOf("txt") < 0) {
//				fiList.remove(i);
//				continue;
//			}
//			CollectLog.LOG.info(fileName);
//		}
//		Collections.sort(fiList);
//		CollectLog.LOG.info(fiList.toString());
//		
//		Collections.reverse(fiList);
//		CollectLog.LOG.info(fiList.toString());
		
//		Statistics s = new Statistics();
//		double t = s.timeTominutes("00:03:16");
//		System.out.println(t);
		
		
//		ScanAllMaterials scanAllMaterials = new ScanAllMaterials(p1, p2);
		
//		ReadLog readLog = new ReadLog();
//		Date dt=new Date();
//	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//	    String recordFile = sdf.format(dt) + ".txt";
//		readLog.readTxtFile(p1 + "/" + recordFile);
//		for (int i = 0; i < readLog.txtlines.size(); i++) {
//			String txtline = readLog.txtlines.get(i);
//			
//			//日志样例：04004S9CAA0,16:11:24,15,292,84,450,3,180,电容,00:39:10,179,1,0,1,0
//			//取物料号和测试号共同组成map唯一键值
//			String txts[] = txtline.split(",");
//			materials2recordLog.put(txts[0] + txts[6], txtline);
//		}
//		System.out.println(materials2recordLog.get("04004S9CAA0333") + "\r\n");
//		System.out.print(materials2recordLog);
		
		
		//连接数据库 1
//		MongoClient client = null;
//		try {
//			String databaseName = "authorize";
//			MongoInit mongoInit = new MongoInit();
//			client = mongoInit.getMongoClient(mongoURL, databaseName);
//			MongoDatabase database = client.getDatabase(databaseName);
//			MongoCollection<Document> collection = database.getCollection("company");
//			BasicDBObject queryObject = new BasicDBObject().append("name", "宇之光");
//			
//			FindIterable<Document> findIterable = collection.find(queryObject);  
//			MongoCursor<Document> mongoCursor = findIterable.iterator();
//			List<String> accessKeys = new LinkedList<>();
//			while(mongoCursor.hasNext()){
//				Document document = mongoCursor.next();
//				Company company = new Company();
//				company.parse(document);
//				System.out.println(company);
//				accessKeys.add(company.getAccessKey());
//				System.out.println(company.getAccessKey());  
//			}
//			
//			if (accessKeys.contains("bo4djkf9t85rqfdjcy")) {
//				System.out.println("验证通过");
//			}
//			
//        } catch (Exception e) {  
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );  
//        } finally {
//			client.close();
//		}
		
		//连接数据库 2
//		MongoClient client = null;
//		try {
//			String databaseName = "authorize";
//			MongoInit mongoInit = new MongoInit();
//			client = mongoInit.getMongoClient(mongoURL, databaseName);
//			@SuppressWarnings("deprecation")
//			DB db = client.getDB(databaseName);
//			DBCollection collection = db.getCollection("company");
//			BasicDBObject queryObject = new BasicDBObject().append("name", "宇之光");
//			
//			DBCursor find = collection.find(queryObject);  
//			List<String> accessKeys = new LinkedList<>();
//			while(find.hasNext()){
//				Company company = new Company();
//				company.parse(find.next());
//				System.out.println(company);
//			}
//			
//			if (accessKeys.contains("bo4djkf9t85rqfdjcy")) {
//				System.out.println("验证通过");
//			}
//			
//        } catch (Exception e) {  
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );  
//        } finally {
//			client.close();
//		}
		
		
//		CompanyDao companyDao = new CompanyDao(mongoURL);
//		
//		List<Company> companys = companyDao.findAll();
//		System.out.println(companys);
//		
//		BasicDBObject queryObject = new BasicDBObject().append("name", "宇之光");
//		Company company = companyDao.find(queryObject);
//		System.out.println(company);
	}
	
	
	/**
     * 把一个不对齐的数字转换为4位对齐的字符串
     * @param s
     * @return
     */
    public static String format4Bits(String s) {
    	if (s.length() < 2) {
			s = "000" + s;
		} else if (s.length() < 3) {
			s = "00" + s;
		} else if (s.length() < 4) {
			s = "0" + s;
		}
    	
    	return s;
    }
    
    public static String getFormatedDateString(String _timeZone){
		TimeZone timeZone = null;
		if(StringUtils.isEmpty(_timeZone)){
			timeZone = TimeZone.getDefault();
		}else{
			timeZone = TimeZone.getTimeZone(_timeZone);
		}
		
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒");
	    sdf.setTimeZone(timeZone);
	    //TimeZone.setDefault(timeZone);
	    return sdf.format(new Date());
   }

}
