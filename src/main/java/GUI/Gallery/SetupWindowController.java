package GUI.Gallery;

import GUI.Gallery.imageResizer.ImgScaller;
import GUI.Gallery.data.connections.BaseConnection;
import GUI.Gallery.data.entities.Company;
import GUI.Gallery.data.entities.Event;
import GUI.Gallery.setUp.SettingsLoader;
import GUI.Gallery.storage.MailBase;
import GUI.Gallery.storage.StageConteiner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import static GUI.Gallery.data.connections.BaseConnection.getEvents;
import static GUI.Gallery.data.connections.BaseConnection.setEvent;

public class SetupWindowController implements Initializable {
    @FXML
    private VBox mainVbox;
    @FXML
    private ToggleButton byAddTime;
    @FXML
    private ToggleButton byName;
    @FXML
    private RadioButton newUp;
    @FXML
    private RadioButton newDown;
    @FXML
    private TextField loginDB;
    @FXML
    private TextField passwordDB;
    @FXML
    private Label connectLabel;
    @FXML
    private CheckBox bgImageCheck;
    @FXML
    private TextField colorNumber;
    @FXML
    private CheckBox bgImageCheck2;
    @FXML
    private TextField pathField;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    public TextField subject;
    @FXML
    private TextField text;
    @FXML
    private ListView companyListView;
    @FXML
    private TextField companyField;
    @FXML
    private ListView allEvents;
    @FXML
    private DatePicker eventDate;
    @FXML
    private TextArea eventText;
    @FXML
    private TextField pathSettings;
    @FXML
    private Button startButton;
    @FXML
    private Button remButton;
    @FXML
    private final ObservableList<String> langs = FXCollections.observableArrayList();
    @FXML
    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    @FXML
    private Stage stage;
    public static boolean rezultbgImageCheck = false;
    public static boolean rezultbgImageCheck2 = false;
    public static String RED;
    public static String GREEN;
    public static String BLUE;
    public static int IdEvent = 0;
    public static Image imageForBackGround;
    public static Image imageForBackGround2;

