package uniquindio.edu.co.cinemauq.model;

public class EntradaNino extends Entrada {

    public EntradaNino(double porcentajeDescuento) {
        super(3, "Nino", porcentajeDescuento);
    }

    @Override
    public double calcularValor(double valorBase) {
        return valorBase * (1.0 - (getPorcentajeDescuento() / 100.0));
    }
}
