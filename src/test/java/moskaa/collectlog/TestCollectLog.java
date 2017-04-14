package moskaa.collectlog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import moskaa.file.ReadLog;

public class TestCollectLog {

public static void main(String[] args) {
		
		Configuration config = null;
		try {
			config = new XMLConfiguration("config/config.xml");
		} catch (ConfigurationException e) {
			e.printStackTrace();
			System.out.println("读取配置文件“config/config.xml”出错！");
		}
    	
    	
		CollectLog collectLog = new CollectLog();
		String recordsfilePath = config.getString("recordsPath");
		ReadLog readLog = new ReadLog();
		
//		readLog.readTxtFile("/Users/befu/work/other/宇之光飞针测试日志/4v2fq00h-1/logs/cache");
//		for (int i = 0; i < readLog.txtlines.size(); i++) {
//			System.out.print(readLog.txtlines.get(i) + "\r\n");
//		}
		
//		List<CollectLogFile> docs = collectLog.generateRecordsLogs(readLog.txtlines);
//		collectLog.saveDocs(docs);
		
//		String testLogfilePath = config.getString("testLogPath");
//
//		FileViewer fileViewer = new FileViewer();
//		List<String> lstFileNames = fileViewer.getListFiles(testLogfilePath, "", true);
//		Collections.sort(lstFileNames);
//		
//		Statistics s = new Statistics();
//		s.setFilePath(testLogfilePath);
//		String statis = s.analyseLog(lstFileNames);
//		statis = "4v2fq00h-1," + statis;
		
//		List<CollectLogFile> docs2 = collectLog.generateLogs(statis);
//		collectLog.saveDocs(docs2);
		
	}

}
