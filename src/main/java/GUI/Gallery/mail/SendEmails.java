package GUI.Gallery.mail;

import GUI.Gallery.setUp.SettingsLoader;
import GUI.Gallery.data.connections.BaseConnection;
import GUI.Gallery.data.entity.Sender;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SendEmails {

    public static String send(List<Sender> senderList) {
        AtomicReference<String> status = new AtomicReference<>("");
        String subject = SettingsLoader.getSubject();
        String text = SettingsLoader.getText();
        senderList.forEach(sender -> {
            String mailTO = sender.getMail();
            String attachedPath = sender.getPath();
//          если вложение > 25Mb, то abort, добавив запись в базу
            if ((new File(sender.getPath()).length()) > 25000000) {
                BaseConnection.updateSenderStatus("too big", sender);
                status.set(mailTO + ": " + "file is too big");
            } else {
                GmailSender sslSender = new GmailSender(SettingsLoader.getLogin(), SettingsLoader.getPassword());
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
