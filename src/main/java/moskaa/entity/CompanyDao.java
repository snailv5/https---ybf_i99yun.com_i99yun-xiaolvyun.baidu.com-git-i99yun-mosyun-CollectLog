package moskaa.entity;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.conn.ssl.PrivateKeyStrategy;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.DBObject;

import moskaa.mongodao.MongoInit;

public class CompanyDao {
	
	private String mongoURL = "";		//数据库连接串
	
	public CompanyDao(String mongoURL) {
		this.mongoURL = mongoURL;
	}
	
	/** 
     * 查询所有公司列表
     * @return 
     * @throws UnknownHostException  
     */  
    public List<Company> findAll() throws Exception{  
    	String databaseName = "kaa";
		MongoInit mongoInit = new MongoInit();
		MongoClient client = mongoInit.getMongoClient(mongoURL, databaseName);
		MongoDatabase database = client.getDatabase(databaseName);
		MongoCollection<Document> collection = database.getCollection("company");
		
		FindIterable<Document> findIterable = collection.find();  
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		List<Company> companies = new LinkedList<>();
		while(mongoCursor.hasNext()){
			Document document = mongoCursor.next();
			Company company = new Company();
			company.parse(document);
			companies.add(company);
		}
		client.close();
        return companies;  
    }
    
    /** 
     * 查询所有公司列表
     * @return 
     * @throws UnknownHostException  
     */  
    public Company find(BasicDBObject queryObject) throws Exception{  
    	String databaseName = "kaa";
		MongoInit mongoInit = new MongoInit();
		MongoClient client = mongoInit.getMongoClient(mongoURL, databaseName);
		MongoDatabase database = client.getDatabase(databaseName);
		MongoCollection<Document> collection = database.getCollection("company");
		
		FindIterable<Document> findIterable = collection.find(queryObject);  
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		Company company = new Company();
		while(mongoCursor.hasNext()){
			company.parse(mongoCursor.next());
		}
		client.close();
        return company;  
    }
}
