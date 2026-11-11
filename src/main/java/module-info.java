module uniquindio.edu.co.cinemauq {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;


    opens uniquindio.edu.co.cinemauq to javafx.fxml;
    exports uniquindio.edu.co.cinemauq;
    exports uniquindio.edu.co.cinemauq.controller;
    opens uniquindio.edu.co.cinemauq.controller to javafx.fxml;
}