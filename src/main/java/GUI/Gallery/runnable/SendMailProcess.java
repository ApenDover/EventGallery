package GUI.Gallery.runnable;

import GUI.Gallery.data.dao.baseDAO;
import GUI.Gallery.data.entity.Sender;
import GUI.Gallery.mail.SendEmails;
import GUI.Gallery.setUp.SettingsLoader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;

@Component
public class SendMailProcess implements Callable<String> {

    public String call() {
        SendEmails sendEmails = new SendEmails();
        String status = StringUtils.EMPTY;
        List<Sender> senderList = baseDAO.getSender();
        if (!senderList.isEmpty()) {
            status = sendEmails.send(senderList, SettingsLoader.getSubject(), SettingsLoader.getText());
        }
        return status;
    }
}
