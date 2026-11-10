package uniquindio.edu.co.cinemauq.model;

import uniquindio.edu.co.cinemauq.model.enums.Cargo;

public class Empleado extends Persona {
    private Cargo cargo;

    public Empleado(String identificacion, String nombre, Cargo cargo) {
        super(identificacion, nombre);
        this.cargo = cargo;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }
}
