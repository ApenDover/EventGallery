package GUI.Gallery.setUp;

import GUI.Gallery.utils.FileStringConverter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class SettingsLoader {

    private static SettingsLoader instance;

    @Getter
    private String login;

    @Getter
    private String password;

    @Getter
    private String dbLogin;

    @Getter
    private String dbPassword;

    @Getter
    private String subject;

    @Getter
    private String text;

    @Getter
    private String sourceFolder;

    @Getter
    private String qualityResizer;

    @Getter
    private String qualityResizeFolder;

    @Getter
    @Setter
    private boolean byAddTime;

    @Getter
    @Setter
    private boolean byName;

    @Getter
    @Setter
    private boolean newUp;

    @Getter
    @Setter
    private boolean newDown;

    @Getter
    @Setter
    private boolean isItTouch;

    private SettingsLoader() {
    }

    public static SettingsLoader getInstance() {
        if (Objects.isNull(instance)) {
            instance = new SettingsLoader();
        }
        return instance;
    }

    public void loadSettingsFromJsonFile(String pathSettings) {
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

    public void saveSettingsToJsonFile(String login, String password, String subject, String text,
                                              String srcPath, String qualityResizer, String sqlLogin, String sqlPassword) {

        JSONObject jsonObject = new JSONObject();
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
