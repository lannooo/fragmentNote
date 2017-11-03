package lannooo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;


public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static Properties loadProperty(String file){
        Properties prop = new Properties();
        String currentPath =  System.getProperty("user.dir");
        try (InputStream inputStream = new FileInputStream(currentPath+ File.separator+file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8")){
            prop.load(inputStreamReader);
        } catch (FileNotFoundException e) {
            logger.error("{} not found", file, e);
        } catch (IOException e) {
            logger.error("read properties fail", e);
        }
        return prop;
    }

    public static boolean setProperties(String file, Properties prop){
        assert prop != null;
        String currentPath =  System.getProperty("user.dir");
        try(FileOutputStream outputStream = new FileOutputStream(currentPath+File.separator+file,false);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream,"utf-8")) {

            prop.store(writer,"Custom Settings");
        } catch (FileNotFoundException e) {
            logger.error("{} not found", file, e);
            return false;
        } catch (IOException e) {
            logger.error("write properties fail", e);
            return false;
        }
        return true;
    }
}
