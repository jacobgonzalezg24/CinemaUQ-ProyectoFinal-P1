package uniquindio.edu.co.cinemauq.model;

public class EntradaAdultoMayor extends Entrada {

    public EntradaAdultoMayor(double valorBase) {
        super(valorBase);
    }

    @Override
    public double calcularPrecio() {
        // Descuento para adulto mayor del 25%
        return getValorBase() * 0.75;
    }
}
