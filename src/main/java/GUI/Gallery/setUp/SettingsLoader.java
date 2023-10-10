package GUI.Gallery.setUp;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsLoader {

    private static String login;
    private static String password;
    private static String dbLogin;
    private static String dbPassword;
    private static String subject;
    private static String text;
    private static String sourceFolder;
    private static String qualityResizer;
    public static boolean byAddTime;
    public static boolean byName;
    public static boolean newUp;
    public static boolean newDown;
    public static boolean isItTouch;

    public static void setLoad(String pathSettings) throws IOException, ParseException {
        FileReader reader = new FileReader(pathSettings);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
        login = (String) jsonObject.get("login");
        password = (String) jsonObject.get("password");
        subject = (String) jsonObject.get("subject");
        text = (String) jsonObject.get("text");
        String ps = (String) jsonObject.get("folderPath");
        sourceFolder = ps.replaceAll("\\\\", "");
        qualityResizer = (String) jsonObject.get("qualityResizer");
        dbLogin = (String) jsonObject.get("sqlLogin");
        dbPassword = (String) jsonObject.get("sqlPassword");
    }

    public static void saveLoad(String login, String password, String subject, String text, String srcPath, String qualityResizer, String sqlLogin, String sqlPassword) throws IOException, ParseException {
        //Creating a JSONObject object
        JSONObject jsonObject = new JSONObject();
        //Inserting key-value pairs into the json object
        jsonObject.put("login", login);
        jsonObject.put("password", password);
        jsonObject.put("subject", subject);
        jsonObject.put("text", text);
        jsonObject.put("folderPath", srcPath);
        jsonObject.put("qualityResizer", qualityResizer);
        jsonObject.put("sqlLogin", sqlLogin);
        jsonObject.put("sqlPassword", sqlPassword);
        try {
            FileWriter file = new FileWriter(srcPath + "/" + "config.json");
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLogin() {
        return login;
    }

    public static String getPassword() {
        return password;
    }

    public static String getSqlLogin() {
        return dbLogin;
    }

    public static String getSqlPassword() {
        return dbPassword;
    }

    public static String getSubject() {
        return subject;
    }

    public static String getText() {
        return text;
    }

    public static String getSourceFolder() {
        return sourceFolder;
    }

    public static String getQualityResizer() {
        return qualityResizer;
    }
}
