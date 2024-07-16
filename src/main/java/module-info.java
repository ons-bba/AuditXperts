module esprit.experts {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;
    requires java.sql.rowset;
    requires org.apache.pdfbox;
    requires jbcrypt;
    requires mysql.connector.j;
    requires java.mail;

    opens esprit.experts to javafx.fxml;
    exports esprit.experts;
    exports esprit.experts.controllers;
    opens esprit.experts.controllers to javafx.fxml;
    opens esprit.experts.entities to javafx.base;

}