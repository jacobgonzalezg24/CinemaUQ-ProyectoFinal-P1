package uniquindio.edu.co.cinemauq.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import uniquindio.edu.co.cinemauq.model.enums.Cargo;
import uniquindio.edu.co.cinemauq.model.enums.Clasificacion;
import uniquindio.edu.co.cinemauq.model.enums.EstadoPelicula;
import uniquindio.edu.co.cinemauq.model.enums.EstadoSala;
import uniquindio.edu.co.cinemauq.model.enums.EstadoVenta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

class VentaTest {

    // Todo lo que necesita una venta, declarado en orden (cada objeto usa los anteriores).
    private Cliente cliente = new Cliente("1001", "Ana");
    private Empleado empleado = new Empleado(1, "2002", "Carlos", Cargo.ADMINISTRADOR);
    private Pelicula pelicula = new Pelicula(1, "Avatar", "Fantasía", 192,
            Clasificacion.MAYORES_12, EstadoPelicula.DISPONIBLE, "avatar.jpeg");
    private Sala sala = new Sala("S1", "Sala 1", 2, 3, EstadoSala.DISPONIBLE);
    private Funcion funcion = new Funcion(100, pelicula, sala,
            LocalDate.of(2026, 11, 20), LocalTime.of(18, 0), 10000.0);       // valor base 10000
    private Asiento asiento = funcion.getMatrizAsientos()[0][0];             // asiento A1 de esa función
    private Entrada entrada = new EntradaEstudiante(20.0);                   // 20% de descuento
    private Venta venta = new Venta(1, cliente, empleado, funcion, asiento, entrada);

    //1. Verifica que una venta nueva quede ACTIVA.

    @Test
    void ventaNuevaNaceActiva() {
        assertEquals(EstadoVenta.ACTIVA, venta.getEstado());
        assertTrue(venta.estaActiva());
    }

    //2. Verifica que la venta guarde el cliente, el empleado, la función, el asiento y la entrada que se le pasaron.

    @Test
    void laVentaConservaClienteEmpleadoFuncionAsientoYEntrada() {
        assertEquals(1, venta.getCodigoVenta());
        assertEquals(cliente, venta.getCliente());
        assertEquals(empleado, venta.getEmpleado());
        assertEquals(funcion, venta.getFuncion());
        assertEquals(asiento, venta.getAsiento());
        assertEquals(entrada, venta.getEntrada());
    }

    // 3. Verifica que el valor pagado salga del valor base de la función y del descuento de la entrada:
    // 10000 con 20% de descuento = 8000. Con una entrada General (sin descuento) paga los 10000 completos.

    @Test
    void elValorPagadoSeCalculaConLaEntradaYElValorBaseDeLaFuncion() {
        Venta ventaGeneral = new Venta(2, cliente, empleado, funcion, asiento, new EntradaGeneral());

        assertEquals(8000.0, venta.getValorPagado(), 0.001);          // 0.001 = margen de error de los decimales
        assertEquals(10000.0, ventaGeneral.getValorPagado(), 0.001);
    }

    // 4. Verifica que la fecha y hora se registren automáticamente al crear la venta:
    // debe estar entre el momento justo antes y el momento justo después de crearla.

    @Test
    void laFechaYHoraSeRegistranAlCrearLaVenta() {
        LocalDateTime antes = LocalDateTime.now();
        Venta nueva = new Venta(3, cliente, empleado, funcion, asiento, entrada);
        LocalDateTime despues = LocalDateTime.now();

        assertTrue(nueva.getFechaHora() != null);                     // sí se registró
        assertTrue(!nueva.getFechaHora().isBefore(antes));            // no es anterior a "antes"
        assertTrue(!nueva.getFechaHora().isAfter(despues));           // no es posterior a "después"
    }

    // 5. Verifica que cancelar() cambie el estado a CANCELADA.

    @Test
    void cancelarCambiaElEstadoACancelada() {
        venta.cancelar();

        assertEquals(EstadoVenta.CANCELADA, venta.getEstado());
        assertFalse(venta.estaActiva());
    }
}
