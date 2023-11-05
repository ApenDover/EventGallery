package gui.gallery;

import gui.gallery.data.dao.BaseDAO;
import gui.gallery.data.entity.Company;
import gui.gallery.data.entity.Event;
import gui.gallery.runnable.ScalePreviewImagesProcess;
import gui.gallery.singleton.SettingsLoader;
import gui.gallery.singleton.MailBase;
import gui.gallery.singleton.StageContainer;
import gui.gallery.utils.EmptyChecker;
import gui.gallery.utils.FileStringConverter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
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
    private TextField subject;

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

    private final ObservableList<String> companyNames = FXCollections.observableArrayList();

    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    private static Stage stage;

    @Getter
    private static boolean resultBgImageCheck = false;

    @Getter
    private static boolean resultBgImageCheck2 = false;

    @Getter
    private static String red;

    @Getter
    private static String green;

    @Getter
    private static String blue;

    @Getter
    private static int idEvent = 0;

    @Getter
    private static Image imageForBackGround;

    @Getter
    private static Image imageForBackGround2;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private static final String CONFIG_JSON = "config.json";

    private static final int PATH_LENGTH = CONFIG_JSON.length() + 1;

    private static final int COLOR_LENGTH_SHARP = 7;

    private static final int COLOR_LENGTH = COLOR_LENGTH_SHARP - 1;

    /**
     * Настройки
     */
    @FXML
    private void settingsPath() {
        Platform.runLater(() -> {
            final var fileChooser = new FileChooser();
            fileChooser.setTitle("Open config.json File");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON", CONFIG_JSON));
            final var selectedFile = fileChooser.showOpenDialog(stage);
            if (Objects.nonNull(selectedFile)) {
                pathSettings.setText(selectedFile.getAbsolutePath());
                if (StringUtils.isNotEmpty(pathSettings.getText())) {
                    SettingsLoader.getInstance().loadSettingsFromJsonFile(pathSettings.getText());
                    String ap = selectedFile.getAbsolutePath();
                    pathField.setText(ap.substring(0, ap.length() - PATH_LENGTH));
                    pathField.setStyle("-fx-faint-focus-color: #00СС22;");
                    pathField.requestFocus();
                    login.setText(SettingsLoader.getInstance().getLogin());
                    password.setText(SettingsLoader.getInstance().getPassword());
                    subject.setText(SettingsLoader.getInstance().getSubject());
                    text.setText(SettingsLoader.getInstance().getText());
                    loginDB.setText(SettingsLoader.getInstance().getDbLogin());
                    passwordDB.setText(SettingsLoader.getInstance().getDbPassword());
                    final var check = (EmptyChecker.isObjectListValid(List.of(companyListView.getSelectionModel(),
                            companyListView.getSelectionModel().getSelectedItem(),
                            allEvents.getSelectionModel(), allEvents.getSelectionModel().getSelectedItem()))
                            || (Objects.nonNull(companyListView.getSelectionModel().getSelectedItem())
                            && EmptyChecker.isStringListValid(List.of(eventText.getText(),
                            eventDate.getEditor().getText()))));
                    startButton.disableProperty().set(!check);
                }
            }
        });
    }

    /**
     * DataBase
     */
    @FXML
    public void connectToDb() {
        companyListView.getItems().clear();
        connectLabel.setText(BaseDAO.getInstance().openConnection(loginDB.getText(), passwordDB.getText()));
        connectLabel.setVisible(true);
        companyNames.addAll(BaseDAO.getInstance().getCompany().stream().map(Company::getName).toList());
        companyListView.setItems(companyNames);
    }

    /**
     * Галерея
     */
    @FXML
    private void findPath() {
        File selectedFile = directoryChooser.showDialog(stage);
        if (Objects.nonNull(selectedFile)) {
            pathField.setText(selectedFile.getAbsolutePath());
            final var check = checkAllFields() || EmptyChecker.isStringListValid(List.of(pathSettings.getText(),
                    eventDate.getEditor().getText(), eventText.getText()))
                    || (StringUtils.isNotBlank(pathSettings.getText())
                    && Objects.nonNull(allEvents.getSelectionModel().getSelectedItem()));
            startButton.disableProperty().set(!check);
        }
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
        findPicture(bgImageCheck, "background gallery Image");
    }

    @FXML
    private void findPicForBackground2(ActionEvent event) {
        findPicture(bgImageCheck2, "background sender Image");
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
            BaseDAO.getInstance().setCompany(companyField.getText());
            companyNames.add(companyField.getText());
            companyField.setText(StringUtils.EMPTY);
            companyField.setFocusTraversable(false);
            companyListView.setItems(companyNames);
        }
    }

    @FXML
    private void removeCompany() {
        BaseDAO.getInstance().removeCompany(companyField.getText());
        companyNames.remove(companyField.getText());
        companyField.setText(StringUtils.EMPTY);
        companyField.setFocusTraversable(false);
        companyListView.setItems(companyNames);
        allEvents.getItems().clear();
    }

    @FXML
    private void getEventsFromListView() {
        try {
            String choose = companyListView.getFocusModel().getFocusedItem();
            final var now = new ArrayList<>(BaseDAO.getInstance().getEventsFromCompany(choose));
            ObservableList<String> events = FXCollections.observableArrayList();
            now.forEach(event -> events.add(event.getDate().toString() + " : " + event.getDescription()));
            companyListView.getFocusModel().getFocusedItem();
            allEvents.setItems(events);
            final var check = (checkAllFields() || checkFileFields() || StringUtils.isNotBlank(pathSettings.getText())
                    || Objects.nonNull(allEvents.getSelectionModel().getSelectedItem())
                    || EmptyChecker.isStringListValid(List.of(eventDate.getEditor().getText(), eventText.getText())));
            startButton.disableProperty().set(!check);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

    }

    @FXML
    private void datePickerORTextAreaClick() {
        allEvents.getSelectionModel().clearSelection();
    }

    @FXML
    private void fieldClick() {
        final var check = (checkAllFields() && Objects.nonNull(companyListView.getSelectionModel().getSelectedItem()))
                || (checkTextFields() && isCompanyListViewAllEventPersist());
        startButton.disableProperty().set(!check);
    }

    private boolean isCompanyListViewAllEventPersist() {
        return EmptyChecker.isObjectListValid(List.of(companyListView.getSelectionModel().getSelectedItem(),
                allEvents.getSelectionModel().getSelectedItem()));
    }

    @FXML
    private void allEventsClick() {
        if (Objects.nonNull(allEvents.getSelectionModel().getSelectedItem())) {
            eventDate.getEditor().clear();
            eventText.setText(StringUtils.EMPTY);
            fieldClick();
        }
    }

    @FXML
    private void openDel() {
        remButton.setDisable(false);
    }

    @FXML
    private void start(MouseEvent click) {
        resultBgImageCheck = bgImageCheck.isSelected();
        resultBgImageCheck2 = bgImageCheck2.isSelected();

        if (colorNumber.getText().length() == COLOR_LENGTH) {
            ImageMediaController.setColorNumber(colorNumber.getText());
            GalleryController.setColorNumber(colorNumber.getText());
            red = String.valueOf(Integer.parseInt(colorNumber.getText().substring(0, 2), 16));
            green = String.valueOf(Integer.parseInt(colorNumber.getText().substring(2, 4), 16));
            blue = String.valueOf(Integer.parseInt(colorNumber.getText().substring(4, COLOR_LENGTH), 16));
        }

        if (colorNumber.getText().length() == COLOR_LENGTH_SHARP) {
            ImageMediaController.setColorNumber(colorNumber.getText());
            GalleryController.setColorNumber(colorNumber.getText());
            red = String.valueOf(Integer.parseInt(colorNumber.getText().substring(1, 3), 16));
            green = String.valueOf(Integer.parseInt(colorNumber.getText().substring(3, 5), 16));
            blue = String.valueOf(Integer.parseInt(colorNumber.getText().substring(5, COLOR_LENGTH_SHARP), 16));
        }
        /**
         * Запишем настройки сортировки в SettingsLoader
         * */
        SettingsLoader.getInstance().setByAddTime(byAddTime.isSelected());
        SettingsLoader.getInstance().setByName(byName.isSelected());
        SettingsLoader.getInstance().setNewUp(newUp.isSelected());
        SettingsLoader.getInstance().setNewDown(newDown.isSelected());

        /**
         * Провери пустая ли папка, если нет - запустим ресайзер (а если пустая?)
         * */

        /** Записываем настройки в config.json */
        SettingsLoader.getInstance().saveSettingsToJsonFile(login.getText(), password.getText(), subject.getText(), text.getText(),
                pathField.getText(), "300", loginDB.getText(), passwordDB.getText());


        /**
         * Заносим мероприятие в БД или определяемся к какому существующему мероприятию будут относиться данные
         * */
        if (StringUtils.isNotBlank(eventDate.getEditor().getText()) && StringUtils.isNotBlank(eventText.getText())) {
            //создаем новое мероприятие
            Date date = Date.from(eventDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            BaseDAO.getInstance().setEvent(date, eventText.getText(), companyListView.getFocusModel().getFocusedItem());
            ArrayList<Event> eventArrayList = new ArrayList<>(BaseDAO.getInstance().getEvents());
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
            Date utilDate;
            try {
                utilDate = df.parse(pole[0]);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            ArrayList<Event> eventArrayList = new ArrayList<>(BaseDAO.getInstance().getEvents());
            eventArrayList.forEach(event -> {
                if ((event.getDate().equals(sqlDate)) && event.getDescription().equals(description)) {
                    atomicIdEvent.set(event.getIdEvent());
                }
            });
            idEvent = atomicIdEvent.get();
            MailBase.getInstance().getMailsFromBase().addAll(BaseDAO.getInstance().getMails());
            MailBase.getInstance().getMailsFromBase().forEach(sender -> MailBase.getInstance().getMailStorage().add(sender.getMail()));
        }

/**
 * Грузим данные в статический класс из config.json и открываем Gallerey-view.fxml
 * */
        SettingsLoader.getInstance().loadSettingsFromJsonFile(FileStringConverter.getFilePath(pathField.getText(), "config", "json"));
        Parent root = OpenWindow.open("gallery-view.fxml");
        StageContainer.getInstance().setStage((Stage) ((Node) click.getSource()).getScene().getWindow());
        Rectangle2D r = Screen.getPrimary().getBounds();
        StageContainer.getInstance().getStage().setWidth(r.getWidth());
        StageContainer.getInstance().getStage().setHeight(r.getHeight());
        StageContainer.getInstance().getStage().centerOnScreen();
        StageContainer.getInstance().getStage().getScene().setRoot(root);
        StageContainer.getInstance().getStage().setFullScreenExitHint(StringUtils.EMPTY);
        StageContainer.getInstance().getStage().setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        StageContainer.getInstance().getStage().setFullScreen(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventDate.setValue(LocalDate.now());
    }

    private void findPicture(CheckBox backGroundImageCheck, String title) {
        colorNumber.setText(StringUtils.EMPTY);
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
            backGroundImageCheck.setSelected(true);
            backGroundImageCheck.setDisable(false);
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
