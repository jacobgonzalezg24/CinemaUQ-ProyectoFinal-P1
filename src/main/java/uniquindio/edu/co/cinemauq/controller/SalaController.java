package uniquindio.edu.co.cinemauq.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uniquindio.edu.co.cinemauq.model.Cinema;
import uniquindio.edu.co.cinemauq.model.Sala;
import uniquindio.edu.co.cinemauq.model.enums.EstadoSala;
import uniquindio.edu.co.cinemauq.model.exceptions.SalaDuplicadaException;
import uniquindio.edu.co.cinemauq.model.exceptions.SalaNoEncontradaException;

public class SalaController {

    @FXML
    private TextField txtCodigoSala;

    @FXML
    private TextField txtNombre;

    @FXML
    private Spinner<Integer> spnFilas;

    @FXML
    private Spinner<Integer> spnColumnas;

    @FXML
    private ComboBox<EstadoSala> cbEstado;

    @FXML
    private TableView<Sala> tblSalas;

    @FXML
    private TableColumn<Sala, String> colCodigoSala;

    @FXML
    private TableColumn<Sala, String> colNombre;

    @FXML
    private TableColumn<Sala, Integer> colCapacidad;

    @FXML
    private TableColumn<Sala, String> colEstado;

    private Cinema cinema;
    private ObservableList<Sala> listaSalasObservable;
    private Sala salaSeleccionada;

    @FXML
    public void initialize() {
        if (this.cinema == null) {
            this.cinema = new Cinema();
        }

        configurarSpinners();
        configurarComboBox();
        configurarTabla();
        listenerSeleccionTabla();
        cargarSalas();
    }

    private void configurarSpinners() {
        SpinnerValueFactory<Integer> valueFactoryFilas = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 26, 10);
        SpinnerValueFactory<Integer> valueFactoryColumnas = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 10);

        spnFilas.setValueFactory(valueFactoryFilas);
        spnColumnas.setValueFactory(valueFactoryColumnas);
    }

    private void configurarComboBox() {
        cbEstado.setItems(FXCollections.observableArrayList(EstadoSala.values()));
    }

    private void configurarTabla() {
        colCodigoSala.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigoSala()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colCapacidad.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCapacidadTotal()).asObject());
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado() != null ? cellData.getValue().getEstado().toString() : ""));

        // Centrar el contenido de las celdas
        colCodigoSala.setStyle("-fx-alignment: CENTER;");
        colNombre.setStyle("-fx-alignment: CENTER;");
        colCapacidad.setStyle("-fx-alignment: CENTER;");
        colEstado.setStyle("-fx-alignment: CENTER;");

        listaSalasObservable = FXCollections.observableArrayList();
        tblSalas.setItems(listaSalasObservable);
    }

    private void listenerSeleccionTabla() {
        tblSalas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                salaSeleccionada = newValue;
                mostrarDetallesSala(newValue);
            }
        });
    }

    private void mostrarDetallesSala(Sala sala) {
        txtCodigoSala.setText(sala.getCodigoSala());
        txtCodigoSala.setDisable(true);
        txtNombre.setText(sala.getNombre());
        spnFilas.getValueFactory().setValue(sala.getNumeroFilas());
        spnColumnas.getValueFactory().setValue(sala.getNumeroColumnas());
        cbEstado.setValue(sala.getEstado());
    }

    private void cargarSalas() {
        if (cinema != null) {
            listaSalasObservable.setAll(cinema.consultarSalas());
            tblSalas.refresh();
        }
    }

    @FXML
    private void handleGuardar() {
        String codigo = txtCodigoSala.getText().trim();
        String nombre = txtNombre.getText().trim();
        Integer filas = spnFilas.getValue();
        Integer columnas = spnColumnas.getValue();
        EstadoSala estado = cbEstado.getValue();

        if (codigo.isEmpty() || nombre.isEmpty() || estado == null || filas == null || columnas == null) {
            mostrarAlerta("Campos Incompletos", "Por favor diligencie todos los campos requeridos.", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (salaSeleccionada == null) {
                // Registrar nueva sala
                Sala nuevaSala = new Sala(codigo, nombre, filas, columnas, estado);
                cinema.registrarSala(nuevaSala);
                mostrarAlerta("Éxito", "La sala se ha registrado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                // Modificar sala existente
                cinema.modificarSala(salaSeleccionada.getCodigoSala(), nombre, estado);
                salaSeleccionada.setNumeroFilas(filas);
                salaSeleccionada.setNumeroColumnas(columnas);
                mostrarAlerta("Éxito", "La sala se ha actualizado correctamente.", Alert.AlertType.INFORMATION);
            }

            limpiarFormulario();
            cargarSalas();

        } catch (SalaDuplicadaException | SalaNoEncontradaException e) {
            mostrarAlerta("Error de Operación", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error Inesperado", "Ocurrió un error al procesar la solicitud: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleEliminar() {
        if (salaSeleccionada == null) {
            mostrarAlerta("Selección requerida", "Debe seleccionar una sala de la tabla para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            cinema.eliminarSala(salaSeleccionada.getCodigoSala());
            mostrarAlerta("Éxito", "La sala ha sido eliminada correctamente.", Alert.AlertType.INFORMATION);
            limpiarFormulario();
            cargarSalas();
        } catch (SalaNoEncontradaException e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleLimpiar() {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtCodigoSala.clear();
        txtCodigoSala.setDisable(false);
        txtNombre.clear();
        spnFilas.getValueFactory().setValue(10);
        spnColumnas.getValueFactory().setValue(10);
        cbEstado.setValue(null);
        salaSeleccionada = null;
        tblSalas.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
        cargarSalas();
    }
}