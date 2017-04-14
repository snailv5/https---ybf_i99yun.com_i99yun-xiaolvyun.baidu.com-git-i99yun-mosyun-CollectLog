package moskaa.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadLog {
	
	public ArrayList<String> txtlines = new ArrayList<String>();
	
	/**
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param filePath
     */
    public void readTxtFile(String filePath) throws Exception{
        try {
            //String encoding="UTF-8";
    		String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	txtlines.add(lineTxt);
                }
                read.close();
	        }else{
	        	throw new Exception("找不到指定的文件[" + filePath + "]");
	        }
        } catch (Exception e) {
        	throw new Exception("读取文件[" + filePath + "]出错->" + e.getMessage());
        }
     
    }
}
