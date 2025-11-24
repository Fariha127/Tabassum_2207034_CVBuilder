module com.example.tabassum_2207034_cvbuilder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    
    // Database
    requires java.sql;

    opens com.example.tabassum_2207034_cvbuilder to javafx.fxml;
    exports com.example.tabassum_2207034_cvbuilder;
}