package uniquindio.edu.co.cinemauq.model;

public class EntradaAdultoMayor extends Entrada {

    public EntradaAdultoMayor(double porcentajeDescuento) {
        super(4, "Adulto Mayor", porcentajeDescuento);
    }

    @Override
    public double calcularValor(double valorBase) {
        return valorBase * (1.0 - (getPorcentajeDescuento() / 100.0));
    }
}