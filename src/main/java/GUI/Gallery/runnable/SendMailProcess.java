package GUI.Gallery.runnable;

import GUI.Gallery.data.dao.baseDAO;
import GUI.Gallery.data.entity.Sender;
import GUI.Gallery.mail.SendEmails;

import java.util.List;
import java.util.concurrent.Callable;

public class SendMailProcess implements Callable<String> {

    public String call() {
        String status = "";
        List<Sender> senderList = baseDAO.getSender();
        if (!senderList.isEmpty()) {
            status = SendEmails.send(senderList);
        }
        return status;
    }
}
