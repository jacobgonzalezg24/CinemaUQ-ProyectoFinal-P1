package uniquindio.edu.co.cinemauq.model;

public class EntradaGeneral extends Entrada {

    public EntradaGeneral(double valorBase) {
        super(valorBase);
    }

    @Override
    public double calcularPrecio() {
        // Entrada general cobra la tarifa base completa
        return getValorBase();
    }
}
