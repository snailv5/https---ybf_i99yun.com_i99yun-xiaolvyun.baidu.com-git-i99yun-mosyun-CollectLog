package moskaa.collectlog;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import moskaa.file.FileViewer;

/**
 * 根据配置文件提供的目录，遍历所有物料文件夹目录
 * @author befu
 *
 */
public class ScanAllMaterials {
	
	public ScanAllMaterials(String recordsLogPath, String path) {
		try {
			FileViewer fileViewer = new FileViewer();
			List<String> listFolderNames = fileViewer.getListFolder(path);
			if (listFolderNames == null) {
				return;
			} else {
				String logfilePath = "";
				for (String folderName : listFolderNames) {
					
					//日志目录
					logfilePath = path + "/" + folderName + "/logs";
					
//					//记录日志目录下已经采集并上传的文件名列表
//					File f = new File(logfilePath + "/cache");
//		            if (f.exists()) {  
//		            	CollectLog.LOG.debug("采集缓存文件已存在！");
//		            } else {  
//						f.createNewFile();
//						CollectLog.LOG.debug("已在目录[" + folderName + "/logs]下成功创建采集缓存文件！");
//		            }
					
					//搜索日志目录下的所有文件
					List<String> lstFileNames = fileViewer.getListFiles(logfilePath, "", true);
					if (fileViewer.getMaterials2flag().get(logfilePath + "/") != null) {
						boolean flag = fileViewer.getMaterials2flag().get(logfilePath + "/");
						//在目录下查找到已采集标志的“OK”文件
						if (flag) {
							CollectLog.LOG.debug("物料[" + folderName + "]已停止测试，放弃采集！");
							continue;
						}
					}
					
					//做简单排序
					Collections.sort(lstFileNames);
					
					//统计日志数据
					
					//启动KaaClient
					if (!CollectLog.kaaClientStatus) {
						CollectLog.kaaClient.start();
					}
					
					Statistics s = new Statistics();
					s.setRecordsPath(recordsLogPath);
					s.setFilePath(logfilePath);
					s.collectTestData(lstFileNames);
					
//					if (CollectLog.kaaClientStatus) {
//						CollectLog.kaaClient.stop();
//					}
					
					//folderName即为物料号
					//statis = folderName + "," + statis;
					//CollectLog.LOG.debug(statis);
				
//					File f = new File(logfilePath + "/OK");
//		            if (f.exists()) {  
//		            	CollectLog.LOG.debug("采集标志文件已存在！");
//		            } else {  
//						f.createNewFile();
//						CollectLog.LOG.debug("已在目录[" + folderName + "/logs]下成功创建采集标志文件");
//		            }
				}
			}
		} catch (Exception e) {
			CollectLog.LOG.debug(e.getMessage());
		}
	}  
}
