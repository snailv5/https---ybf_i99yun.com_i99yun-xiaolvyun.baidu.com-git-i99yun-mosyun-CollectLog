package moskaa.collectlog;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.kaaproject.kaa.schema.collectlog.CollectLogFile;

import com.mongodb.BasicDBObject;

import moskaa.entity.Company;
import moskaa.entity.CompanyDao;
import moskaa.file.AppendToFile;
import moskaa.file.FileViewer;
import moskaa.file.ReadLog;

public class Statistics {
	
	private String filePath = "";	// 如：/Users/befu/work/other/宇之光飞针测试日志/4v2fq00h-1/logs
	private String recordsPath = "";
	public DecimalFormat decimalFormat = new DecimalFormat(".#");
	/**
	 * 传入扫描到的所有文件列表，分析上传给kaa平台
	 * 1.pcs测试ok和ng数
	 * 2.sets测试ok和ng数
	 * 3.测试人员名称
	 * 4.开始测试时间
	 * 5.测试总耗时
	 * @param lstFileNames	文件列表
	 */
	public void collectTestData(List<String> lstFileNames) {
		
		String materials = "";		//物料号
		
		//读日志文件目录下的cache文件，获取已经上传的日志文件列表
		List<String> collectFileList = new LinkedList<String>();
		try {
			ReadLog readLog = new ReadLog();
			readLog.readTxtFile(filePath + "/cache");
			for (int i = 0; i < readLog.txtlines.size(); i++) {
				collectFileList.add(readLog.txtlines.get(i));
				//CollectLog.LOG.debug(readLog.txtlines.get(i) + "\r\n");
			}
		} catch (Exception e) {
			CollectLog.LOG.debug("该物料号第一次开始统计，可忽略该错误->" + e.getMessage());
		}
		
		try {
			String dir[] = filePath.split("/");
			materials = dir[dir.length - 2];
		} catch (Exception e) {
			CollectLog.LOG.debug("获取物料号时出错->" + e.getMessage());
			return;
		}
		
		Company company = null;

		Configuration config;
		try {
			config = new XMLConfiguration("config/config.xml");
			String accessKey = config.getString("accessKey");
			String mongoURL = config.getString("mongoURL");
			CompanyDao companyDao = new CompanyDao(mongoURL);
			
			BasicDBObject queryObject = new BasicDBObject().append("accessKey", accessKey);
			company = companyDao.find(queryObject);
		} catch (ConfigurationException e) {
			CollectLog.LOG.debug("获取物料号时出错->" + e.getMessage());
		} catch (Exception e) {
			CollectLog.LOG.debug("连接数据库时出错->" + e.getMessage());
		}
		
		for (String fileName : lstFileNames) {
			
			//如果fileName已在cache文件里则跳过
			if (collectFileList.contains(fileName)) {
				continue;
			} else {
				int machine = 0;			//机台号
				int pcsNum = 0;				//测试单元，如 1-0002.oky，则取2
				int testNum = 0;			//测试号
				int testStatus = 0;			//测试状态，1、oky；2、err；3、abr
				String testUser = "";		//操作员
				Long beginTime = 0L;		//开始时间
				double usedTime = 0.0;		//该次测试所用时间
				int points = 0;				//点数
				int nets = 0;				//网络数
				int closeNets = 0;			//相邻网络数
				String testType = "";		//测试方法：电阻，电容，低阻，高压
				//“物料号+测试号”到日志信息的映射关系
				HashMap<String, String> materials2RecordsLog = new HashMap<>();
				
				try {
					String str[] = fileName.split("-");
					testNum = Integer.parseInt(str[0]);
					
					String string = str[1];
					String strUnit[] = string.split("\\.");
					pcsNum = Integer.parseInt(strUnit[0]);
					
					if (strUnit[1].indexOf("oky") >= 0) {
						testStatus = 1;
					}else if (strUnit[1].indexOf("err") >= 0) {
						testStatus = 2;
					} else {
						testStatus = 3;
					}
					
					testUser = getValueFileName("User", fileName);
					
					String dateStr = getValueFileName("BeginTime", fileName);
			        Date date = new Date();   
			        //注意format的格式要与日期String的格式相匹配   
			        DateFormat sdf = new SimpleDateFormat(" yyyy/MM/dd  HH:mm:ss");   
			        date = sdf.parse(dateStr);   
		            beginTime = date.getTime();
					
					usedTime = timeTominutes(getValueFileName("UsedTime", fileName));
					
					//如果从日志文件中并未正确获取到开始时间或耗费时间，则跳过不保存
					if (beginTime < 1 || usedTime < 0.0001) {
						continue;
					}
					
				} catch (Exception e) {
					CollectLog.LOG.debug("统计物料号[" + materials + "]的[" + fileName + "]信息时出错->" + e.getMessage());
					continue;
				}
				
				try {
					//把日志文件信息保存入库
					List<CollectLogFile> docs = new LinkedList<CollectLogFile>();
					
					int companyId = company.get_id();
					
					//获取机台号、点数、网络数、相邻网络数、测试方法
					getRecordsLogMap(materials2RecordsLog, recordsPath, "");
					String recodsLog = materials2RecordsLog.get(materials + testNum);
					if (recodsLog != null) {
						try {
							String recods[] = recodsLog.split(",");
							machine = Integer.parseInt(recods[2]);
							points = Integer.parseInt(recods[3]);
							nets = Integer.parseInt(recods[4]);
							closeNets = Integer.parseInt(recods[5]);
							testType = recods[8];
						} catch (Exception e) {
							CollectLog.LOG.debug("在获取机台号、点数、网络数、相邻网络数测试方法时出错，可能影响数据的完整性！");
						}
					}
					
			    	docs.add(new CollectLogFile(companyId, machine, materials, testNum, pcsNum, 
			    			testStatus, testUser, beginTime, usedTime, points, nets, closeNets, testType));
			        
			        CollectLog.saveDocs(docs);
					
			        CollectLog.LOG.debug("[" + machine + "," + materials + "," + testNum + "," + pcsNum + "," + 
							testStatus + "," + testUser + "," + beginTime + "," + usedTime + "]准备上传入库！");
				} catch (Exception e) {
					CollectLog.LOG.debug("物料号[" + materials + "]的[" + fileName + "]准备上传入库时出错->" + e.getMessage());
					return;
				}
				
				try {
					//把已经保存入库的日志文件名记录在cache文件中
					AppendToFile appendToFile = new AppendToFile(filePath + "/cache", fileName + "\r\n");
					CollectLog.LOG.debug("物料号[" + materials + "]进行本地缓存[" + fileName + "]成功！");
				} catch (Exception e) {
					CollectLog.LOG.debug("物料号[" + materials + "]进行本地缓存[" + fileName + "]出错->" + e.getMessage());
				}
			}
		}
	}
	
