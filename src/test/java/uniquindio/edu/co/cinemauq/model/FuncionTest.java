package uniquindio.edu.co.cinemauq.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import uniquindio.edu.co.cinemauq.model.enums.Clasificacion;
import uniquindio.edu.co.cinemauq.model.enums.EstadoAsiento;
import uniquindio.edu.co.cinemauq.model.enums.EstadoFuncion;
import uniquindio.edu.co.cinemauq.model.enums.EstadoPelicula;
import uniquindio.edu.co.cinemauq.model.enums.EstadoSala;
import uniquindio.edu.co.cinemauq.model.exceptions.AsientoNoEncontradoException;
import uniquindio.edu.co.cinemauq.model.exceptions.AsientoOcupadoException;

import java.time.LocalDate;
import java.time.LocalTime;

class FuncionTest {

    // Los objetos se declaran en orden porque unos dependen de otros.
    // Cada @Test recibe objetos nuevos, así que una prueba no afecta a otra.
    private Pelicula pelicula = new Pelicula(1, "Avatar", "Fantasía", 192,
            Clasificacion.MAYORES_12, EstadoPelicula.DISPONIBLE, "avatar.jpeg");
    private Sala sala = new Sala("S1", "Sala 1", 2, 3, EstadoSala.DISPONIBLE);   // 2 filas x 3 columnas = 6 asientos
    private Funcion funcion = new Funcion(100, pelicula, sala,
            LocalDate.of(2026, 11, 20), LocalTime.of(18, 0), 10000.0);

    // 1. Verifica que una función nueva quede PROGRAMADA.

    @Test
    void funcionNuevaNaceProgramada() {
        assertEquals(EstadoFuncion.PROGRAMADA, funcion.getEstado());
        assertTrue(funcion.estaProgramada());
    }

    // 2. Verifica que la matriz de asientos de la función tenga las mismas dimensiones que la sala.

    @Test
    void laMatrizTieneLasMismasDimensionesQueLaSala() {
        Asiento[][] matriz = funcion.getMatrizAsientos();

        assertEquals(sala.getNumeroFilas(), matriz.length);          // 2 filas
        assertEquals(sala.getNumeroColumnas(), matriz[0].length);    // 3 columnas
        assertEquals("A1", matriz[0][0].getCodigoAsiento());         // primer asiento
        assertEquals("B3", matriz[1][2].getCodigoAsiento());         // último asiento
    }

    // 3. Verifica que ocupar un asiento en la FUNCIÓN no cambie los asientos de la SALA:
    // la función tiene su propia copia de asientos.

    @Test
    void ocuparUnAsientoDeLaFuncionNoAfectaLosAsientosDeLaSala() throws AsientoNoEncontradoException, AsientoOcupadoException {
        funcion.ocuparAsiento("A1");

        assertEquals(EstadoAsiento.OCUPADO, funcion.buscarAsiento("A1").getEstado());   // en la función: ocupado
        assertTrue(sala.getAsientos().get(0).estaDisponible());                         // en la sala (A1 es el primero): sigue disponible
    }

    // 4. Verifica que dos funciones en la misma sala tengan ocupación independiente.

    @Test
    void dosFuncionesDeLaMismaSalaTienenOcupacionIndependiente() throws AsientoNoEncontradoException, AsientoOcupadoException {
        Funcion funcion2 = new Funcion(101, pelicula, sala,
                LocalDate.of(2026, 11, 20), LocalTime.of(21, 0), 10000.0);

        funcion.ocuparAsiento("A1");                       // ocupamos solo en la primera función

        assertEquals(1, funcion.contarOcupados());
        assertEquals(0, funcion2.contarOcupados());        // la segunda no se enteró
        assertTrue(funcion2.buscarAsiento("A1").estaDisponible());
    }

    // 5. Verifica que ocuparAsiento cambie el estado del asiento y actualice los contadores.

