module GUI.Gallery {
    requires javafx.controls;
    requires java.desktop;
    requires java.xml.bind;
    requires javafx.fxml;
    requires json.simple;
    requires mail;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.sql;
    requires java.naming;
    requires org.controlsfx.controls;
    requires jdk.jfr;
    requires javafx.media;
    requires VideoJpgResized;

    opens GUI.Gallery to javafx.fxml;
    opens GUI.Gallery.MySQL.Entities to org.hibernate.orm.core;
    exports GUI.Gallery;
    exports GUI.Gallery.Storage;
    opens GUI.Gallery.Storage to javafx.fxml;
    exports GUI.Gallery.Mail;
    opens GUI.Gallery.Mail to javafx.fxml;
}