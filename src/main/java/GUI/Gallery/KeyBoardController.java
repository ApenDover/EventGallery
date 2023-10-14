package GUI.Gallery;

import GUI.Gallery.data.connections.BaseConnection;
import GUI.Gallery.data.entity.Event;
import GUI.Gallery.runnable.SendMailProcess;
import GUI.Gallery.setUp.SettingsLoader;
import GUI.Gallery.storage.LinkTransfer;
import GUI.Gallery.storage.MailBase;
import GUI.Gallery.storage.StageContainer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyBoardController implements Initializable {

    @FXML
    private TextField mailField;

    @FXML
    private Button one;

    @FXML
    private Button two;

    @FXML
    private Button three;

    @FXML
    private Button four;

    @FXML
    private Button five;

    @FXML
    private Button six;

    @FXML
    private Button seven;

    @FXML
    private Button eight;

    @FXML
    private Button nine;

    @FXML
    private Button zero;

    @FXML
    private Button tire;

    @FXML
    private Button del;

    @FXML
    private Button q;

    @FXML
    private Button w;

    @FXML
    private Button e;

    @FXML
    private Button r;

    @FXML
    private Button t;

    @FXML
    private Button y;

    @FXML
    private Button u;

    @FXML
    private Button i;

    @FXML
    private Button o;

    @FXML
    private Button p;

    @FXML
    private Button a;

    @FXML
    private Button s;

    @FXML
    private Button d;

    @FXML
    private Button f;

    @FXML
    private Button g;

    @FXML
    private Button h;

    @FXML
    private Button j;

    @FXML
    private Button k;

    @FXML
    private Button l;

    @FXML
    private Button z;

    @FXML
    private Button x;

    @FXML
    private Button c;

    @FXML
    private Button v;

    @FXML
    private Button b;

    @FXML
    private Button n;

    @FXML
    private Button m;

    @FXML
    private Button downTire;

    @FXML
    private Button clear;

    @FXML
    private Button end;

    @FXML
    private Button space;

    @FXML
    private Button ru;

    @FXML
    private Button com;

    @FXML
    private Button send;

    @FXML
    private Button point;

    @FXML
    private Button dog;

    @FXML
    private TilePane tileMails;

    @FXML
    private Label sendRezultLabel;

    @FXML
    private VBox back;

    private String finalText = "";

    private final SendMailProcess sendMailProcess = new SendMailProcess();

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Getter
    private String textForStatus = "";

    private Timeline statusCheck = new Timeline(
            new KeyFrame(Duration.millis(1000),
                    event -> {
                        if (StringUtils.isNotBlank(textForStatus)) {
                            sendRezultLabel.setText(textForStatus);
                            sendRezultLabel.setVisible(true);
                            textForStatus = "";
                        }
                    }));

    public void searchMail() {
        if (StringUtils.isNotBlank(textForStatus)) {
            statusCheck.stop();
        }
        textForStatus = "";
        sendRezultLabel.setVisible(false);
        mailField.setStyle("-fx-text-fill: black;");
        tileMails.getChildren().clear();
        tileMails.requestLayout();
        if (finalText.length() > 1) {
//        эта штука смотрит введенный текст, сопостовляет в массиве, находит подходящие и далее создает кнопки
            tileMails.setHgap(10);
            tileMails.setVgap(10);
            ArrayList<String> serched = new ArrayList<>();
            MailBase.getMailStorage().forEach(mail -> {
                if (mail.startsWith(finalText)) {
                    serched.add(mail);
                }
            });

            serched.forEach(mailSearched -> {
                Button button = new Button(mailSearched);
                button.setOnAction(event -> {
                    mailField.setText(mailSearched);
                    finalText = mailField.getText();
                });
                button.setMinHeight(40);
                button.setMinWidth(60);
                tileMails.getChildren().add(button);
            });
            tileMails.requestLayout();
        }
    }

    public void goToMedia() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ImageMedia-view.fxml"));
        StageContainer.getStage().centerOnScreen();
        StageContainer.getStage().getScene().setRoot(root);
    }

    /**
     * Клавиатура
     */
    public void oneAction(ActionEvent event) {
        finalText += one.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void twoAction(ActionEvent event) {
        finalText += two.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void threeAction(ActionEvent event) {
        finalText += three.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void fourAction(ActionEvent event) {
        finalText += four.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void fiveAction(ActionEvent event) {
        finalText += five.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void sixAction(ActionEvent event) {
        finalText += six.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void sevenAction(ActionEvent event) {
        finalText += seven.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void eightAction(ActionEvent event) {
        finalText += eight.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void nineAction(ActionEvent event) {
        finalText += nine.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void zeroAction(ActionEvent event) {
        finalText += zero.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void spaceAction(ActionEvent event) {
//        finalTextField += " ";
//        mailField.setText(finalTextField);
    }

    public void qAction(ActionEvent event) {
        finalText += q.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void wAction(ActionEvent event) {
        finalText += w.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void eAction(ActionEvent event) {
        finalText += e.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void rAction(ActionEvent event) {
        finalText += r.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void tAction(ActionEvent event) {
        finalText += t.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();

    }

    public void yAction(ActionEvent event) {
        finalText += y.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void uAction(ActionEvent event) {
        finalText += u.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void iAction(ActionEvent event) {
        finalText += i.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void oAction(ActionEvent event) {
        finalText += o.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void pAction(ActionEvent event) {
        finalText += p.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void aAction(ActionEvent event) {
        finalText += a.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void sAction(ActionEvent event) {
        finalText += s.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void dAction(ActionEvent event) {
        finalText += d.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void fAction(ActionEvent event) {
        finalText += f.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void gAction(ActionEvent event) {
        finalText += g.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void hAction(ActionEvent event) {
        finalText += h.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void jAction(ActionEvent event) {
        finalText += j.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void kAction(ActionEvent event) {
        finalText += k.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void lAction(ActionEvent event) {
        finalText += l.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void zAction(ActionEvent event) {
        finalText += z.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void xAction(ActionEvent event) {
        finalText += x.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void cAction(ActionEvent event) {
        finalText += c.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void vAction(ActionEvent event) {
        finalText += v.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void bAction(ActionEvent event) {
        finalText += b.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void nAction(ActionEvent event) {
        finalText += n.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void mAction(ActionEvent event) {
        finalText += m.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void tireAction(ActionEvent event) {
        finalText += tire.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void delAction(ActionEvent event) {
        if (finalText.length() > 0) {
            finalText = finalText.substring(0, finalText.length() - 1);
            mailField.setText(finalText);
            searchMail();
        }
    }

    public void downTireAction(ActionEvent event) {
        finalText += "_";
        mailField.setText(finalText);
        searchMail();
    }

    public void clearAction(ActionEvent event) {
        finalText = "";
        mailField.setText(finalText);
        searchMail();
    }

    public void pointAction(ActionEvent event) {
        finalText += point.getText().toLowerCase();
        mailField.setText(finalText);
        searchMail();
    }

    public void dogAction(ActionEvent event) {
        finalText += "@";
        mailField.setText(finalText);
        searchMail();
    }

    public void ruAction(ActionEvent event) {
        finalText += ".ru";
        mailField.setText(finalText);
        searchMail();
    }

    public void comAction(ActionEvent event) {
        finalText += ".com";
        mailField.setText(finalText);
        searchMail();
    }

    public void endAction(ActionEvent event) {
        try {
            goToMedia();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void sendAction(ActionEvent actionEvent) {
        statusCheck.play();
        String imagePath = SettingsLoader.getSourceFolder() + "/" + LinkTransfer.getLink();
        String mail = mailField.getText();
        Pattern pattern = Pattern.compile("^.*@.*\\..*$");
        Matcher matcher = pattern.matcher(mail);
        if (!matcher.find()) {
            mailField.setStyle("-fx-text-fill: red;");
        } else {
            MailBase.getMailStorage().add(mail.toLowerCase());
            Event event = BaseConnection.getEventById(SetupWindowController.getIdEvent());
            BaseConnection.setSender(mail.toLowerCase(), imagePath, event);
            mailField.clear();
            executor.execute(() -> textForStatus = sendMailProcess.call());
            executor.shutdown();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sendRezultLabel.setText("");
        Background background = new Background(new BackgroundImage(ImageMediaController.getImage(), BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        back.setBackground(background);
        statusCheck.setCycleCount(Animation.INDEFINITE);
    }
}

