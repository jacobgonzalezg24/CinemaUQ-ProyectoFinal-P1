package uniquindio.edu.co.cinemauq.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uniquindio.edu.co.cinemauq.model.Cinema;
import uniquindio.edu.co.cinemauq.model.Empleado;
import uniquindio.edu.co.cinemauq.model.Pelicula;
import uniquindio.edu.co.cinemauq.model.Venta;
import uniquindio.edu.co.cinemauq.model.exceptions.EmpleadoNoEncontradoException;

import java.util.List;

public class ReportesController {

    @FXML
    private Label lblTotalEntradas;

    @FXML
    private Label lblTotalIngresos;

    @FXML
    private Label lblPeliculaMasVendida;

    @FXML
    private ComboBox<Empleado> cmbEmpleadoFiltro;

    @FXML
    private TableView<Venta> tblVentas;

    @FXML
    private TableColumn<Venta, String> colCodigoVenta;

    @FXML
    private TableColumn<Venta, String> colEmpleado;

    @FXML
    private TableColumn<Venta, String> colCliente;

    @FXML
    private TableColumn<Venta, String> colPelicula;

    @FXML
    private TableColumn<Venta, String> colAsiento;

    @FXML
    private TableColumn<Venta, String> colValor;

    private Cinema cinema;

    @FXML
    public void initialize() {
        // Configuración estándar de celdas
        colCodigoVenta.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getCodigoVenta())));
        colEmpleado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmpleado() != null ? cell.getValue().getEmpleado().getNombre() : "N/A"));
        colCliente.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCliente() != null ? cell.getValue().getCliente().getNombre() : "N/A"));
        colPelicula.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFuncion() != null && cell.getValue().getFuncion().getPelicula() != null ? cell.getValue().getFuncion().getPelicula().getNombre() : "N/A"));
        colAsiento.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAsiento() != null ? cell.getValue().getAsiento().getCodigoAsiento() : "N/A"));
        colValor.setCellValueFactory(cell -> new SimpleStringProperty(String.format("$ %.2f", cell.getValue().getValorPagado())));

        // Listener del ComboBox de Empleados
        cmbEmpleadoFiltro.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                filtrarVentasPorEmpleado(newVal);
            }
        });
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
        cargarDatos();
    }

    private void cargarDatos() {
        if (cinema == null) return;

        // Métricas Globales
        lblTotalEntradas.setText(String.valueOf(cinema.totalEntradasVendidas()));
        lblTotalIngresos.setText(String.format("$ %.2f", cinema.totalIngresos()));

        Pelicula peliTop = cinema.peliculaMasVendida();
        lblPeliculaMasVendida.setText(peliTop != null ? peliTop.getNombre() : "Sin ventas");

        // Cargar Lista de Empleados en el ComboBox
        cmbEmpleadoFiltro.setItems(FXCollections.observableArrayList(cinema.consultarEmpleados()));

        // Cargar todas las ventas en la tabla inicialmente
        tblVentas.setItems(FXCollections.observableArrayList(cinema.consultarVentas()));
    }

    private void filtrarVentasPorEmpleado(Empleado empleado) {
        if (cinema == null || empleado == null) return;
        try {
            List<Venta> ventasEmpleado = cinema.consultarVentasPorEmpleado(empleado.getCodigoEmpleado());
            tblVentas.setItems(FXCollections.observableArrayList(ventasEmpleado));
        } catch (EmpleadoNoEncontradoException e) {
            tblVentas.getItems().clear();
        }
    }

    @FXML
    private void handleMostrarTodas() { // O sin parámetros: handleMostrarTodas()
        if (cinema != null) {
            cmbEmpleadoFiltro.setValue(null);
            tblVentas.setItems(FXCollections.observableArrayList(cinema.consultarVentas()));
        }
    }
}
