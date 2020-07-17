package telega;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class ConnectionPropertiesDB {
    String URL;
    String USER;
    String PASSWORD;

    public HashMap<String, String> getConnPropDB() throws IOException {

        Properties props = new Properties();
        File file = new File("./src/main/resources/Conn.cfg");
        FileInputStream fis = new FileInputStream(file);

        props.load(fis);

        URL = props.getProperty("URL");
        USER = props.getProperty("USER");
        PASSWORD = props.getProperty("PASSWORD");


        HashMap<String, String> connPropHashMap = new HashMap<String, String>();
        connPropHashMap.put("url", URL);
        connPropHashMap.put("user", USER);
        connPropHashMap.put("password", PASSWORD);

        fis.close();
        return connPropHashMap;

    }

}

