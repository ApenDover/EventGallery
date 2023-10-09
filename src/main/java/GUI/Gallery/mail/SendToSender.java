package GUI.Gallery.mail;

import GUI.Gallery.setUp.SettingsLoader;
import GUI.Gallery.data.connections.BaseConnection;
import GUI.Gallery.data.entities.Sender;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SendToSender {

    public String sending(List<Sender> senderList) throws IOException, ParseException {
        AtomicReference<String> status = new AtomicReference<>("");

        String subject = SettingsLoader.getSubject();
        String text = SettingsLoader.getText();

        // создаем очередь на отправку письма

        //      Отправляем письмо на почту, заполняя статус отправки
        senderList.forEach(sender -> {
            String mailTO = sender.getMail();
            String attachedPath = sender.getPath();
//          если вложение > 25Mb, то abort, добавив запись в базу
            if ((new File(sender.getPath()).length()) > 25000000) {
                BaseConnection.updateSenderStatus("too big", sender);
                status.set(mailTO + ": " + "file is too big");
            } else {
                SSLGmailSender sslSender = new SSLGmailSender(SettingsLoader.getLogin(), SettingsLoader.getPassword());
                try {
                    String statusSender = sslSender.send(subject, text, attachedPath, mailTO);
                    BaseConnection.updateSenderStatus(statusSender, sender);
                    status.set(mailTO + ": " + statusSender);
                } catch (Exception e) {
                    System.out.println("MAIN ERROR: " + e.getMessage() + " ");
                    e.printStackTrace();
                    if (e.getMessage().equals("Unknown SMTP host: smtp.gmail.com")) {
                        BaseConnection.updateSenderStatus("NO INTERNET", sender);
                        status.set(mailTO + ": " + "no internet connection");
                    } else {
                        BaseConnection.updateSenderStatus("ERROR", sender);
                        status.set(mailTO + ": " + "error");
                    }
                }
            }
        });

        return status.get();
    }

}
