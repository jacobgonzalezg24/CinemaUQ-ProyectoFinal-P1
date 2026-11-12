package uniquindio.edu.co.cinemauq.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class EntradaGeneralTest {

    private EntradaGeneral entrada = new EntradaGeneral();

    // 1. Verifica el código y el nombre fijos del tipo General (y que no tenga descuento).

    @Test
    void elCodigoYElNombreSonLosDelTipoGeneral() {
        assertEquals(1, entrada.getCodigoEntrada());
        assertEquals("General", entrada.getNombre());
        assertEquals(0.0, entrada.getPorcentajeDescuento(), 0.001);   // 0.001 = margen de error de los decimales
    }

    // 2. Verifica que el valor sea igual al valor base, porque no hay descuento.

    @Test
    void calcularValorDevuelveElValorBaseSinDescuento() {
        assertEquals(10000.0, entrada.calcularValor(10000.0), 0.001);
        assertEquals(25000.0, entrada.calcularValor(25000.0), 0.001);
    }
}
