package uniquindio.edu.co.cinemauq.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import uniquindio.edu.co.cinemauq.model.enums.Clasificacion;
import uniquindio.edu.co.cinemauq.model.enums.EstadoPelicula;

class PeliculaTest {

    // Película de ejemplo, disponible. Se recrea limpia en cada @Test.
    private Pelicula pelicula = new Pelicula(1, "Enredados", "Animación", 93,
            Clasificacion.TODOS, EstadoPelicula.DISPONIBLE, "enredados.png");

    // 1. Verifica que deshabilitar() ponga la película en NO_DISPONIBLE.

    @Test
    void deshabilitarCambiaElEstadoANoDisponible() {
        assertTrue(pelicula.estaDisponible());             // antes: disponible

        pelicula.deshabilitar();

        assertEquals(EstadoPelicula.NO_DISPONIBLE, pelicula.getEstado());
        assertFalse(pelicula.estaDisponible());
    }

    // 2. Verifica que modificar() cambie todos los datos de la película (menos el código).

    @Test
    void modificarActualizaLosDatosDeLaPelicula() {
        pelicula.modificar("Intensa-Mente 2", "Comedia", 96,
                Clasificacion.MAYORES_7, EstadoPelicula.NO_DISPONIBLE, "intensa.png");

        assertEquals("Intensa-Mente 2", pelicula.getNombre());
        assertEquals("Comedia", pelicula.getGenero());
        assertEquals(96, pelicula.getDuracion());
        assertEquals(Clasificacion.MAYORES_7, pelicula.getClasificacion());
        assertEquals(EstadoPelicula.NO_DISPONIBLE, pelicula.getEstado());
        assertEquals("intensa.png", pelicula.getImagen());
        assertEquals(1, pelicula.getCodigoPelicula());     // el código no cambia
    }
}

