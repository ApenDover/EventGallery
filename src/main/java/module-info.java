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
    requires javacv;
    requires javacpp;
    requires imgscalr.lib;
    requires org.apache.commons.lang3;
    requires static lombok;

    opens GUI.Gallery to javafx.fxml;
    opens GUI.Gallery.data.entity to org.hibernate.orm.core;
    exports GUI.Gallery;
    exports GUI.Gallery.storage;
    exports GUI.Gallery.data.entity;
    opens GUI.Gallery.storage to javafx.fxml;
    exports GUI.Gallery.mail;
    opens GUI.Gallery.mail to javafx.fxml;
}