package moskaa.entity;

import org.bson.Document;

/**
 * 公司信息
 * @author befu
 *
 */
public class Company {
	private int _id;			//公司id
	private String name;		//公司名称
	private String accessKey;	//公司密钥
	
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	
	public void parse(Document doc){  
        this.set_id((Integer)doc.get("_id")); 
        this.setAccessKey((String)doc.get("accessKey"));
        this.setName((String)doc.get("name"));  
    }  
      
      
    public String toString(){  
        return "_id:"+_id+",name:"+name+",accessKey:"+accessKey;  
    }
}
