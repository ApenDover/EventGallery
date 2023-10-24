package GUI.Gallery.setUp;

import GUI.Gallery.utils.FileStringConverter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsLoader {

    @Getter
    private static String login;

    @Getter
    private static String password;

    @Getter
    private static String dbLogin;

    @Getter
    private static String dbPassword;

    @Getter
    private static String subject;

    @Getter
    private static String text;

    @Getter
    private static String sourceFolder;

    @Getter
    private static String qualityResizer;

    @Getter
    private static String qualityResizeFolder;

    @Getter
    @Setter
    private static boolean byAddTime;

    @Getter
    @Setter
    private static boolean byName;

    @Getter
    @Setter
    private static boolean newUp;

    @Getter
    @Setter
    private static boolean newDown;

    @Getter
    @Setter
    private static boolean isItTouch;

    private SettingsLoader() {
    }

    public static void setLoad(String pathSettings) {
        try {
            FileReader reader = new FileReader(pathSettings);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            login = (String) jsonObject.get("login");
            password = (String) jsonObject.get("password");
            subject = (String) jsonObject.get("subject");
            text = (String) jsonObject.get("text");
            String ps = (String) jsonObject.get("folderPath");
            sourceFolder = ps.replaceAll("\\\\", StringUtils.EMPTY);
            qualityResizer = (String) jsonObject.get("qualityResizer");
            qualityResizeFolder = sourceFolder + "/" + qualityResizer;
            dbLogin = (String) jsonObject.get("sqlLogin");
            dbPassword = (String) jsonObject.get("sqlPassword");
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveLoad(String login, String password, String subject, String text,
                                String srcPath, String qualityResizer, String sqlLogin, String sqlPassword) {
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
        try (FileWriter file = new FileWriter(FileStringConverter.getFilePath(srcPath, "config", "json"))) {
            file.write(jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
