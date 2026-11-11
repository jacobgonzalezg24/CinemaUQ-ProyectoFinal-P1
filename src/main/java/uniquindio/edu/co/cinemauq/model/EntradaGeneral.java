package uniquindio.edu.co.cinemauq.model;

public class EntradaGeneral extends Entrada {

    public EntradaGeneral() {
        super(1, "General", 0.0);
    }

    @Override
    public double calcularValor(double valorBase) {
        return valorBase;
    }
}