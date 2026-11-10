package uniquindio.edu.co.cinemauq.model;

public abstract class Entrada {
    private int codigoEntrada;
    private String nombre;
    private double porcentajeDescuento;

    public Entrada(int codigoEntrada, String nombre, double porcentajeDescuento) {
        this.codigoEntrada = codigoEntrada;
        this.nombre = nombre;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public int getCodigoEntrada() {
        return codigoEntrada;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public abstract double calcularValor(double valorBase);
}

