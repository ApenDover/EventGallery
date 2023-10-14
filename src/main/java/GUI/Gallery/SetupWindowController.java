package GUI.Gallery;

import GUI.Gallery.data.connections.BaseConnection;
import GUI.Gallery.data.entity.Company;
import GUI.Gallery.data.entity.Event;
import GUI.Gallery.runnable.ScalePreviewImagesProcess;
import GUI.Gallery.setUp.SettingsLoader;
import GUI.Gallery.storage.MailBase;
import GUI.Gallery.storage.StageContainer;
import GUI.Gallery.utils.EmptyChecker;
import javafx.application.Platform;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static GUI.Gallery.data.connections.BaseConnection.getEvents;
import static GUI.Gallery.data.connections.BaseConnection.setEvent;

public class SetupWindowController implements Initializable {

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
    private ListView<String> companyListView;

    @FXML
    private TextField companyField;

    @FXML
    private ListView<String> allEvents;

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
    private static Stage stage;

    @Getter
    private static boolean resultBgImageCheck = false;

    @Getter
    private static boolean resultBgImageCheck2 = false;

    @Getter
    private static String RED;

    @Getter
    private static String GREEN;

    @Getter
    private static String BLUE;

    @Getter
    private static int idEvent = 0;

    @Getter
    private static Image imageForBackGround;

