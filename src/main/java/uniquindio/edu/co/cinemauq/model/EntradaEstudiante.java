package uniquindio.edu.co.cinemauq.model;

public class EntradaEstudiante extends Entrada {

    public EntradaEstudiante(double valorBase) {
        super(valorBase);
    }

    @Override
    public double calcularPrecio() {
        // Descuento para estudiantes del 20%
        return getValorBase() * 0.80;
    }
}