    @Test
    void ocuparAsientoCambiaSuEstadoYActualizaLosContadores() throws AsientoNoEncontradoException, AsientoOcupadoException {
        funcion.ocuparAsiento("A1");

        assertEquals(EstadoAsiento.OCUPADO, funcion.buscarAsiento("A1").getEstado());
        assertEquals(5, funcion.contarDisponibles());      // 6 - 1
        assertEquals(1, funcion.contarOcupados());
    }

    // 6. Verifica que ocupar un asiento ya ocupado lance AsientoOcupadoException.

    @Test
    void ocuparUnAsientoOcupadoLanzaAsientoOcupadoException() throws AsientoNoEncontradoException, AsientoOcupadoException {
        funcion.ocuparAsiento("A1");

        assertThrows(AsientoOcupadoException.class, () -> funcion.ocuparAsiento("A1"));
        assertEquals(1, funcion.contarOcupados());         // no se ocupó otro más
    }

    // 7. Verifica que ocupar un asiento que no existe lance AsientoNoEncontradoException.

    @Test
    void ocuparUnAsientoInexistenteLanzaAsientoNoEncontradoException() {
        assertThrows(AsientoNoEncontradoException.class, () -> funcion.ocuparAsiento("Z9"));
        assertEquals(0, funcion.contarOcupados());         // no se ocupó ninguno
    }

    // 8. Verifica que liberarAsiento deje el asiento DISPONIBLE otra vez.

    @Test
    void liberarAsientoLoDejaDisponible() throws AsientoNoEncontradoException, AsientoOcupadoException {
        funcion.ocuparAsiento("B2");
        assertFalse(funcion.buscarAsiento("B2").estaDisponible());   // antes: ocupado

        funcion.liberarAsiento("B2");

        assertTrue(funcion.buscarAsiento("B2").estaDisponible());    // después: disponible
        assertEquals(0, funcion.contarOcupados());
    }

    // 9. Verifica los tres contadores: totales, disponibles y ocupados.

    @Test
    void contarTotalesDisponiblesYOcupados() throws AsientoNoEncontradoException, AsientoOcupadoException {
        assertEquals(6, funcion.contarTotales());          // 2 x 3
        assertEquals(6, funcion.contarDisponibles());
        assertEquals(0, funcion.contarOcupados());

        funcion.ocuparAsiento("A1");
        funcion.ocuparAsiento("A2");

        assertEquals(6, funcion.contarTotales());          // el total no cambia
        assertEquals(4, funcion.contarDisponibles());
        assertEquals(2, funcion.contarOcupados());
    }

    // 10. Verifica que hayDisponibles() sea false cuando se ocupan los 6 asientos.

    @Test
    void hayDisponiblesEsFalsoCuandoLaFuncionEstaLlena() throws AsientoNoEncontradoException, AsientoOcupadoException {
        assertTrue(funcion.hayDisponibles());              // al inicio hay sitio

        funcion.ocuparAsiento("A1");
        funcion.ocuparAsiento("A2");
        funcion.ocuparAsiento("A3");
        funcion.ocuparAsiento("B1");
        funcion.ocuparAsiento("B2");
        funcion.ocuparAsiento("B3");

        assertFalse(funcion.hayDisponibles());             // ya no hay
        assertEquals(0, funcion.contarDisponibles());
    }

    // 11. Verifica que el mapa en texto muestre el código y el estado de cada asiento.

    @Test
    void mostrarMapaAsientosIncluyeCodigoYEstadoDeCadaAsiento() throws AsientoNoEncontradoException, AsientoOcupadoException {
        funcion.ocuparAsiento("A1");

        String mapa = funcion.mostrarMapaAsientos();

        assertTrue(mapa.contains("A1 (OCUPADO)"));         // el ocupado
        assertTrue(mapa.contains("A2 (DISPONIBLE)"));      // uno libre
        assertTrue(mapa.contains("B3 (DISPONIBLE)"));      // el último también aparece
    }
}