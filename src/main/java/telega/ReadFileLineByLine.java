package telega;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReadFileLineByLine {

    public Map<String, String> getDBPropConn(){
        Map<String, String> map = new HashMap<String, String>();
        try {
            File file = new File("./src/main/resources/Conn.cfg");

            FileReader fr = new FileReader(file);

            BufferedReader reader = new BufferedReader(fr);

            String line = reader.readLine();

            while (line != null) {
                String[] tmpline = line.split("=", 2);
                map.put(tmpline[0], tmpline[1]);
                line = reader.readLine();
            }

            fr.close();
            reader.close();

/*            String url = map.get("url");
            String user = map.get("user");
            String password = map.get("password");
            System.out.println("URL=" + url + "\n" + "USER=" + user + "\n" + "PASSWORD=" + password);*/

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}
