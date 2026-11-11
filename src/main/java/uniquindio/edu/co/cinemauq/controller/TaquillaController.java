package uniquindio.edu.co.cinemauq.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import uniquindio.edu.co.cinemauq.model.*;
import uniquindio.edu.co.cinemauq.model.enums.Cargo;
import uniquindio.edu.co.cinemauq.model.exceptions.CinemaException;
import uniquindio.edu.co.cinemauq.model.exceptions.ClienteNoEncontradoException;
import uniquindio.edu.co.cinemauq.model.exceptions.EmpleadoNoEncontradoException;

import java.util.ArrayList;
import java.util.List;

public class TaquillaController {

    @FXML
    private ComboBox<Funcion> cmbFuncion;

    @FXML
    private GridPane gridAsientos;

    @FXML
    private TextField txtCedulaCliente;

    @FXML
    private TextField txtNombreCliente;

    @FXML
    private TextField txtCodigoEmpleado;

    @FXML
    private TextField txtNombreEmpleado;

    @FXML
    private ComboBox<Entrada> cmbTipoEntrada;

    @FXML
    private Label lblAsientosSeleccionados;

    @FXML
    private Label lblTotalPagar;

    private Cinema cinema;
    private Funcion funcionSeleccionada;
    private final List<Asiento> asientosSeleccionados = new ArrayList<>();