    /**
     * Настройки
     */
    @FXML
    private void settingsPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open config.json File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "config.json"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            pathSettings.setText(selectedFile.getAbsolutePath());
        }

        if (!(pathSettings.getText().equals(""))) {
            try {
                SettingsLoader.setLoad(pathSettings.getText());
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
            String ap = selectedFile.getAbsolutePath();
            pathField.setText(ap.substring(0, ap.length() - 12));
            pathField.setStyle("-fx-faint-focus-color: #00СС22;");
            pathField.requestFocus();
            login.setText(SettingsLoader.getLogin());
            password.setText(SettingsLoader.getPassword());
            subject.setText(SettingsLoader.getSubject());
            text.setText(SettingsLoader.getText());
            loginDB.setText(SettingsLoader.getSqlLogin());
            passwordDB.setText(SettingsLoader.getSqlPassword());

            if (companyListView.getSelectionModel().getSelectedItem() != null && allEvents.getSelectionModel().getSelectedItem() != null) {
                startButton.disableProperty().set(false);
            }
            if (companyListView.getSelectionModel().getSelectedItem() != null && !eventText.getText().equals("") && !eventDate.getEditor().getText().equals("")) {
                startButton.disableProperty().set(false);
            }
        }

    }

    /**
     * DataBase
     */
    public void connectToDB() {
        companyListView.getItems().clear();
        //        Подключаем базу через hibernate.cfg
        try {
            BaseConnection.addConnection(loginDB.getText(), passwordDB.getText());
            connectLabel.setText("SUCCESS");
            connectLabel.setVisible(true);
            ArrayList<Company> company = new ArrayList<>(BaseConnection.getCompany());
            company.forEach(c -> langs.add(c.getName()));
            companyListView.setItems(langs);
        } catch (Exception e) {
            connectLabel.setVisible(true);
            connectLabel.setText("ERROR");
        }
    }

    /**
     * Галерея
     */
    @FXML
    private void findPath() {
        File selectedFile = directoryChooser.showDialog(stage);
        if (selectedFile != null) {
            String text = selectedFile.getAbsolutePath();
            pathField.setText(text);

            if (!Objects.equals(password.getText(), "") && !Objects.equals(login.getText(), "") && !Objects.equals(subject.getText(), "") && !Objects.equals(this.text.getText(), "") && !Objects.equals(pathField.getText(), "") &&
                    (!Objects.equals(eventText.getText(), "")) && (!Objects.equals(eventDate.getEditor().getText(), ""))) {
                startButton.disableProperty().set(false);
            }

            if (!Objects.equals(pathSettings.getText(), "") && !Objects.equals(eventDate.getEditor().getText(), "") && !Objects.equals(eventText.getText(), "")) {
                startButton.disableProperty().set(false);
            }

            if (!Objects.equals(pathSettings.getText(), "") && allEvents.getSelectionModel().getSelectedItem() != null) {
                startButton.disableProperty().set(false);
            }

        }
    }

    @FXML
    private void pathFieldClick() {
        if (!Objects.equals(password.getText(), "") && !Objects.equals(login.getText(), "") && !Objects.equals(subject.getText(), "") && !Objects.equals(text.getText(), "") && !Objects.equals(pathField.getText(), "") && allEvents.getSelectionModel().getSelectedItem() != null &&
                allEvents.getSelectionModel().getSelectedItem() != null && !Objects.equals(eventText.getText(), "") && !Objects.equals(eventDate.getEditor().getText(), "")) {
            startButton.disableProperty().set(false);
        }

        if (!Objects.equals(pathSettings.getText(), "") && companyListView.getSelectionModel().getSelectedItem() != null && !Objects.equals(eventDate.getEditor().getText(), "") && !Objects.equals(eventText.getText(), "")) {
            startButton.disableProperty().set(false);
        }

        if (!Objects.equals(pathSettings.getText(), "") && companyListView.getSelectionModel().getSelectedItem() != null && allEvents.getSelectionModel().getSelectedItem() != null) {
            startButton.disableProperty().set(false);
        }
    }

    @FXML
    private void folderResize() {
        if (!Objects.equals(pathField.getText(), "")) {
            Thread thread = new Thread(() ->
            {
                File[] allFiles = new File(pathField.getText()).listFiles();
                TreeSet<File> fileInFolder = new TreeSet<>();
                for (File file : allFiles) {
                    if (!(file.isDirectory()) && (file.getName().charAt(0) != '.') && (file.getName() != "config.json")) {
                        fileInFolder.add(file);
                    }
                }
                if (!fileInFolder.isEmpty()) {
                    new ImgScaller(fileInFolder);
                }
            });
            thread.start();
        }
    }

    @FXML
    private void findPicForBackGround(ActionEvent event) throws FileNotFoundException {
        colorNumber.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("background gallery Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            imageForBackGround = new Image(new FileInputStream(selectedFile));
            bgImageCheck.setSelected(true);
            bgImageCheck.setDisable(false);
        }
    }

    public void typingColor(KeyEvent event) {
        bgImageCheck.setSelected(false);
        bgImageCheck.setDisable(true);
        bgImageCheck2.setSelected(false);
        bgImageCheck2.setDisable(true);
    }

    @FXML
    private void findPicForBackGround2(ActionEvent event) throws FileNotFoundException {
        colorNumber.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("background sender Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            imageForBackGround2 = new Image(new FileInputStream(selectedFile));
            bgImageCheck2.setSelected(true);
            bgImageCheck2.setDisable(false);
        }
    }

    /**
     * Мероприятие
     */
    @FXML
    private void addCompany() {
        if (!(companyField.getText().equals(""))) {
            BaseConnection.setCompany(companyField.getText());
            langs.add(companyField.getText());
            companyField.setText("");
            companyField.setFocusTraversable(false);
            companyListView.setItems(langs);

        }
    }

    @FXML
    private void removeCompany() {
        BaseConnection.removeCompany(companyField.getText());
        langs.remove(companyField.getText());
        companyField.setText("");
        companyField.setFocusTraversable(false);
        companyListView.setItems(langs);
        allEvents.getItems().clear();
    }

    @FXML
    private void getEventsFromListView() {

        try {
            String choose = companyListView.getFocusModel().getFocusedItem().toString();
            ArrayList<Event> now = new ArrayList<>(BaseConnection.getEventsFromCompany(choose));
            ObservableList<String> events = FXCollections.observableArrayList();
            now.forEach(event -> {
                events.add(event.getDate().toString() + " : " + event.getDescription());
            });
            companyListView.getFocusModel().getFocusedItem();
            allEvents.setItems(events);
        } catch (NullPointerException ignored) {
        }


        UpdateStartButtonVisible();

    }

    private void UpdateStartButtonVisible() {
        if (!Objects.equals(password.getText(), "") && !Objects.equals(login.getText(), "")
                && !Objects.equals(subject.getText(), "") && !Objects.equals(text.getText(), "")
                && !Objects.equals(pathField.getText(), "") && !Objects.equals(eventText.getText(), "")
                && !Objects.equals(eventDate.getEditor().getText(), "")) {
            startButton.disableProperty().set(false);
        }

        if (!Objects.equals(pathSettings.getText(), "") && !Objects.equals(eventDate.getEditor().getText(), "") && !Objects.equals(eventText.getText(), "")) {
            startButton.disableProperty().set(false);
        }

        if (!Objects.equals(pathSettings.getText(), "") && allEvents.getSelectionModel().getSelectedItem() != null) {
            startButton.disableProperty().set(false);
        }

        if (!Objects.equals(eventDate.getEditor().getText(), "") || !Objects.equals(eventDate.getEditor().getText(), null)
                || !Objects.equals(eventText.getText(), "")) {
            startButton.disableProperty().set(true);
        }
    }

    @FXML
    private void DatePickerORTextAreaClick() {
        allEvents.getSelectionModel().clearSelection();
    }

    @FXML
    private void typeEventText() {
        if (!Objects.equals(password.getText(), "") && !Objects.equals(login.getText(), "") && !Objects.equals(subject.getText(), "") && !Objects.equals(text.getText(), "") &&
                (!Objects.equals(pathField.getText(), "") && !Objects.equals(eventText.getText(), "") && !Objects.equals(eventDate.getEditor().getText(), "") &&
                        companyListView.getSelectionModel().getSelectedItem() != null)) {
            startButton.disableProperty().set(false);
        } else
            startButton.disableProperty().set(!(!Objects.equals(password.getText(), "") && !Objects.equals(login.getText(), "") && !Objects.equals(subject.getText(), "") && !Objects.equals(text.getText(), "") &&
                    !Objects.equals(pathField.getText(), "") && companyListView.getSelectionModel().getSelectedItem() != null && allEvents.getSelectionModel().getSelectedItem() != null));
    }

    /**
     * Почта
     */
    @FXML
    private void passwordPressKey() {
        if (!Objects.equals(password.getText(), "") && !Objects.equals(login.getText(), "") && !Objects.equals(subject.getText(), "") && !Objects.equals(text.getText(), "") &&
                !Objects.equals(pathField.getText(), "") && !Objects.equals(eventText.getText(), "") && !Objects.equals(eventDate.getEditor().getText(), "") &&
                companyListView.getSelectionModel().getSelectedItem() != null) {
            startButton.disableProperty().set(false);
        } else
            startButton.disableProperty().set(!(!Objects.equals(password.getText(), "") && !Objects.equals(login.getText(), "") && !Objects.equals(subject.getText(), "") && !Objects.equals(text.getText(), "") &&
                    !Objects.equals(pathField.getText(), "") && companyListView.getSelectionModel().getSelectedItem() != null && allEvents.getSelectionModel().getSelectedItem() != null));
    }

    @FXML
    private void loginTyped() {
        if ((!Objects.equals(password.getText(), "")) && (!Objects.equals(login.getText(), "")) && (!Objects.equals(subject.getText(), "")) && (!Objects.equals(text.getText(), "")) &&
                (!Objects.equals(pathField.getText(), "")) && (!Objects.equals(eventText.getText(), "")) && (!Objects.equals(eventDate.getEditor().getText(), "")) &&
                (companyListView.getSelectionModel().getSelectedItem() != null)) {
            startButton.disableProperty().set(false);
        } else
            startButton.disableProperty().set(!((!Objects.equals(password.getText(), "")) && (!Objects.equals(login.getText(), "")) && (!Objects.equals(subject.getText(), "")) && (!Objects.equals(text.getText(), "")) &&
                    (!Objects.equals(pathField.getText(), "")) && (companyListView.getSelectionModel().getSelectedItem() != null) && (allEvents.getSelectionModel().getSelectedItem() != null)));
    }

    @FXML
    private void subjectClick() {
        if (!Objects.equals(password.getText(), "") && !Objects.equals(login.getText(), "") && !Objects.equals(subject.getText(), "") && !Objects.equals(text.getText(), "") &&
                (!Objects.equals(pathField.getText(), "")) && (!Objects.equals(eventText.getText(), "")) && (!Objects.equals(eventDate.getEditor().getText(), "")) &&
                companyListView.getSelectionModel().getSelectedItem() != null) {
            startButton.disableProperty().set(false);
        } else
            startButton.disableProperty().set(!(!Objects.equals(password.getText(), "") && !Objects.equals(login.getText(), "") && !Objects.equals(subject.getText(), "") && !Objects.equals(text.getText(), "") &&
                    !Objects.equals(pathField.getText(), "") && companyListView.getSelectionModel().getSelectedItem() != null && allEvents.getSelectionModel().getSelectedItem() != null));
    }

    @FXML
    private void textClick() {
        if (!Objects.equals(password.getText(), "") && !Objects.equals(login.getText(), "") && !Objects.equals(subject.getText(), "") && !Objects.equals(text.getText(), "") &&
                !Objects.equals(pathField.getText(), "") && !Objects.equals(eventText.getText(), "") && !Objects.equals(eventDate.getEditor().getText(), "") &&
                companyListView.getSelectionModel().getSelectedItem() != null) {
            startButton.disableProperty().set(false);
        } else
            startButton.disableProperty().set(!(!Objects.equals(password.getText(), "") && !Objects.equals(login.getText(), "") && !Objects.equals(subject.getText(), "") && !Objects.equals(text.getText(), "") &&
                    !Objects.equals(pathField.getText(), "") && companyListView.getSelectionModel().getSelectedItem() != null && allEvents.getSelectionModel().getSelectedItem() != null));
    }

    @FXML
    private void allEventsClick() {
        if (allEvents.getSelectionModel().getSelectedItem() != null) {
            eventDate.getEditor().clear();
            eventText.setText("");

            if ((!Objects.equals(pathSettings.getText(), "")) && (companyListView.getSelectionModel().getSelectedItem() != null)) {
                startButton.disableProperty().set(false);
            } else if (companyListView.getSelectionModel().getSelectedItem() != null && !Objects.equals(pathField.getText(), "") &&
                    companyListView.getSelectionModel().getSelectedItem() != null && !Objects.equals(login.getText(), "") &&
                    !Objects.equals(password.getText(), "") && !Objects.equals(text.getText(), "") && !Objects.equals(subject.getText(), ""))
                ;
            {
                startButton.disableProperty().set(false);
            }
        }

    }


    @FXML
    private void openDel() {
        remButton.setDisable(false);
    }

    @FXML
    private void start(MouseEvent click) throws IOException, java.text.ParseException {
        rezultbgImageCheck = bgImageCheck.isSelected();
        rezultbgImageCheck2 = bgImageCheck2.isSelected();

        if (colorNumber.getText().length() == 6) {
            ImageMediaController.colorNumber = colorNumber.getText();
            GalleryController.colorNumber = colorNumber.getText();
            RED = colorNumber.getText().substring(0, 2);
            GREEN = colorNumber.getText().substring(2, 4);
            BLUE = colorNumber.getText().substring(4, 6);
        }

        if (colorNumber.getText().length() == 7) {
            ImageMediaController.colorNumber = colorNumber.getText();
            GalleryController.colorNumber = colorNumber.getText();
            RED = colorNumber.getText().substring(1, 3);
            GREEN = colorNumber.getText().substring(3, 5);
            BLUE = colorNumber.getText().substring(5, 7);
        }
        /**
         * Запишем настройки сортировки в SettingsLoader
         * */
        SettingsLoader.byAddTime = byAddTime.isSelected();
        SettingsLoader.byName = byName.isSelected();
        SettingsLoader.newUp = newUp.isSelected();
        SettingsLoader.newDown = newDown.isSelected();

        /**
         * Провери пустая ли папка, если нет - запустим ресайзер (а если пустая?)
         * */

        /** Записываем настройки в config.json */
        try {
            SettingsLoader.saveLoad(login.getText(), password.getText(), subject.getText(), text.getText(), pathField.getText(), "300", loginDB.getText(), passwordDB.getText());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        /**
         * Заносим мероприятие в БД или определяемся к какому существующему мероприятию будут относиться данные
         * */
        if ((!Objects.equals(eventDate.getEditor().getText(), "")) && (!Objects.equals(eventText.getText(), ""))) {
            //создаем новое мероприятие
            Date date = Date.from(eventDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            setEvent(date, eventText.getText(), companyListView.getFocusModel().getFocusedItem().toString());
            ArrayList<Event> eventArrayList = new ArrayList<>(getEvents());
            String description = eventText.getText();
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date utilDate = df.parse(eventDate.getEditor().getText());
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            eventArrayList.forEach(event -> {
//                можно взять максимальное значение ключа (последний добавленый), но будем сравнивать
//                параметры на случай ручного вмешательства в БД
                if ((event.getDate().equals(sqlDate)) && event.getDescription().equals(description)) {
                    IdEvent = event.getIdEvent();
                }
            });
        } else {
            //если выбрали существующее мероприятие мэтчим с базой по дате и описанию, ключ запомним.
            // будем записывать ключ в таблицу sender с отправкой писем
            String[] pole = allEvents.getSelectionModel().getSelectedItem().toString().split(" : ");
            AtomicInteger IdEvent = new AtomicInteger();
            String description = pole[1];
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date utilDate = df.parse(pole[0]);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            ArrayList<Event> eventArrayList = new ArrayList<>(getEvents());
            eventArrayList.forEach(event -> {
                if ((event.getDate().equals(sqlDate)) && event.getDescription().equals(description)) {
                    IdEvent.set(event.getIdEvent());
                }
            });
            SetupWindowController.IdEvent = IdEvent.get();
            MailBase.mailsFromBase.addAll(BaseConnection.getMails());
            MailBase.mailsFromBase.forEach(sender -> MailBase.mailStorage.add(sender.getMail()));
        }

/**
 * Грузим данные в статический класс из config.json и открываем Gallerey-view.fxml
 * */

        try {
            SettingsLoader.setLoad(pathField.getText() + "/" + "config.json");
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        Parent root = FXMLLoader.load(getClass().getResource("Gallery-view.fxml"));
        StageConteiner.stage = (Stage) ((Node) click.getSource()).getScene().getWindow();
        Rectangle2D r = Screen.getPrimary().getBounds();
        StageConteiner.stage.setWidth(r.getWidth());
        StageConteiner.stage.setHeight(r.getHeight());
        StageConteiner.stage.centerOnScreen();
        StageConteiner.stage.getScene().setRoot(root);
        StageConteiner.stage.setFullScreenExitHint("");
        StageConteiner.stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        StageConteiner.stage.setFullScreen(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventDate.setValue(LocalDate.now());
    }
}
