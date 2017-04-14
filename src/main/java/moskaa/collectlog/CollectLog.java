package moskaa.collectlog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.kaaproject.kaa.client.DesktopKaaPlatformContext;
import org.kaaproject.kaa.client.Kaa;
import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.SimpleKaaClientStateListener;
import org.kaaproject.kaa.client.logging.BucketInfo;
import org.kaaproject.kaa.client.logging.RecordInfo;
import org.kaaproject.kaa.client.logging.future.RecordFuture;
import org.kaaproject.kaa.client.logging.strategies.RecordCountLogUploadStrategy;

import org.kaaproject.kaa.schema.collectlog.CollectLogFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * 采集文件日志
 *
 */
public class CollectLog 
{
    public static final Logger LOG = LoggerFactory.getLogger(CollectLog.class);
    public static KaaClient kaaClient = null;
    public static boolean kaaClientStatus = false;
    
    public CollectLog() {
    	LOG.info("Data collection demo started");
        //Create a Kaa client with the Kaa desktop context.
        kaaClient = Kaa.newClient(new DesktopKaaPlatformContext(), new SimpleKaaClientStateListener() {
            @Override
            public void onStarted() {
            	CollectLog.kaaClientStatus = true;
                LOG.info("Kaa client started");
            }

            @Override
            public void onStopped() {
            	CollectLog.kaaClientStatus = false;
                LOG.info("Kaa client stopped");
            }
        });

        // Set record count strategy for uploading logs with count threshold set to 1.
        // Defined strategy configuration informs to upload every log record as soon as it is created.
        // The default strategy uploads logs after either a threshold logs count
        // or a threshold logs size has been reached.
        kaaClient.setLogUploadStrategy(new RecordCountLogUploadStrategy(1));

        // Start the Kaa client and connect it to the Kaa server.
        kaaClient.start();

        // Stop the Kaa client and release all the resources which were in use.
        //kaaClient.stop();

        //LOG.info("Data collection demo stopped");
    }
    
    public static void saveDocs(List<CollectLogFile> docs) {
    	// Collect log record delivery futures and corresponding log record creation timestamps.
        Map<RecordFuture, Long> futuresMap = new HashMap<RecordFuture, Long>();
        
        // Send logs in a loop.
        for (CollectLogFile log : docs) {
            futuresMap.put(kaaClient.addLogRecord(log), (long)(Math.random()*100));
            LOG.info("Log record {} sent", log.toString());
        }


        // Iterate over log record delivery futures and wait for delivery
        // acknowledgment for each record.
//        for (RecordFuture future : futuresMap.keySet()) {
//            try {
//            	
//                RecordInfo recordInfo = future.get();
//                BucketInfo bucketInfo = recordInfo.getBucketInfo();
//                Long timeSpent = (recordInfo.getRecordAddedTimestampMs() - futuresMap.get(future))
//                        + recordInfo.getRecordDeliveryTimeMs();
//                LOG.info(
//                        "Received log record delivery info. Bucket Id [{}]. Record delivery time [{} ms].",
//                        bucketInfo.getBucketId(), timeSpent);
//            } catch (Exception e) {
//                LOG.error(
//                        "Exception was caught while waiting for callback future",
//                        e);
//            }
//        }
        
//        kaaClient.stop();
    }
    
//    public List<CollectLogFile> generateRecordsLogs(ArrayList<String> txtlines) {
//        List<CollectLogFile> logs = new LinkedList<CollectLogFile>();
//        
//        for (String txtline : txtlines) {
//        	String str[] = txtline.split(",", 15);
//        	Long curTime = System.currentTimeMillis(); 
//            logs.add(new CollectLogFile(str[0], 0, "", 0, "", "", 0.0, Integer.parseInt(str[2]),Integer.parseInt(str[3]),Integer.parseInt(str[4]),Integer.parseInt(str[5]),str[8],curTime));
//        }
//        return logs;
//    }
}
