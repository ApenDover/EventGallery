package GUI.Gallery.storage;

import GUI.Gallery.data.entity.Sender;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeSet;

@Getter
@Setter
public class MailBase {

    private static MailBase instance;

    private ArrayList<Sender> mailsFromBase = new ArrayList<>();

    private TreeSet<String> mailStorage = new TreeSet<>();

    private MailBase() {
    }

    public static MailBase getInstance() {
        if (Objects.isNull(instance)) {
            instance = new MailBase();
        }
        return instance;
    }


}
