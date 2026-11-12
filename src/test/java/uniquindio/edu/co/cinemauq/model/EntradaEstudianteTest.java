package uniquindio.edu.co.cinemauq.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class EntradaEstudianteTest {

    // Entrada de ejemplo con 20.0% de descuento. Valor base que usamos en las pruebas: 10000.
    // (En assertEquals con decimales, el 0.001 es el margen de error permitido.)
    private EntradaEstudiante entrada = new EntradaEstudiante(20.0);

    // 1. Verifica el código, el nombre y el descuento que trae el tipo Estudiante.

    @Test
    void elCodigoYElNombreSonLosDelTipoEstudiante() {
        assertEquals(2, entrada.getCodigoEntrada());
        assertEquals("Estudiante", entrada.getNombre());
        assertEquals(20.0, entrada.getPorcentajeDescuento(), 0.001);
    }

    // 2. Verifica que se aplique el descuento al valor base: 10000 con 20.0% = 8000.0.

    @Test
    void calcularValorAplicaElDescuentoAlValorBase() {
        assertEquals(8000.0, entrada.calcularValor(10000.0), 0.001);
    }

    // 3. Verifica que con 0% de descuento el valor sea igual al valor base.

    @Test
    void calcularValorSinDescuentoDevuelveElValorBase() {
        entrada.setPorcentajeDescuento(0.0);

        assertEquals(10000.0, entrada.calcularValor(10000.0), 0.001);
    }

    // 4. Verifica que con 100% de descuento el valor sea cero.

    @Test
    void calcularValorConDescuentoTotalDevuelveCero() {
        entrada.setPorcentajeDescuento(100.0);

        assertEquals(0.0, entrada.calcularValor(10000.0), 0.001);
    }

    // 5. Verifica que al cambiar el descuento cambie también el valor calculado (aquí a 50%: 10000 -> 5000).

    @Test
    void modificarElDescuentoCambiaElValorCalculado() {
        double antes = entrada.calcularValor(10000.0);        // con el descuento original

        entrada.setPorcentajeDescuento(50.0);
        double despues = entrada.calcularValor(10000.0);      // con el descuento nuevo

        assertEquals(8000.0, antes, 0.001);
        assertEquals(5000.0, despues, 0.001);
        assertTrue(despues < antes);                          // ahora paga menos
    }
}