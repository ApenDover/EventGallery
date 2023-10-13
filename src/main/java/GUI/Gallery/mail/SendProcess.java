package GUI.Gallery.mail;

import GUI.Gallery.data.connections.BaseConnection;
import GUI.Gallery.data.entity.Sender;

import java.util.List;
import java.util.concurrent.Callable;

public class SendProcess implements Callable<String> {

    public String call() {
        String status = "";
        List<Sender> senderList = BaseConnection.getSender();
        if (!senderList.isEmpty()) {
            status = SendEmails.send(senderList);
        }
        return status;
    }
}
