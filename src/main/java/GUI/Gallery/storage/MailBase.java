package GUI.Gallery.storage;

import GUI.Gallery.data.entity.Sender;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.TreeSet;

public class MailBase {

    @Getter
    @Setter
    private static ArrayList<Sender> mailsFromBase = new ArrayList<>();

    @Getter
    @Setter
    private static TreeSet<String> mailStorage = new TreeSet<>();

    private MailBase() {
    }
}
