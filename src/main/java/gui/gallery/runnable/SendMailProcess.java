package gui.gallery.runnable;

import gui.gallery.data.dao.BaseDAO;
import gui.gallery.mail.SendEmails;
import gui.gallery.singleton.SettingsLoader;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Callable;

public class SendMailProcess implements Callable<String> {

    public String call() {
        final var sendEmails = new SendEmails();
        final var senderList = BaseDAO.getInstance().getSender();
        var status = StringUtils.EMPTY;
        if (!senderList.isEmpty()) {
            status = sendEmails.send(senderList, SettingsLoader.getInstance().getSubject(), SettingsLoader.getInstance().getText());
        }
        return status;
    }

}
