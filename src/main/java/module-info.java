module GUI.Gallery {
    requires java.desktop;
    requires java.xml.bind;
    requires json.simple;
    requires mail;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.sql;
    requires java.naming;
    requires jdk.jfr;
    requires org.bytedeco.javacpp;
    requires org.bytedeco.javacv;
    requires imgscalr.lib;
    requires org.apache.commons.lang3;
    requires static lombok;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.controls;

    exports GUI.Gallery;
    opens GUI.Gallery to javafx.fxml;
    opens GUI.Gallery.data.entity;
}