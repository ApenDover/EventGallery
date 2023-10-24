package GUI.Gallery.mail;

import GUI.Gallery.setUp.SettingsLoader;
import GUI.Gallery.data.dao.baseDAO;
import GUI.Gallery.data.entity.Sender;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor
public class SendEmails {

    public String send(List<Sender> senderList, String subject, String text) {
        AtomicReference<String> status = new AtomicReference<>(StringUtils.EMPTY);
        senderList.forEach(sender -> {
            String mailTO = sender.getMail();
            String attachedPath = sender.getPath();
//          если вложение > 25Mb, то abort, добавив запись в базу
            if ((new File(sender.getPath()).length()) > 25000000) {
                baseDAO.updateSenderStatus("too big", sender);
                status.set(mailTO + ": " + "file is too big");
            } else {
                GmailSender sslSender = new GmailSender(SettingsLoader.getLogin(), SettingsLoader.getPassword());
                try {
                    String statusSender = sslSender.send(subject, text, attachedPath, mailTO);
                    baseDAO.updateSenderStatus(statusSender, sender);
                    status.set(mailTO + ": " + statusSender);
                } catch (Exception e) {
                    System.out.println("MAIN ERROR: " + e.getMessage() + " ");
                    e.printStackTrace();
                    if (e.getMessage().equals("Unknown SMTP host: smtp.gmail.com")) {
                        baseDAO.updateSenderStatus("NO INTERNET", sender);
                        status.set(mailTO + ": " + "no internet connection");
                    } else {
                        baseDAO.updateSenderStatus("ERROR", sender);
                        status.set(mailTO + ": " + "error");
                    }
                }
            }
        });

        return status.get();
    }

}
