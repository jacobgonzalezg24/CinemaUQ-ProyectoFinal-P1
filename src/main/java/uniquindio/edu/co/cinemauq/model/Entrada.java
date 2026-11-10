package uniquindio.edu.co.cinemauq.model;

public abstract class Entrada {
    private double valorBase;

    public Entrada(double valorBase) {
        this.valorBase = valorBase;
    }

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }

    // Metodo polimorfico para calcular el precio final segun el tipo de entrada
    public abstract double calcularPrecio();
}
