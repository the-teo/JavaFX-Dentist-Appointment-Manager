module gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires jakarta.xml.bind;
    requires jakarta.activation;
    requires com.google.gson;

    opens domain to com.google.gson, jakarta.xml.bind;
    opens repository to jakarta.xml.bind;

    opens gui to javafx.fxml;
    opens main to javafx.fxml;

    exports gui;
    exports main;
    exports controller;
    opens controller to javafx.fxml;
}

