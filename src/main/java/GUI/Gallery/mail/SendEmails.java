package GUI.Gallery.mail;

import GUI.Gallery.singleton.SettingsLoader;
import GUI.Gallery.data.dao.BaseDAO;
import GUI.Gallery.data.entity.Sender;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor
public class SendEmails {

    public String send(List<Sender> senderList, String subject, String text) {
        final var status = new AtomicReference<>(StringUtils.EMPTY);
        senderList.forEach(sender -> {
            final var mailTO = sender.getMail();
            final var attachedPath = sender.getPath();
//          если вложение > 25Mb, то abort, добавив запись в базу
            if (new File(sender.getPath()).length() > 25000000) {
                BaseDAO.getInstance().updateSenderStatus("too big", sender);
                status.set(mailTO + ": " + "file is too big");
            } else {
                final var sslSender = new GmailSender(SettingsLoader.getInstance().getLogin(), SettingsLoader.getInstance().getPassword());
                try {
                    final var statusSender = sslSender.send(subject, text, attachedPath, mailTO);
                    BaseDAO.getInstance().updateSenderStatus(statusSender, sender);
                    status.set(mailTO + ": " + statusSender);
                } catch (Exception e) {
                    System.out.println("MAIN ERROR: " + e.getMessage() + " ");
                    e.printStackTrace();
                    if (e.getMessage().equals("Unknown SMTP host: smtp.gmail.com")) {
                        BaseDAO.getInstance().updateSenderStatus("NO INTERNET", sender);
                        status.set(mailTO + ": " + "no internet connection");
                    } else {
                        BaseDAO.getInstance().updateSenderStatus("ERROR", sender);
                        status.set(mailTO + ": " + "error");
                    }
                }
            }
        });

        return status.get();
    }

}
