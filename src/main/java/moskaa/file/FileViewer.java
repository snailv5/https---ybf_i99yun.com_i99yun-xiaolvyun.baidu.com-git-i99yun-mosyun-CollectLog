package moskaa.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import moskaa.collectlog.CollectLog;


/** 
 * 读取目录及子目录下指定文件名的路径, 返回一个List 
 */  
  
public class FileViewer {

	private static String path = "";	// /Users/befu/work/other/宇之光飞针测试日志/4v2fq00h-1/logs
	//物料目录，采集状态的映射关系，如果已经采集(1.采集成功，2.多次重试采集失败)，
	//则在物料号的日志目录下创建一个“OK”文件
	//且设置collectFlag为true
	private HashMap<String, Boolean> materials2flag = new HashMap<String, Boolean>();
	
	/**
	 * @param path
	 *            文件路径
	 * @param suffix
	 *            后缀名, 为空则表示所有文件
	 * @param isdepth
	 *            是否遍历子目录
	 * @return list
	 */
	public List<String> getListFiles(String p, String suffix, boolean isdepth) {
		List<String> lstFileNames = new ArrayList<String>();
		try {
			path = p + "/";
			File file = new File(p);
			listFile(lstFileNames, file, suffix, isdepth);
		} catch (Exception e) {
			CollectLog.LOG.debug(e.getMessage());
		}
		
		return lstFileNames;
	}
  
	private List<String> listFile(List<String> lstFileNames, File f, String suffix, boolean isdepth) {
		try {
			String fileName = "";
			// 若是目录, 采用递归的方法遍历子目录
			if (f.isDirectory()) {
				File[] t = f.listFiles();

				for (int i = 0; i < t.length; i++) {
					if (isdepth || t[i].isFile()) {
						listFile(lstFileNames, t[i], suffix, isdepth);
					}
				}
			} else {
				String filePath = f.getAbsolutePath();
				//兼容windows
				filePath = filePath.replace("\\", "/");
				if (!suffix.equals("")) {
					int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
					String tempsuffix = "";

					if (begIndex != -1) {
						tempsuffix = filePath.substring(begIndex + 1, filePath.length());
						if (tempsuffix.equals(suffix)) {
							//如果在该目录中发现了OK文件，则表示该目录已经采集过，设置采集状态为true
							if (filePath.indexOf("OK") >= 0) {
								materials2flag.put(path, true);
								return null;
							}
							fileName = filePath.replace(path, "");
							lstFileNames.add(fileName);
						}
					}
				} else {
					//如果在该目录中发现了OK文件，则表示该目录已经采集过，设置采集状态为true
					if (filePath.indexOf("OK") >= 0) {
						materials2flag.put(path, true);
						return null;
					}
					fileName = filePath.replace(path, "");
					//只保留以数字开头的有效文件名，表示测试号
					if (fileName.substring(0, 1).matches("[0-9]+")) {
						lstFileNames.add(fileName);
					}
				}
			}
		} catch (Exception e) {
			CollectLog.LOG.debug("扫描日志文件目录时出错->" + e.getMessage());
		}
		
		return lstFileNames;
	}
	
	/**
	 * 获取文件夹列表
	 * @param p
	 * @return
	 */
	public List<String> getListFolder(String p) {
		List<String> listFolderNames = new ArrayList<String>();
		try {
			path = p + "/";
			File file = new File(p);
			
			if (file.isDirectory()) {
				File[] t = file.listFiles();
				for (int i = 0; i < t.length; i++) {
					if (t[i].isDirectory()) {
						String filePath = t[i].getAbsolutePath();
						//兼容windows
						filePath = filePath.replace("\\", "/");
						String folderName = filePath.replace(path, "");
						listFolderNames.add(folderName);
					}
				}
			} else
				return null;
		} catch (Exception e) {
			CollectLog.LOG.debug("获取文件夹列表时出错->" + e.getMessage());
			return null;
		}
		return listFolderNames;
	}

	public HashMap<String, Boolean> getMaterials2flag() {
		return materials2flag;
	}

	public void setMaterials2flag(HashMap<String, Boolean> materials2flag) {
		this.materials2flag = materials2flag;
	}
}