package GUI.Gallery;

import GUI.Gallery.mail.SendToSender;
import GUI.Gallery.data.connections.BaseConnection;
import GUI.Gallery.data.entities.Event;
import GUI.Gallery.data.entities.Sender;
import GUI.Gallery.setUp.SettingsLoader;
import GUI.Gallery.storage.LinkTransfer;
import GUI.Gallery.storage.MailBase;
import GUI.Gallery.storage.StageConteiner;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
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
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyBoardController implements Initializable {

    public TextField mailField;
    public String finalText = "";
    public Button one;
    public Button two;
    public Button three;
    public Button four;
    public Button five;
    public Button six;
    public Button seven;
    public Button eight;
    public Button nine;
    public Button zero;
    public Button tire;
    public Button del;
    public Button q;
    public Button w;
    public Button e;
    public Button r;
    public Button t;
    public Button y;
    public Button u;
    public Button i;
    public Button o;
    public Button p;
    public Button a;
    public Button s;
    public Button d;
    public Button f;
    public Button g;
    public Button h;
    public Button j;
    public Button k;
    public Button l;
    public Button z;
    public Button x;

    public Button c;
    public Button v;
    public Button b;
    public Button n;
    public Button m;
    public Button downTire;
    public Button clear;
    public Button end;
    public Button space;
    public Button ru;
    public Button com;
    public Button send;
    public Button point;
    public Button dog;
    public TilePane tileMails;
    public Label sendRezultLabel;
    public VBox back;

    String textForStatus = "";
    Timeline statusCheck = new Timeline(
            new KeyFrame(Duration.millis(1000),
                    event -> {
                        if (!(textForStatus.equals(""))) {
                            sendRezultLabel.setText(textForStatus);
                            sendRezultLabel.setVisible(true);
                            textForStatus = "";
                        }
                    }));

    public void searchMail() {
        if (textForStatus != "") {
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
            MailBase.mailStorage.forEach(mail -> {
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
        StageConteiner.stage.centerOnScreen();
        StageConteiner.stage.getScene().setRoot(root);
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

    public void endAction(ActionEvent event) throws IOException {
        goToMedia();
    }

    public void sendAction(ActionEvent event) {
        statusCheck.play();
        String imagePath = SettingsLoader.getSourseFolder() + "/" + LinkTransfer.link;
        String mail = mailField.getText();
        Pattern pattern = Pattern.compile("^.*@.*\\..*$");
        Matcher matcher = pattern.matcher(mail);
        if (!matcher.find()) {
            mailField.setStyle("-fx-text-fill: red;");
        } else {
            MailBase.mailStorage.add(mail.toLowerCase());
            ArrayList<Event> eventList = new ArrayList<>(BaseConnection.getEvents());
            eventList.forEach(e -> {
                if (e.getIdEvent() == SetupWindowController.IdEvent) {
                    BaseConnection.setSender(mail.toLowerCase(), imagePath, e);
                }
            });
            mailField.setText("");

            Thread thread = new Thread(() -> {
                String status = "";
                List<Sender> senderList = BaseConnection.getSender();
                if (senderList.size() != 0) {
                    try {
                        status = new SendToSender().sending(senderList);
                    } catch (IOException | ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                textForStatus = status;
            });
            thread.start();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sendRezultLabel.setText("");
        Background background = new Background(new BackgroundImage(ImageMediaController.image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        back.setBackground(background);
        statusCheck.setCycleCount(Timeline.INDEFINITE);
    }
}

