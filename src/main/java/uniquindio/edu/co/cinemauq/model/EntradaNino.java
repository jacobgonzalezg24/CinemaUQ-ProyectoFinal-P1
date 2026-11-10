package uniquindio.edu.co.cinemauq.model;

public class EntradaNino extends Entrada {

    public EntradaNino(double valorBase) {
        super(valorBase);
    }

    @Override
    public double calcularPrecio() {
        // Descuento para ninos del 30%
        return getValorBase() * 0.70;
    }
}
