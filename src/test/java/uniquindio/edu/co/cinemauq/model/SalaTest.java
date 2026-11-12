package uniquindio.edu.co.cinemauq.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import uniquindio.edu.co.cinemauq.model.enums.EstadoAsiento;
import uniquindio.edu.co.cinemauq.model.enums.EstadoSala;

class SalaTest {

    // Sala de ejemplo: 3 filas (A, B, C) x 4 columnas (1 a 4) = 12 asientos.
    private Sala sala = new Sala("S1", "Sala 1", 3, 4, EstadoSala.DISPONIBLE);

    // 1. Verifica que la capacidad sea filas x columnas.

    @Test
    void laCapacidadEsFilasPorColumnas() {
        assertEquals(12, sala.getCapacidadTotal());        // 3 x 4

        sala.setNumeroFilas(5);                            // ahora 5 x 4
        assertEquals(20, sala.getCapacidadTotal());
    }

    // 2. Verifica que la sala genere los asientos con las dimensiones correctas.
    // (En Sala los asientos están en una lista, no en una matriz: se revisa que tenga filas x columnas elementos.)

    @Test
    void laMatrizSeGeneraConLasDimensionesCorrectas() {
        assertEquals(12, sala.getAsientos().size());       // 3 x 4 asientos

        sala.setNumeroColumnas(2);                         // al cambiar el tamaño se vuelven a generar
        assertEquals(6, sala.getAsientos().size());        // 3 x 2
    }

    //3. Verifica que todos los asientos de la sala empiecen DISPONIBLES.
    // Se recorre la lista con un for y se revisa uno por uno.

    @Test
    void todosLosAsientosNacenDisponibles() {
        for (Asiento a : sala.getAsientos()) {
            assertTrue(a.estaDisponible());
            assertEquals(EstadoAsiento.DISPONIBLE, a.getEstado());
        }
    }

    // 4. Verifica que el código de cada asiento corresponda a su posición:
    // las filas son letras (A, B, C...) y las columnas números desde 1.

    @Test
    void losCodigosDeLosAsientosCorrespondenASuPosicion() {
        assertEquals("A1", sala.getAsientos().get(0).getCodigoAsiento());    // primero
        assertEquals("A4", sala.getAsientos().get(3).getCodigoAsiento());    // fin de la fila A
        assertEquals("B1", sala.getAsientos().get(4).getCodigoAsiento());    // empieza la fila B
        assertEquals("C4", sala.getAsientos().get(11).getCodigoAsiento());   // último
        assertEquals('B', sala.getAsientos().get(4).getFila());
        assertEquals(1, sala.getAsientos().get(4).getColumna());
    }

    // 5. PENDIENTE: la clase Sala no tiene un método buscarAsiento (esa búsqueda está en Funcion y en Asiento.buscarEnMatriz).
    //  Por eso no se puede probar aquí sin inventar código; queda desactivada.

    @Test @Disabled("Sala no tiene buscarAsiento")
    void buscarAsientoExistenteRetornaElAsiento() {}

    // 6. PENDIENTE: igual que la anterior, Sala no tiene buscarAsiento.

    @Test @Disabled("Sala no tiene buscarAsiento")
    void buscarAsientoInexistenteLanzaAsientoNoEncontradoException() {}

    // 7. PENDIENTE: igual que la anterior, Sala no tiene buscarAsiento.

    @Test @Disabled("Sala no tiene buscarAsiento")
    void buscarAsientoConFormatoInvalidoLanzaAsientoNoEncontradoException() {}

    // 8. Verifica que la sala pueda quedar FUERA_DE_SERVICIO.
    // Sala no tiene un método "deshabilitar": se hace con setEstado, que es lo mismo que hace Cinema.deshabilitarSala.

    @Test
    void deshabilitarCambiaElEstadoAFueraDeServicio() {
        assertEquals(EstadoSala.DISPONIBLE, sala.getEstado());   // antes

        sala.setEstado(EstadoSala.FUERA_DE_SERVICIO);

        assertEquals(EstadoSala.FUERA_DE_SERVICIO, sala.getEstado());   // después
    }

    // 9. Verifica que una sala fuera de servicio ya no esté disponible.
    // Sala no tiene estaDisponible(): se compara su estado contra DISPONIBLE.

    @Test
    void estaDisponibleEsFalsoSiLaSalaEstaFueraDeServicio() {
        assertTrue(sala.getEstado() == EstadoSala.DISPONIBLE);          // al inicio sí está disponible

        sala.setEstado(EstadoSala.FUERA_DE_SERVICIO);

        assertFalse(sala.getEstado() == EstadoSala.DISPONIBLE);         // ya no
    }
}
