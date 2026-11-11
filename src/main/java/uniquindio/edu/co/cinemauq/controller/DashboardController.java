package uniquindio.edu.co.cinemauq.controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import uniquindio.edu.co.cinemauq.model.Cinema;
import uniquindio.edu.co.cinemauq.model.Empleado;

import java.io.IOException;

public class DashboardController {

    @FXML
    private BorderPane mainContainer;

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private Label lblRolUsuario;

    // Botones de la barra de navegación superior
    @FXML
    private Button btnHome;

    @FXML
    private Button btnPeliculas;

    @FXML
    private Button btnSalas;

    @FXML
    private Button btnFunciones;

    @FXML
    private Button btnTaquilla;

    @FXML
    private Button btnReportes;

    // Instancia única centralizada del modelo del cine y usuario activo
    private Cinema cinema;
    private Empleado empleadoActual;

    @FXML
    public void initialize() {
        // Asocia la referencia del controlador a la escena para navegaciones indirectas
        mainContainer.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setUserData(this);
            }
        });
    }

    /**
     * Inicializa el controlador con los datos del usuario autenticado y la instancia global de Cinema.
     */
    public void initData(Empleado empleado, Cinema cinema) {
        this.empleadoActual = empleado;
        this.cinema = cinema;

        if (empleado != null) {
            lblNombreUsuario.setText(empleado.getNombre());
            lblRolUsuario.setText(empleado.getCargo().toString());
        }

        // Cargar la vista inicial del Dashboard y marcar el botón Home como activo
        resaltarBotonActivo(btnHome);
        cargarVistaHome("/uniquindio/edu/co/cinemauq/dashboard-home.view.fxml");
    }

    // ==========================================
    // HANDLERS DE NAVEGACIÓN
    // ==========================================

    @FXML
    public void onIrHome(ActionEvent event) {
        resaltarBotonActivo(btnHome);
        cargarVistaHome("/uniquindio/edu/co/cinemauq/dashboard-home.view.fxml");
    }

    @FXML
    public void onIrDashboard(ActionEvent event) {
        onIrHome(event);
    }

    @FXML
    public void onIrPeliculas(ActionEvent event) {
        resaltarBotonActivo(btnPeliculas);
        cargarVistaConCinema("/uniquindio/edu/co/cinemauq/peliculas.view.fxml");
    }

    @FXML
    public void onIrSalas(ActionEvent event) {
        resaltarBotonActivo(btnSalas);
        cargarVistaConCinema("/uniquindio/edu/co/cinemauq/sala.view.fxml");
    }

    @FXML
    public void onIrFunciones(ActionEvent event) {
        resaltarBotonActivo(btnFunciones);
        cargarVistaConCinema("/uniquindio/edu/co/cinemauq/funciones.view.fxml");
    }

    @FXML
    public void onIrTaquilla(ActionEvent event) {
        resaltarBotonActivo(btnTaquilla);
        cargarVistaConCinema("/uniquindio/edu/co/cinemauq/taquilla.view.fxml");
    }

    @FXML
    public void onIrReportes(ActionEvent event) {
        resaltarBotonActivo(btnReportes);
        cargarVistaConCinema("/uniquindio/edu/co/cinemauq/reportes.view.fxml");
    }

    // ==========================================
    // LÓGICA DE ESTILOS Y NAVEGACIÓN
    // ==========================================

    /**
     * Remueve la clase CSS 'active' de todos los botones de la barra y la asigna al botón recibido.
     */
    private void resaltarBotonActivo(Button botonActivo) {
        Button[] botones = {btnHome, btnPeliculas, btnSalas, btnFunciones, btnTaquilla, btnReportes};

        for (Button b : botones) {
            if (b != null) {
                b.getStyleClass().remove("active");
            }
        }

        if (botonActivo != null) {
            botonActivo.getStyleClass().add("active");
        }
    }

    /**
     * Carga vistas estándar que no requieren inyección del modelo Cinema.
     */
    private void cargarVista(String fxmlPath) {
        try {
            if (mainContainer != null) {
                Parent vista = FXMLLoader.load(getClass().getResource(fxmlPath));
                transicionarVista(vista);
            } else {
                System.out.println("Error: El contenedor 'mainContainer' es nulo.");
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la vista: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Carga vistas inyectando la instancia única y centralizada del objeto Cinema en sus controladores.
     */
    private void cargarVistaConCinema(String fxmlPath) {
        try {
            if (mainContainer != null) {
                java.net.URL resource = getClass().getResource(fxmlPath.startsWith("/") ? fxmlPath : "/uniquindio/edu/co/cinemauq/" + fxmlPath);
                if (resource == null) {
                    System.out.println("No se encontró el archivo FXML: " + fxmlPath);
                    return;
                }

                FXMLLoader loader = new FXMLLoader(resource);
                Parent vista = loader.load();

                Object controller = loader.getController();

                if (controller instanceof PeliculasController) {
                    ((PeliculasController) controller).initCinema(cinema);
                } else if (controller instanceof SalaController) {
                    ((SalaController) controller).setCinema(cinema);
                } else if (controller instanceof TaquillaController) {
                    ((TaquillaController) controller).setCinema(cinema);
                } else if (controller instanceof FuncionController) {
                    ((FuncionController) controller).setCinema(cinema);
                } else if (controller instanceof ReportesController) {
                    ((ReportesController) controller).setCinema(cinema);
                }

                transicionarVista(vista);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la vista con Cinema: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Carga la vista principal del Dashboard Home inyectando la instancia de Cinema.
     */
    private void cargarVistaHome(String fxmlPath) {
        try {
            if (mainContainer != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent vista = loader.load();

                Object controller = loader.getController();
                if (controller instanceof DashboardHomeController) {
                    ((DashboardHomeController) controller).initData(cinema);
                }

                transicionarVista(vista);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el home del dashboard: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Aplica un efecto suave de fundido (FadeTransition) al transicionar de vista.
     */
    private void transicionarVista(Parent vista) {
        vista.setOpacity(0.0);
        mainContainer.setCenter(vista);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(350), vista);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }
}