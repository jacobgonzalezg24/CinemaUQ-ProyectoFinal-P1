package uniquindio.edu.co.cinemauq.model;

import uniquindio.edu.co.cinemauq.model.enums.EstadoVenta;
import java.time.LocalDateTime;

public class Venta {
    private String codigo;
    private Cliente cliente;
    private Funcion funcion;
    private Asiento asiento;
    private Entrada entrada;
    private LocalDateTime fechaHora;
    private double valorPagado;
    private EstadoVenta estado;

    public Venta(String codigo, Cliente cliente, Funcion funcion, Asiento asiento, Entrada entrada) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asiento = asiento;
        this.entrada = entrada;
        this.fechaHora = LocalDateTime.now();
        this.valorPagado = entrada.calcularPrecio();
        this.estado = EstadoVenta.ACTIVA;
        // Al registrar la venta, el asiento cambia su estado a ocupado en la matriz de la sala
        asiento.ocupar();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(double valorPagado) {
        this.valorPagado = valorPagado;
    }

    public EstadoVenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoVenta estado) {
        this.estado = estado;
    }

    // Al cancelar la venta, se libera la reserva del asiento correspondiente
    public void cancelarVenta() {
        this.estado = EstadoVenta.CANCELADA;
        this.asiento.liberar();
    }
}
