package uniquindio.edu.co.cinemauq.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import uniquindio.edu.co.cinemauq.model.Cinema;
import uniquindio.edu.co.cinemauq.model.Pelicula;
import uniquindio.edu.co.cinemauq.model.enums.Clasificacion;
import uniquindio.edu.co.cinemauq.model.enums.EstadoPelicula;
import uniquindio.edu.co.cinemauq.model.exceptions.CinemaException;

import java.io.File;

public class PeliculasController {

    @FXML private TableView<Pelicula> tablePeliculas;
    @FXML private TableColumn<Pelicula, Integer> colCodigo;
    @FXML private TableColumn<Pelicula, String> colNombre;
    @FXML private TableColumn<Pelicula, String> colGenero;
    @FXML private TableColumn<Pelicula, Integer> colDuracion;
    @FXML private TableColumn<Pelicula, Clasificacion> colClasificacion;
    @FXML private TableColumn<Pelicula, EstadoPelicula> colEstado;

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtGenero;
    @FXML private TextField txtDuracion;
    @FXML private TextField txtImagen; // <--- Nuevo campo para la ruta de la imagen
    @FXML private ComboBox<Clasificacion> cmbClasificacion;
    @FXML private ComboBox<EstadoPelicula> cmbEstado;

    private Cinema cinema = new Cinema();
    private ObservableList<Pelicula> listaObservable;

    @FXML
    public void initialize() {
        // 1. Configurar columnas usando expresiones lambda
        colCodigo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCodigoPelicula()).asObject());
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        colGenero.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGenero()));
        colDuracion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getDuracion()).asObject());
        colClasificacion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getClasificacion()));
        colEstado.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getEstado()));

        // 2. Inicializar ComboBoxes
        cmbClasificacion.setItems(FXCollections.observableArrayList(Clasificacion.values()));
        cmbEstado.setItems(FXCollections.observableArrayList(EstadoPelicula.values()));

        if (this.cinema == null) {
            this.cinema = new Cinema();
        }

        listaObservable = FXCollections.observableArrayList(cinema.consultarPeliculas());
        tablePeliculas.setItems(listaObservable);

        // Listener para rellenar formulario al seleccionar una fila (incluyendo la imagen)
        tablePeliculas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtCodigo.setText(String.valueOf(newSelection.getCodigoPelicula()));
                txtNombre.setText(newSelection.getNombre());
                txtGenero.setText(newSelection.getGenero());
                txtDuracion.setText(String.valueOf(newSelection.getDuracion()));
                txtImagen.setText(newSelection.getImagen() != null ? newSelection.getImagen() : "");
                cmbClasificacion.setValue(newSelection.getClasificacion());
                cmbEstado.setValue(newSelection.getEstado());
            }
        });
    }

    @FXML
    public void initCinema(Cinema cinemaCompartido) {
        this.cinema = cinemaCompartido;
        if (listaObservable != null && this.cinema != null) {
            listaObservable.setAll(this.cinema.consultarPeliculas());
        }
    }

    @FXML
    void onSeleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Póster de Película");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg")
        );

        File archivoSeleccionado = fileChooser.showOpenDialog(txtCodigo.getScene().getWindow());
        if (archivoSeleccionado != null) {
            // Guardamos la ruta absoluta o el URI para que JavaFX pueda cargarlo sin problemas desde cualquier equipo
            txtImagen.setText(archivoSeleccionado.toURI().toString());
        }
    }

    @FXML
    void onRegistrarPelicula() {
        try {
            int codigo = Integer.parseInt(txtCodigo.getText().trim());
            String nombre = txtNombre.getText().trim();
            String genero = txtGenero.getText().trim();
            int duracion = Integer.parseInt(txtDuracion.getText().trim());
            String imagen = txtImagen.getText().trim();
            Clasificacion clasificacion = cmbClasificacion.getValue();
            EstadoPelicula estado = cmbEstado.getValue();

            if (nombre.isEmpty() || genero.isEmpty() || clasificacion == null || estado == null) {
                mostrarAlerta("Campos Incompletos", "Por favor complete todos los campos obligatorios del formulario.", Alert.AlertType.WARNING);
                return;
            }

            if (cinema.verificarPelicula(codigo)) {
                // Actualizar existente
                cinema.modificarPelicula(codigo, nombre, genero, duracion, clasificacion, estado, imagen);
                mostrarAlerta("Éxito", "Película actualizada correctamente.", Alert.AlertType.INFORMATION);
            } else {
                // Registrar nueva
                Pelicula nuevaPelicula = new Pelicula(codigo, nombre, genero, duracion, clasificacion, estado, imagen);
                cinema.registrarPelicula(nuevaPelicula);
                mostrarAlerta("Éxito", "Película registrada con éxito.", Alert.AlertType.INFORMATION);
            }

            listaObservable.setAll(cinema.consultarPeliculas());
            limpiarFormulario();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "Por favor verifique que los campos numéricos (código, duración) sean válidos.", Alert.AlertType.ERROR);
        } catch (CinemaException e) {
            mostrarAlerta("Error de Negocio", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onEliminarPelicula() {
        try {
            Pelicula seleccionada = tablePeliculas.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                cinema.eliminarPelicula(seleccionada.getCodigoPelicula());
                listaObservable.setAll(cinema.consultarPeliculas());
                limpiarFormulario();
                mostrarAlerta("Éxito", "Película eliminada correctamente.", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Advertencia", "Seleccione una película de la tabla para eliminar.", Alert.AlertType.WARNING);
            }
        } catch (CinemaException e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onLimpiarFormulario() {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtCodigo.clear();
        txtNombre.clear();
        txtGenero.clear();
        txtDuracion.clear();
        txtImagen.clear();
        cmbClasificacion.setValue(null);
        cmbEstado.setValue(null);
        tablePeliculas.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}