package GUI.Gallery.runnable;

import GUI.Gallery.data.connections.BaseConnection;
import GUI.Gallery.data.entity.Sender;
import GUI.Gallery.mail.SendEmails;

import java.util.List;
import java.util.concurrent.Callable;

public class SendMailProcess implements Callable<String> {

    public String call() {
        String status = "";
        List<Sender> senderList = BaseConnection.getSender();
        if (!senderList.isEmpty()) {
            status = SendEmails.send(senderList);
        }
        return status;
    }
}
