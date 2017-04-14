package moskaa.mongodao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoInit {
	
	/**
	 * 根据传的mongo连接串和数据库名，获取一个MongoClient
	 * @param mongoURL	数据库连接串
	 * @param dataBase	需要连接的数据库名
	 * @throws Exception
	 */
	public MongoClient getMongoClient(String mongoURL, String dataBase) throws Exception {
		
		mongoURL = MD5AndKL.unEncryption(mongoURL, "yuzhiguang");
		//mongoURL = "mongodb://mosyun:mongo141421@112.124.110.91:10023/?authSource=";
		mongoURL = mongoURL + dataBase;
		
		//连接数据库
		try { 
			MongoClientURI uri = new MongoClientURI(mongoURL);
			MongoClient client = new MongoClient(uri);
			return client;
			
        } catch (Exception e) {  
            throw new Exception(e.getClass().getName() + ": " + e.getMessage());  
        }
	}
}
