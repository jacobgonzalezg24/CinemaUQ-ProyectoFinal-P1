package uniquindio.edu.co.cinemauq; // o view según tu paquete

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import java.io.IOException;

public class App extends Application {



    @Override
    public void start(Stage stage) throws IOException {
        // Cargamos la fuente y verificamos sus datos internos
        Font myFont = Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"), 14);

        if (myFont != null) {
            System.out.println("✅ ¡Fuente cargada correctamente!");
            System.out.println("📌 Nombre exacto para el CSS (Family): " + myFont.getFamily());
        } else {
            System.out.println("❌ Error crítico: La ruta del archivo de fuente no es accesible.");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/uniquindio/edu/co/cinemauq/login.view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("CinemaUQ - Iniciar Sesión");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

