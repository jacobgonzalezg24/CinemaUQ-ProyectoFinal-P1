package uniquindio.edu.co.cinemauq.model;

import static org.junit.jupiter.api.Assertions.*;

// @Test marca un método como prueba: JUnit lo ejecuta solo.
import org.junit.jupiter.api.Test;

import uniquindio.edu.co.cinemauq.model.enums.EstadoAsiento;
import uniquindio.edu.co.cinemauq.model.exceptions.AsientoOcupadoException;

// Pruebas de Asiento. Responsable: Alison.
class AsientoTest {

    // así que en cada prueba el asiento arranca limpio (fila A, columna 1, disponible).
    private Asiento asiento = new Asiento('A', 1);

    // 1. Verifica que el constructor arme el código del asiento uniendo la fila y la columna.

    @Test
    void constructorArmaElCodigoConFilaYColumna() {
        Asiento otro = new Asiento('C', 7);

        assertEquals("A1", asiento.getCodigoAsiento());   // 'A' + 1 = "A1"
        assertEquals("C7", otro.getCodigoAsiento());      // 'C' + 7 = "C7"
        assertEquals('C', otro.getFila());                // la fila se guardó bien
        assertEquals(7, otro.getColumna());               // la columna se guardó bien
    }

    // 2. Verifica que un asiento recién creado esté DISPONIBLE.
    @Test
    void asientoNuevoNaceDisponible() {
        assertEquals(EstadoAsiento.DISPONIBLE, asiento.getEstado());
        assertTrue(asiento.estaDisponible());
    }

    // 3. Verifica que ocupar() cambie el estado a OCUPADO.
    // "throws AsientoOcupadoException" va porque ocupar() puede lanzarla y Java obliga a declararlo.
    @Test
    void ocuparCambiaElEstadoAOcupado() throws AsientoOcupadoException {
        asiento.ocupar();

        assertEquals(EstadoAsiento.OCUPADO, asiento.getEstado());
        assertFalse(asiento.estaDisponible());
    }

    // 4. Verifica que ocupar un asiento que YA está ocupado lance AsientoOcupadoException.
    // assertThrows recibe la excepción esperada y el código que debería lanzarla ("() -> ...").
    @Test
    void ocuparUnAsientoOcupadoLanzaAsientoOcupadoException() throws AsientoOcupadoException {
        asiento.ocupar();                                  // primera vez: funciona

        assertThrows(AsientoOcupadoException.class, () -> asiento.ocupar());   // segunda vez: falla
        assertEquals(EstadoAsiento.OCUPADO, asiento.getEstado());              // sigue ocupado
    }

    //5. Verifica que liberar() deje el asiento otra vez DISPONIBLE.

    @Test
    void liberarCambiaElEstadoADisponible() throws AsientoOcupadoException {
        asiento.ocupar();
        assertEquals(EstadoAsiento.OCUPADO, asiento.getEstado());   // antes: ocupado

        asiento.liberar();

        assertEquals(EstadoAsiento.DISPONIBLE, asiento.getEstado()); // después: disponible
        assertTrue(asiento.estaDisponible());
    }

    //6. Verifica que estaDisponible() siempre refleje el estado actual del asiento.

    @Test
    void estaDisponibleReflejaElEstado() throws AsientoOcupadoException {
        assertTrue(asiento.estaDisponible());              // recién creado: true

        asiento.ocupar();
        assertFalse(asiento.estaDisponible());             // ocupado: false

        asiento.liberar();
        assertTrue(asiento.estaDisponible());              // liberado: true otra vez

        asiento.setEstado(EstadoAsiento.OCUPADO);          // también si se cambia el estado a mano
        assertFalse(asiento.estaDisponible());
    }
}
