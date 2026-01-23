module org.team2.roktokhoj {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires com.gluonhq.maps;
    requires java.net.http;
    requires com.google.gson;
    requires jdk.jshell;

    exports org.team2.roktokhoj;
    exports org.team2.roktokhoj.views;
    exports org.team2.roktokhoj.models;
    exports org.team2.roktokhoj.models.map;
    exports org.team2.roktokhoj.components;

    opens org.team2.roktokhoj to javafx.fxml;
    opens org.team2.roktokhoj.views to javafx.fxml;
    opens org.team2.roktokhoj.components to javafx.fxml;

    opens org.team2.roktokhoj.models to com.google.gson;
    opens org.team2.roktokhoj.models.map to com.google.gson;
}