    @Getter
    private static Image imageForBackGround2;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * Настройки
     */
    @FXML
    private void settingsPath() {
        Platform.runLater(() -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open config.json File");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON", "config.json"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (Objects.nonNull(selectedFile)) {
                pathSettings.setText(selectedFile.getAbsolutePath());
                if (StringUtils.isNotEmpty(pathSettings.getText())) {
                    SettingsLoader.setLoad(pathSettings.getText());
                    String ap = selectedFile.getAbsolutePath();
                    pathField.setText(ap.substring(0, ap.length() - 12));
                    pathField.setStyle("-fx-faint-focus-color: #00СС22;");
                    pathField.requestFocus();
                    login.setText(SettingsLoader.getLogin());
                    password.setText(SettingsLoader.getPassword());
                    subject.setText(SettingsLoader.getSubject());
                    text.setText(SettingsLoader.getText());
                    loginDB.setText(SettingsLoader.getDbLogin());
                    passwordDB.setText(SettingsLoader.getDbPassword());
                    try {
                        final var check = (EmptyChecker.isObjectListValid(List.of(companyListView.getSelectionModel(),
                                companyListView.getSelectionModel().getSelectedItem(),
                                allEvents.getSelectionModel(), allEvents.getSelectionModel().getSelectedItem()))
                                || (Objects.nonNull(companyListView.getSelectionModel().getSelectedItem())
                                && EmptyChecker.isStringListValid(List.of(eventText.getText(),
                                eventDate.getEditor().getText()))));
                        startButton.disableProperty().set(!check);
                    } catch (Exception ignored) {
                    }
                }
            }
        });
    }

    /**
     * DataBase
     */
    public void connectToDB() {
        companyListView.getItems().clear();
        try {
            BaseConnection.openConnection(loginDB.getText(), passwordDB.getText());
            connectLabel.setText("SUCCESS");
            connectLabel.setVisible(true);
            ArrayList<Company> companies = new ArrayList<>(BaseConnection.getCompany());
            companies.forEach(company -> langs.add(company.getName()));
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
        if (Objects.nonNull(selectedFile)) {
            pathField.setText(selectedFile.getAbsolutePath());
            final var check = checkAllFields() || EmptyChecker.isStringListValid(List.of(pathSettings.getText(), eventDate.getEditor().getText(), eventText.getText()))
                    || (StringUtils.isNotBlank(pathSettings.getText()) && Objects.nonNull(allEvents.getSelectionModel().getSelectedItem()));
            startButton.disableProperty().set(!check);
        }
    }

    @FXML
    private void pathFieldClick() {
        final var check = (checkAllFields() && Objects.nonNull(allEvents.getSelectionModel().getSelectedItem()))
                || (checkFileFields() && Objects.nonNull(companyListView.getSelectionModel().getSelectedItem()))
                || (StringUtils.isNotBlank(pathSettings.getText()) && isCompanyListViewAllEventPersist());
        startButton.disableProperty().set(!check);
    }

    @FXML
    private void folderResize() {
        if (StringUtils.isNotBlank(pathField.getText())) {
            final var scalePreviewImagesProcess = new ScalePreviewImagesProcess(pathField);
            executor.execute(scalePreviewImagesProcess);
            executor.shutdown();
        }
    }

    @FXML
    private void findPicForBackground(ActionEvent event) {
        findPic(bgImageCheck, "background gallery Image");
    }

    @FXML
    private void findPicForBackground2(ActionEvent event) {
        findPic(bgImageCheck2, "background sender Image");
    }

    public void typingColor(KeyEvent event) {
        bgImageCheck.setSelected(false);
        bgImageCheck.setDisable(true);
        bgImageCheck2.setSelected(false);
        bgImageCheck2.setDisable(true);
    }

    /**
     * Мероприятие
     */
    @FXML
    private void addCompany() {
        if (StringUtils.isNotBlank(companyField.getText())) {
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
            String choose = companyListView.getFocusModel().getFocusedItem();
            ArrayList<Event> now = new ArrayList<>(BaseConnection.getEventsFromCompany(choose));
            ObservableList<String> events = FXCollections.observableArrayList();
            now.forEach(event -> events.add(event.getDate().toString() + " : " + event.getDescription()));
            companyListView.getFocusModel().getFocusedItem();
            allEvents.setItems(events);
            final var check = ((checkAllFields() || checkFileFields() || StringUtils.isNotBlank(pathSettings.getText()))
                    || Objects.nonNull(allEvents.getSelectionModel().getSelectedItem())
                    || EmptyChecker.isStringListValid(List.of(eventDate.getEditor().getText(), eventText.getText())));
            startButton.disableProperty().set(!check);
        } catch (Exception ignored) {}

    }

    @FXML
    private void DatePickerORTextAreaClick() {
        allEvents.getSelectionModel().clearSelection();
    }

    @FXML
    private void typeEventText() {
        final var check = (checkAllFields() && Objects.nonNull(companyListView.getSelectionModel().getSelectedItem()))
                || (checkTextFields() && isCompanyListViewAllEventPersist());
        startButton.disableProperty().set(!check);
    }

    /**
     * Почта
     */
    @FXML
    private void passwordPressKey() {
        final boolean check = (checkAllFields() && Objects.nonNull(companyListView.getSelectionModel().getSelectedItem()))
                || (checkTextFields() && EmptyChecker.isObjectListValid(List.of(companyListView.getSelectionModel().getSelectedItem(),
                companyListView.getSelectionModel().getSelectedItem(), allEvents.getSelectionModel().getSelectedItem())));
        startButton.disableProperty().set(!check);
    }

    @FXML
    private void loginTyped() {
        final boolean check = (checkAllFields() && Objects.nonNull(companyListView.getSelectionModel().getSelectedItem()))
                || (checkTextFields() && isCompanyListViewAllEventPersist());
        startButton.disableProperty().set(!check);
    }

    @FXML
    private void subjectClick() {
        final boolean check = (checkAllFields() && Objects.nonNull(companyListView.getSelectionModel().getSelectedItem()))
                || (checkTextFields() && isCompanyListViewAllEventPersist());
        startButton.disableProperty().set(!check);
    }

    @FXML
    private void textClick() {
        final boolean check = (checkAllFields() && Objects.nonNull(companyListView.getSelectionModel().getSelectedItem()))
                || (checkAllFields() && Objects.nonNull(companyListView.getSelectionModel().getSelectedItem())
                || (checkTextFields() && isCompanyListViewAllEventPersist()));
        startButton.disableProperty().set(!check);

    }

    private boolean isCompanyListViewAllEventPersist() {
        return EmptyChecker.isObjectListValid(List.of(companyListView.getSelectionModel().getSelectedItem(), allEvents.getSelectionModel().getSelectedItem()));
    }

    @FXML
    private void allEventsClick() {
        if (Objects.nonNull(allEvents.getSelectionModel().getSelectedItem())) {
            eventDate.getEditor().clear();
            eventText.setText("");
            final var check = ((StringUtils.isNotBlank(pathSettings.getText()) && Objects.nonNull(companyListView.getSelectionModel().getSelectedItem()))
                    || (Objects.nonNull(companyListView.getSelectionModel().getSelectedItem()) && checkTextFields()));
            startButton.disableProperty().set(!check);
        }

    }

    @FXML
    private void openDel() {
        remButton.setDisable(false);
    }

    @FXML
    private void start(MouseEvent click) throws IOException, java.text.ParseException {
        resultBgImageCheck = bgImageCheck.isSelected();
        resultBgImageCheck2 = bgImageCheck2.isSelected();

        if (colorNumber.getText().length() == 6) {
            ImageMediaController.setColorNumber(colorNumber.getText());
            GalleryController.setColorNumber(colorNumber.getText());
            RED = colorNumber.getText().substring(0, 2);
            GREEN = colorNumber.getText().substring(2, 4);
            BLUE = colorNumber.getText().substring(4, 6);
        }

        if (colorNumber.getText().length() == 7) {
            ImageMediaController.setColorNumber(colorNumber.getText());
            GalleryController.setColorNumber(colorNumber.getText());
            RED = colorNumber.getText().substring(1, 3);
            GREEN = colorNumber.getText().substring(3, 5);
            BLUE = colorNumber.getText().substring(5, 7);
        }
        /**
         * Запишем настройки сортировки в SettingsLoader
         * */
        SettingsLoader.setByAddTime(byAddTime.isSelected());
        SettingsLoader.setByName(byName.isSelected());
        SettingsLoader.setNewUp(newUp.isSelected());
        SettingsLoader.setNewDown(newDown.isSelected());

        /**
         * Провери пустая ли папка, если нет - запустим ресайзер (а если пустая?)
         * */

        /** Записываем настройки в config.json */
        SettingsLoader.saveLoad(login.getText(), password.getText(), subject.getText(), text.getText(),
                pathField.getText(), "300", loginDB.getText(), passwordDB.getText());


        /**
         * Заносим мероприятие в БД или определяемся к какому существующему мероприятию будут относиться данные
         * */
        if (StringUtils.isNotBlank(eventDate.getEditor().getText()) && StringUtils.isNotBlank(eventText.getText())) {
            //создаем новое мероприятие
            Date date = Date.from(eventDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            setEvent(date, eventText.getText(), companyListView.getFocusModel().getFocusedItem());
            ArrayList<Event> eventArrayList = new ArrayList<>(getEvents());
            String description = eventText.getText();
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("dd.MM.yyyy")
                    .toFormatter();
            LocalDate localDate = LocalDate.parse(eventDate.getEditor().getText(), formatter);
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
            eventArrayList.forEach(event -> {
//                можно взять максимальное значение ключа (последний добавленый), но будем сравнивать
//                параметры на случай ручного вмешательства в БД
                if ((event.getDate().equals(sqlDate)) && event.getDescription().equals(description)) {
                    idEvent = event.getIdEvent();
                }
            });
        } else {
            //если выбрали существующее мероприятие мэтчим с базой по дате и описанию, ключ запомним.
            // будем записывать ключ в таблицу sender с отправкой писем
            String[] pole = allEvents.getSelectionModel().getSelectedItem().split(" : ");
            AtomicInteger atomicIdEvent = new AtomicInteger();
            String description = pole[1];
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date utilDate = df.parse(pole[0]);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            ArrayList<Event> eventArrayList = new ArrayList<>(getEvents());
            eventArrayList.forEach(event -> {
                if ((event.getDate().equals(sqlDate)) && event.getDescription().equals(description)) {
                    atomicIdEvent.set(event.getIdEvent());
                }
            });
            idEvent = atomicIdEvent.get();
            MailBase.getMailsFromBase().addAll(BaseConnection.getMails());
            MailBase.getMailsFromBase().forEach(sender -> MailBase.getMailStorage().add(sender.getMail()));
        }

/**
 * Грузим данные в статический класс из config.json и открываем Gallerey-view.fxml
 * */
        SettingsLoader.setLoad(pathField.getText() + "/" + "config.json");
        Parent root = FXMLLoader.load(getClass().getResource("Gallery-view.fxml"));
        StageContainer.setStage((Stage) ((Node) click.getSource()).getScene().getWindow());
        Rectangle2D r = Screen.getPrimary().getBounds();
        StageContainer.getStage().setWidth(r.getWidth());
        StageContainer.getStage().setHeight(r.getHeight());
        StageContainer.getStage().centerOnScreen();
        StageContainer.getStage().getScene().setRoot(root);
        StageContainer.getStage().setFullScreenExitHint("");
        StageContainer.getStage().setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        StageContainer.getStage().setFullScreen(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventDate.setValue(LocalDate.now());
    }

    private void findPic(CheckBox bgImageCheck, String title) {
        colorNumber.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (Objects.nonNull(selectedFile)) {
            try {
                imageForBackGround = new Image(new FileInputStream(selectedFile));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            bgImageCheck.setSelected(true);
            bgImageCheck.setDisable(false);
        }
    }

    private boolean checkTextFields() {
        return EmptyChecker.isObjectListValid(List.of(
                password.getText(),
                login.getText(),
                subject.getText(),
                text.getText(),
                pathField.getText()));
    }

    private boolean checkFileFields() {
        return EmptyChecker.isObjectListValid(List.of(
                pathSettings.getText(),
                eventDate.getEditor().getText(),
                eventText.getText()));
    }

    private boolean checkAllFields() {
        return checkTextFields()
                && EmptyChecker.isStringListValid(List.of(
                eventText.getText(),
                eventDate.getEditor().getText()));
    }

}