    @FXML
    public void initialize() {
        cmbFuncion.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                funcionSeleccionada = newVal;
                asientosSeleccionados.clear();
                actualizarResumen();
                generarMapaAsientos();
            }
        });

        if (cmbTipoEntrada != null) {
            cmbTipoEntrada.valueProperty().addListener((obs, oldVal, newVal) -> actualizarResumen());
        }

        // Cargar por defecto los datos del empleado Jacob González (111)
        txtCodigoEmpleado.setText("111");
        txtNombreEmpleado.setText("Jacob Gonzalez");
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        if (cinema != null) {
            cmbFuncion.setItems(FXCollections.observableArrayList(cinema.consultarFuncionesProgramadas()));
            if (cmbTipoEntrada != null) {
                cmbTipoEntrada.setItems(FXCollections.observableArrayList(cinema.consultarTiposEntrada()));
            }
        }
    }

    @FXML
    private void handleBuscarCliente() {
        String cedula = txtCedulaCliente.getText().trim();
        if (cedula.isEmpty()) {
            mostrarAlerta("Atención", "Ingrese un número de documento para buscar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            Cliente cliente = cinema.buscarCliente(cedula);
            txtNombreCliente.setText(cliente.getNombre());
            mostrarAlerta("Cliente Encontrado", "Cliente registrado: " + cliente.getNombre(), Alert.AlertType.INFORMATION);
        } catch (ClienteNoEncontradoException e) {
            txtNombreCliente.clear();
            mostrarAlerta("Cliente Nuevo", "El cliente no está registrado. Ingrese el nombre para registrarlo automáticamente al vender.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void handleBuscarEmpleado() {
        String codigoText = txtCodigoEmpleado.getText().trim();
        if (codigoText.isEmpty()) {
            mostrarAlerta("Atención", "Ingrese el código del empleado.", Alert.AlertType.WARNING);
            return;
        }

        try {
            int codigoEmpleado = Integer.parseInt(codigoText);
            Empleado emp = cinema.buscarEmpleado(codigoEmpleado);
            txtNombreEmpleado.setText(emp.getNombre());
            mostrarAlerta("Empleado Encontrado", "Empleado: " + emp.getNombre() + " (" + emp.getCargo() + ")", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "El código debe ser numérico.", Alert.AlertType.ERROR);
        } catch (EmpleadoNoEncontradoException e) {
            txtNombreEmpleado.clear();
            mostrarAlerta("Empleado Nuevo", "El empleado no existe. Ingrese el nombre para registrarlo automáticamente.", Alert.AlertType.INFORMATION);
        }
    }

    private void generarMapaAsientos() {
        gridAsientos.getChildren().clear();

        if (funcionSeleccionada == null || funcionSeleccionada.getSala() == null) {
            return;
        }

        Sala sala = funcionSeleccionada.getSala();
        int numFilas = sala.getNumeroFilas();
        int numColumnas = sala.getNumeroColumnas();
        Asiento[][] matriz = funcionSeleccionada.getMatrizAsientos();

        for (int col = 0; col < numColumnas; col++) {
            Label lblCol = new Label(String.valueOf(col + 1));
            lblCol.setStyle("-fx-text-fill: #94a3b8; -fx-font-weight: bold; -fx-font-size: 11px;");
            gridAsientos.add(lblCol, col + 1, 0);
        }

        for (int i = 0; i < numFilas; i++) {
            char letraFila = (char) ('A' + i);

            Label lblFilaIzq = new Label(String.valueOf(letraFila));
            lblFilaIzq.setStyle("-fx-text-fill: #94a3b8; -fx-font-weight: bold; -fx-font-size: 11px;");
            gridAsientos.add(lblFilaIzq, 0, i + 1);

            for (int j = 0; j < numColumnas; j++) {
                Asiento asiento = matriz[i][j];
                if (asiento != null) {
                    Button btnAsiento = crearBotonAsiento(asiento);
                    gridAsientos.add(btnAsiento, j + 1, i + 1);
                }
            }

            Label lblFilaDer = new Label(String.valueOf(letraFila));
            lblFilaDer.setStyle("-fx-text-fill: #94a3b8; -fx-font-weight: bold; -fx-font-size: 11px;");
            gridAsientos.add(lblFilaDer, numColumnas + 1, i + 1);
        }
    }

    private Button crearBotonAsiento(Asiento asiento) {
        Button btn = new Button(asiento.getCodigoAsiento());
        btn.setPrefSize(44, 38);

        if (!asiento.estaDisponible()) {
            btn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 10px; -fx-background-radius: 6;");
            btn.setDisable(true);
        } else if (asientosSeleccionados.contains(asiento)) {
            btn.setStyle("-fx-background-color: #7c3aed; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 10px; -fx-background-radius: 6;");
        } else {
            aplicarEstiloDisponible(btn);
        }

        btn.setOnAction(e -> {
            if (asientosSeleccionados.contains(asiento)) {
                asientosSeleccionados.remove(asiento);
                aplicarEstiloDisponible(btn);
            } else {
                asientosSeleccionados.add(asiento);
                btn.setStyle("-fx-background-color: #7c3aed; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 10px; -fx-background-radius: 6;");
            }
            actualizarResumen();
        });

        return btn;
    }

    private void aplicarEstiloDisponible(Button btn) {
        btn.setStyle("-fx-background-color: #1e293b; -fx-text-fill: #cbd5e1; -fx-border-color: #475569; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-size: 10px; -fx-cursor: hand;");
    }

    private void actualizarResumen() {
        if (asientosSeleccionados.isEmpty() || funcionSeleccionada == null) {
            lblAsientosSeleccionados.setText("Ninguno");
            lblTotalPagar.setText("$ 0");
            return;
        }

        StringBuilder codigos = new StringBuilder();
        for (Asiento a : asientosSeleccionados) {
            codigos.append(a.getCodigoAsiento()).append(" ");
        }
        lblAsientosSeleccionados.setText(codigos.toString().trim());

        double precioBaseUnitario = funcionSeleccionada.getValorBase();
        Entrada tipoEntrada = cmbTipoEntrada.getValue();

        double precioUnitario = (tipoEntrada != null)
                ? tipoEntrada.calcularValor(precioBaseUnitario)
                : precioBaseUnitario;

        double total = asientosSeleccionados.size() * precioUnitario;

        lblTotalPagar.setText(String.format("$ %.0f", total));
    }

    @FXML
    private void handleConfirmarVenta() {
        if (funcionSeleccionada == null) {
            mostrarAlerta("Atención", "Debe seleccionar una función primero.", Alert.AlertType.WARNING);
            return;
        }

        String cedula = txtCedulaCliente.getText().trim();
        String nombreCliente = txtNombreCliente.getText().trim();

        if (cedula.isEmpty() || nombreCliente.isEmpty()) {
            mostrarAlerta("Atención", "Debe ingresar el documento y el nombre del cliente.", Alert.AlertType.WARNING);
            return;
        }

        String codEmpleadoText = txtCodigoEmpleado.getText().trim();
        String nombreEmpleado = txtNombreEmpleado.getText().trim();

        if (codEmpleadoText.isEmpty() || nombreEmpleado.isEmpty()) {
            mostrarAlerta("Atención", "Debe ingresar el código y nombre del empleado que procesa la venta.", Alert.AlertType.WARNING);
            return;
        }

        Entrada tipoEntrada = cmbTipoEntrada.getValue();
        if (tipoEntrada == null) {
            mostrarAlerta("Atención", "Debe seleccionar un tipo de entrada.", Alert.AlertType.WARNING);
            return;
        }

        if (asientosSeleccionados.isEmpty()) {
            mostrarAlerta("Atención", "Debe seleccionar al menos un asiento en el mapa.", Alert.AlertType.WARNING);
            return;
        }

        try {
            int codigoEmpleado = Integer.parseInt(codEmpleadoText);

            // 1. Cliente: si no existe, se registra
            if (!cinema.verificarCliente(cedula)) {
                Cliente nuevoCliente = new Cliente(cedula, nombreCliente);
                cinema.registrarCliente(nuevoCliente);
            }

            // 2. Empleado: si no existe, se registra automáticamente
            if (!cinema.verificarEmpleado(codigoEmpleado)) {
                Empleado nuevoEmpleado = new Empleado(codigoEmpleado, "00000000", nombreEmpleado, Cargo.OPERADOR);
                cinema.registrarEmpleado(nuevoEmpleado);
            }

            // 3. Registrar venta asociada al cliente y empleado seleccionado
            for (Asiento asiento : asientosSeleccionados) {
                cinema.venderEntrada(
                        cedula,
                        codigoEmpleado,
                        funcionSeleccionada.getCodigoFuncion(),
                        tipoEntrada.getCodigoEntrada(),
                        asiento.getCodigoAsiento()
                );
            }

            mostrarAlerta("Venta Exitosa", "Se registraron " + asientosSeleccionados.size() + " entradas por el empleado " + nombreEmpleado + ".", Alert.AlertType.INFORMATION);

            asientosSeleccionados.clear();
            txtCedulaCliente.clear();
            txtNombreCliente.clear();
            actualizarResumen();
            generarMapaAsientos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "El código de empleado debe ser un valor entero.", Alert.AlertType.ERROR);
        } catch (CinemaException e) {
            mostrarAlerta("Error en la Venta", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error Inesperado", "Ocurrió un error al procesar la venta: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancelarSeleccion() {
        asientosSeleccionados.clear();
        txtCedulaCliente.clear();
        txtNombreCliente.clear();
        actualizarResumen();
        generarMapaAsientos();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}