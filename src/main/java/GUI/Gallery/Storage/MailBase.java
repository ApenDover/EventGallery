package GUI.Gallery.Storage;

import GUI.Gallery.MySQL.Entities.Sender;

import java.util.ArrayList;
import java.util.TreeSet;

public class MailBase {
    public static ArrayList<Sender> mailsFromBase = new ArrayList<>();
    public static TreeSet<String> mailStorage = new TreeSet<>();
}
