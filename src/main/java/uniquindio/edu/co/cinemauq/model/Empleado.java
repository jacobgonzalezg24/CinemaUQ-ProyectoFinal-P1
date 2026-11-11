package uniquindio.edu.co.cinemauq.model;


import uniquindio.edu.co.cinemauq.model.enums.Cargo;

public class Empleado extends Persona {
    private int codigoEmpleado;
    private Cargo cargo;

    public Empleado(int codigoEmpleado, String identificacion, String nombre, Cargo cargo) {
        super(identificacion, nombre);
        this.codigoEmpleado = codigoEmpleado;
        this.cargo = cargo;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return super.getNombre();
    }
}

