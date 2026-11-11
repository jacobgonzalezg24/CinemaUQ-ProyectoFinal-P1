package uniquindio.edu.co.cinemauq.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import uniquindio.edu.co.cinemauq.model.*;
import uniquindio.edu.co.cinemauq.model.exceptions.CinemaException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FuncionController {

    @FXML
    private ComboBox<Pelicula> cmbPelicula;

    @FXML
    private ComboBox<Sala> cmbSala;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private Spinner<Integer> spHora;

    @FXML
    private Spinner<Integer> spMinuto;

    @FXML
    private TextField txtValorBase;

    @FXML
    private TableView<Funcion> tblFunciones;

    @FXML
    private TableColumn<Funcion, String> colCodigo;

    @FXML
    private TableColumn<Funcion, String> colPelicula;

    @FXML
    private TableColumn<Funcion, String> colSala;

    @FXML
    private TableColumn<Funcion, String> colFecha;

    @FXML
    private TableColumn<Funcion, String> colHora;

    @FXML
    private TableColumn<Funcion, String> colValorBase;

    private Cinema cinema;

    @FXML
    public void initialize() {
        // Configurar Spinners para la hora (00-23) y minutos (00-59 de 15 en 15)
        spHora.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 18));
        spMinuto.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15));

        // Conversores para mostrar el nombre directo en los ComboBox
        cmbPelicula.setConverter(new StringConverter<Pelicula>() {
            @Override
            public String toString(Pelicula pelicula) {
                return pelicula != null ? pelicula.getNombre() : "";
            }

            @Override
            public Pelicula fromString(String string) {
                return null;
            }
        });

        cmbSala.setConverter(new StringConverter<Sala>() {
            @Override
            public String toString(Sala sala) {
                return sala != null ? sala.getNombre() : "";
            }

            @Override
            public Sala fromString(String string) {
                return null;
            }
        });

        // Configuración defensiva de columnas usando CellValueFactory con lambdas directo
        colCodigo.setCellValueFactory(cellData -> {
            Funcion f = cellData.getValue();
            if (f == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(String.valueOf(f.getCodigoFuncion()));
        });

        colPelicula.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue() != null && cellData.getValue().getPelicula() != null
                                ? cellData.getValue().getPelicula().getNombre()
                                : ""
                ));

        colSala.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue() != null && cellData.getValue().getSala() != null
                                ? cellData.getValue().getSala().getNombre()
                                : ""
                ));

        colFecha.setCellValueFactory(cellData -> {
            Funcion f = cellData.getValue();
            return new SimpleStringProperty(f != null && f.getFecha() != null ? f.getFecha().toString() : "");
        });

        colHora.setCellValueFactory(cellData -> {
            Funcion f = cellData.getValue();
            if (f != null && f.getHora() != null) {
                return new SimpleStringProperty(f.getHora().format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            return new SimpleStringProperty("");
        });

        colValorBase.setCellValueFactory(cellData -> {
            Funcion f = cellData.getValue();
            if (f != null) {
                return new SimpleStringProperty(String.format("$ %.2f", f.getValorBase()));
            }
            return new SimpleStringProperty("");
        });
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
        cargarCombos();
        actualizarTabla();
    }

    private void cargarCombos() {
        if (cinema != null) {
            cmbPelicula.setItems(FXCollections.observableArrayList(cinema.consultarPeliculasDisponibles()));
            cmbSala.setItems(FXCollections.observableArrayList(cinema.consultarSalas()));
        }
    }

    private void actualizarTabla() {
        if (cinema != null) {
            tblFunciones.setItems(FXCollections.observableArrayList(cinema.consultarFuncionesProgramadas()));
            tblFunciones.refresh();
        }
    }

    @FXML
    private void handleProgramarFuncion() {
        Pelicula pelicula = cmbPelicula.getValue();
        Sala sala = cmbSala.getValue();
        LocalDate fecha = dpFecha.getValue();
        String valorTexto = txtValorBase.getText().trim();

        if (pelicula == null || sala == null || fecha == null || valorTexto.isEmpty()) {
            mostrarAlerta("Campos Incompletos", "Por favor complete todos los campos obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            double valorBase = Double.parseDouble(valorTexto.replace(",", "."));
            int hora = spHora.getValue();
            int minuto = spMinuto.getValue();
            LocalTime horaFuncion = LocalTime.of(hora, minuto);

            // Programar la función mediante el modelo Cinema
            cinema.programarFuncion(
                    pelicula.getCodigoPelicula(),
                    sala.getCodigoSala(),
                    fecha,
                    horaFuncion,
                    valorBase
            );

            mostrarAlerta("Éxito", "La función ha sido programada correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();
            actualizarTabla();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "El valor base debe ser un número válido.", Alert.AlertType.ERROR);
        } catch (CinemaException e) {
            mostrarAlerta("Error al Programar", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error Inesperado", "Ocurrió un error al procesar la solicitud: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Alias para evitar errores con FXML que apunten a 'handleLimpiar'
    @FXML
    private void handleLimpiar(ActionEvent event) {
        limpiarCampos();
    }

    @FXML
    private void handleLimpiarCampos(ActionEvent event) {
        limpiarCampos();
    }

    @FXML
    private void handleCancelarFuncion(ActionEvent event) {
        Funcion funcionSeleccionada = tblFunciones != null ? tblFunciones.getSelectionModel().getSelectedItem() : null;

        // Si hay una función seleccionada en la tabla, la cancela/elimina del sistema
        if (funcionSeleccionada != null) {
            try {
                if (cinema != null) {
                    cinema.cancelarFuncion(funcionSeleccionada.getCodigoFuncion());
                }
                mostrarAlerta("Función Cancelada", "La función ha sido cancelada exitosamente.", Alert.AlertType.INFORMATION);
                actualizarTabla();
                limpiarCampos();
            } catch (CinemaException e) {
                mostrarAlerta("Error al Cancelar", e.getMessage(), Alert.AlertType.ERROR);
            } catch (Exception e) {
                mostrarAlerta("Error Inesperado", "No se pudo cancelar la función: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            // Si no se seleccionó ninguna fila de la tabla, simplemente limpia el formulario
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        cmbPelicula.setValue(null);
        cmbSala.setValue(null);
        dpFecha.setValue(null);
        txtValorBase.clear();
        if (spHora.getValueFactory() != null) spHora.getValueFactory().setValue(18);
        if (spMinuto.getValueFactory() != null) spMinuto.getValueFactory().setValue(0);
        if (tblFunciones != null) tblFunciones.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}