	/**
	 * 通过传入的关键词，获取对应的值
	 * @param key		要获取的关键值
	 * @param fileName	文件名
	 * @return
	 */
	public String getValueFileName(String key, String fileName) throws Exception{
		
		ReadLog readLog = new ReadLog();
		readLog.readTxtFile(getFilePath() + "/" + fileName);
		for (int i = 0; i < readLog.txtlines.size(); i++) {
			String txtline = readLog.txtlines.get(i);
			if (txtline.indexOf(key) >= 0) {
				String result = txtline.substring(txtline.indexOf(":") + 1, txtline.length());
				return result;
			}
		}
		return "";
	}
	
	/**
	 * 获取物料号到记录日志信息的映射表
	 * @param path
	 * @param fileName	保存记录日志的文件，如果为""，则表示取当前日期组成文件名
	 * @return
	 * @throws Exception
	 */
	public void getRecordsLogMap(HashMap<String, String> materials2RecordsLog, String path, String fileName) throws Exception{
		try {
			ReadLog readLog = new ReadLog();
			String recordFile = "";
			if (fileName == "") {
				//根据日期构造一个标准日志文件名，表示采集当天的数据，如“2016-09-30.txt”
				Date dt=new Date();
			    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			    recordFile = sdf.format(dt) + ".txt";
			} else {
				recordFile = fileName;
			}
			
			readLog.readTxtFile(path + "/" + recordFile);
			CollectLog.LOG.debug("读取的日志文件为[" + path + "/" + recordFile + "]");
			for (int i = 0; i < readLog.txtlines.size(); i++) {
				String txtline = readLog.txtlines.get(i);
				
				//日志样例：04004S9CAA0,16:11:24,15,292,84,450,3,180,电容,00:39:10,179,1,0,1,0
				//取物料号和测试号共同组成map唯一键值,如下：
				//04004S9CAA03=04004S9CAA0,16:11:24,15,292,84,450,3,180,电容,00:39:10,179,1,0,1,0
				String txts[] = txtline.split(",");
				materials2RecordsLog.put(txts[0] + txts[6], txtline);
			}
		} catch (Exception e) {
			String file = getLatestRecordsFile(path);
			if (file == "") {
				throw new Exception("统计TestRecords文件时出错->" + e.getMessage());
			}
			getRecordsLogMap(materials2RecordsLog, path, file);
		}
	}
	
	/**
	 * 
	 * @param time 一个用字符串表示的时间，把h,m,s全统一用分钟m来表示
	 * @return
	 */
	public double timeTominutes(String time) {
		double m = 0.0;
		
		String t[] = time.split(":");
		double h = Double.parseDouble(t[0]);
		m = Double.parseDouble(t[1]);
		double s = Double.parseDouble(t[2]);
		m += h * 60 + s / 60.0; 
		
		return Double.parseDouble(decimalFormat.format(m));
	}
	
	/**
	 * 获取最新的记录日志文件名
	 * @return
	 */
	public String getLatestRecordsFile(String path) {
		String recordsFile = "";
		
		FileViewer fileViewer = new FileViewer();
		List<String> fiList = new LinkedList<>();
		fiList = fileViewer.getListFiles(path, "", false);
		for (int i = 0; i < fiList.size(); i++) {
			String fileName = fiList.get(i);
			//如：2016-08-23.txt，筛选标准的记录日志文件，
			if (fileName.length() != 14 || fileName.indexOf("txt") < 0) {
				fiList.remove(i);
				continue;
			}
		}
		//倒序排，把最新的日志放前面
		Collections.reverse(fiList);
		recordsFile = fiList.get(0);
		
		return recordsFile;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getRecordsPath() {
		return recordsPath;
	}

	public void setRecordsPath(String recordsPath) {
		this.recordsPath = recordsPath;
	}
}
