package uniquindio.edu.co.cinemauq.model;

public class EntradaEstudiante extends Entrada {

    public EntradaEstudiante(double porcentajeDescuento) {
        super(2, "Estudiante", porcentajeDescuento);
    }

    @Override
    public double calcularValor(double valorBase) {
        return valorBase * (1.0 - (getPorcentajeDescuento() / 100.0));
    }
}
