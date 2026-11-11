package uniquindio.edu.co.cinemauq.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import uniquindio.edu.co.cinemauq.model.Cinema;
import uniquindio.edu.co.cinemauq.model.Empleado;
import uniquindio.edu.co.cinemauq.model.Funcion;
import uniquindio.edu.co.cinemauq.model.Pelicula;
import uniquindio.edu.co.cinemauq.model.Venta;

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
    private TextField txtBusquedaGlobal;

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
    private TableColumn<Venta, String> colSala;

    @FXML
    private TableColumn<Venta, String> colFuncion;

    @FXML
    private TableColumn<Venta, String> colAsiento;

    @FXML
    private TableColumn<Venta, String> colValor;

    private Cinema cinema;
    private ObservableList<Venta> listaVentasMaster = FXCollections.observableArrayList();
    private FilteredList<Venta> listaFiltradaVentas;

    @FXML
    public void initialize() {
        // Converter para el ComboBox de Empleados
        cmbEmpleadoFiltro.setConverter(new StringConverter<Empleado>() {
            @Override
            public String toString(Empleado emp) {
                return emp != null ? emp.getNombre() + " (ID: " + emp.getCodigoEmpleado() + ")" : "";
            }

            @Override
            public Empleado fromString(String string) {
                return null;
            }
        });

        // Mapeo detallado y seguro de las celdas de la tabla
        colCodigoVenta.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().getCodigoVenta())));

        colEmpleado.setCellValueFactory(cell -> {
            Venta v = cell.getValue();
            if (v != null && v.getEmpleado() != null) {
                return new SimpleStringProperty(v.getEmpleado().getNombre() + " [" + v.getEmpleado().getCodigoEmpleado() + "]");
            }
            return new SimpleStringProperty("N/A");
        });

        colCliente.setCellValueFactory(cell -> {
            Venta v = cell.getValue();
            if (v != null && v.getCliente() != null) {
                String idCliente = "";
                try {
                    idCliente = String.valueOf(v.getCliente().getIdentificacion());
                } catch (Exception e) {
                    idCliente = "N/A";
                }
                return new SimpleStringProperty(v.getCliente().getNombre() + " [ID: " + idCliente + "]");
            }
            return new SimpleStringProperty("N/A");
        });

        colPelicula.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue() != null && cell.getValue().getFuncion() != null && cell.getValue().getFuncion().getPelicula() != null
                        ? cell.getValue().getFuncion().getPelicula().getNombre()
                        : "N/A"
        ));

        // Mapeo defensivo para SALA
        colSala.setCellValueFactory(cell -> {
            Venta v = cell.getValue();
            if (v != null && v.getFuncion() != null && v.getFuncion().getSala() != null) {
                String nombreSala = v.getFuncion().getSala().getNombre();
                String codSala = v.getFuncion().getSala().getCodigoSala();
                if (nombreSala != null && !nombreSala.trim().isEmpty()) {
                    return new SimpleStringProperty(nombreSala);
                } else if (codSala != null && !codSala.trim().isEmpty()) {
                    return new SimpleStringProperty(codSala);
                }
            }
            return new SimpleStringProperty("N/A");
        });

        // Mapeo defensivo para CÓDIGO DE FUNCIÓN
        colFuncion.setCellValueFactory(cell -> {
            Venta v = cell.getValue();
            if (v != null && v.getFuncion() != null) {
                return new SimpleStringProperty(String.valueOf(v.getFuncion().getCodigoFuncion()));
            }
            return new SimpleStringProperty("N/A");
        });

        colAsiento.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue() != null && cell.getValue().getAsiento() != null
                        ? cell.getValue().getAsiento().getCodigoAsiento()
                        : "N/A"
        ));

        colValor.setCellValueFactory(cell -> new SimpleStringProperty(
                String.format("$ %.2f", cell.getValue().getValorPagado())
        ));

        // Ajuste automático para que las columnas cubran todo el ancho de la tabla
        tblVentas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Inicialización de la lista filtrada ligada a la tabla
        listaFiltradaVentas = new FilteredList<>(listaVentasMaster, p -> true);
        tblVentas.setItems(listaFiltradaVentas);

        // Listener para búsqueda dinámica en tiempo real
        if (txtBusquedaGlobal != null) {
            txtBusquedaGlobal.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        }

        // Listener del ComboBox de Empleados
        cmbEmpleadoFiltro.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
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

        // Cargar todas las ventas
        listaVentasMaster.setAll(cinema.consultarVentas());
        aplicarFiltros();
    }

    /**
     * Filtra dinámicamente por texto ingresado (Empleado, Cliente, Película, Sala, Función, Asiento)
     * y por la selección en el ComboBox de Empleados.
     */
    private void aplicarFiltros() {
        String busqueda = (txtBusquedaGlobal != null && txtBusquedaGlobal.getText() != null)
                ? txtBusquedaGlobal.getText().trim().toLowerCase()
                : "";
        Empleado empleadoSeleccionado = cmbEmpleadoFiltro.getValue();

        listaFiltradaVentas.setPredicate(venta -> {
            if (venta == null) return false;

            // Filtro 1: ComboBox de Empleados (Comparación primitiva int a int)
            if (empleadoSeleccionado != null) {
                if (venta.getEmpleado() == null ||
                        venta.getEmpleado().getCodigoEmpleado() != empleadoSeleccionado.getCodigoEmpleado()) {
                    return false;
                }
            }

            // Filtro 2: Búsqueda Global (si está vacía, pasa la prueba)
            if (busqueda.isEmpty()) {
                return true;
            }

            // Extracción segura de atributos para comparación
            String codVenta = String.valueOf(venta.getCodigoVenta()).toLowerCase();
            String nombreEmpleado = venta.getEmpleado() != null ? venta.getEmpleado().getNombre().toLowerCase() : "";

            String codEmpleado = (venta.getEmpleado() != null)
                    ? String.valueOf(venta.getEmpleado().getCodigoEmpleado()).toLowerCase()
                    : "";

            String nombreCliente = venta.getCliente() != null ? venta.getCliente().getNombre().toLowerCase() : "";
            String idCliente = "";
            if (venta.getCliente() != null) {
                try {
                    idCliente = String.valueOf(venta.getCliente().getIdentificacion()).toLowerCase();
                } catch (Exception e) {
                    idCliente = "";
                }
            }

            String nombrePelicula = (venta.getFuncion() != null && venta.getFuncion().getPelicula() != null)
                    ? venta.getFuncion().getPelicula().getNombre().toLowerCase() : "";
            String nombreSala = (venta.getFuncion() != null && venta.getFuncion().getSala() != null)
                    ? venta.getFuncion().getSala().getNombre().toLowerCase() : "";
            String codFuncion = venta.getFuncion() != null
                    ? String.valueOf(venta.getFuncion().getCodigoFuncion()).toLowerCase() : "";
            String codAsiento = venta.getAsiento() != null
                    ? venta.getAsiento().getCodigoAsiento().toLowerCase() : "";

            // Retorna verdadero si alguna propiedad coincide con la búsqueda
            return codVenta.contains(busqueda)
                    || nombreEmpleado.contains(busqueda)
                    || codEmpleado.contains(busqueda)
                    || nombreCliente.contains(busqueda)
                    || idCliente.contains(busqueda)
                    || nombrePelicula.contains(busqueda)
                    || nombreSala.contains(busqueda)
                    || codFuncion.contains(busqueda)
                    || codAsiento.contains(busqueda);
        });
    }

    @FXML
    private void handleMostrarTodas() {
        cmbEmpleadoFiltro.setValue(null);
        if (txtBusquedaGlobal != null) {
            txtBusquedaGlobal.clear();
        }
        aplicarFiltros();
    }
}