package uniquindio.edu.co.cinemauq.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import uniquindio.edu.co.cinemauq.model.Cinema;
import uniquindio.edu.co.cinemauq.model.Empleado;
import uniquindio.edu.co.cinemauq.model.exceptions.CinemaException;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtCodigoEmpleado;

    @FXML
    private Button btnIngresar;

    // Instancia de la fachada principal del cine
    private Cinema cinema = new Cinema();

    @FXML
    void onIngresarClick(ActionEvent event) {
        try {
            String textoCodigo = txtCodigoEmpleado.getText().trim();
            if (textoCodigo.isEmpty()) {
                mostrarAlerta("Error", "Por favor ingrese un código de empleado.", Alert.AlertType.WARNING);
                return;
            }

            int codigo = Integer.parseInt(textoCodigo);
            Empleado empleado = cinema.buscarEmpleado(codigo);

            if (empleado != null) {
                // 1. Validar que la ruta del FXML del dashboard exista
                java.net.URL archivoFXML = getClass().getResource("/uniquindio/edu/co/cinemauq/dashboard.view.fxml");
                if (archivoFXML == null) {
                    mostrarAlerta("Error de Ruta", "No se encontró el archivo 'dashboard.view.fxml' en la carpeta resources.", Alert.AlertType.ERROR);
                    return;
                }

                // 2. Mostrar alerta de bienvenida
                mostrarAlerta("Éxito", "Bienvenido, " + empleado.getNombre() + " (" + empleado.getCargo() + ")", Alert.AlertType.INFORMATION);

                // 3. Cargar el Dashboard
                FXMLLoader loader = new FXMLLoader(archivoFXML);
                Parent root = loader.load();

                // 4. Pasar los datos del empleado Y la instancia de cinema al controlador del Dashboard
                DashboardController controller = loader.getController();
                controller.initData(empleado, cinema); // <--- ¡Aquí está la corrección clave!

                // 5. Obtener el Stage (ventana) actual desde el botón de login
                Stage stage = (Stage) btnIngresar.getScene().getWindow();

                // 6. GARANTÍA DE TAMAÑO: Creamos la escena heredando el ancho y alto actual de la pantalla
                Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
                stage.setScene(scene);
                stage.setTitle("CinemaUQ - Panel Principal");

                // 7. Forzar el estado maximizado para asegurar que cubra toda la pantalla
                stage.setMaximized(true);
                stage.show();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "El código debe ser un número entero válido.", Alert.AlertType.ERROR);
        } catch (CinemaException e) {
            mostrarAlerta("Acceso Denegado", e.getMessage(), Alert.AlertType.ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Carga", "Ocurrió un error al abrir el panel principal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}