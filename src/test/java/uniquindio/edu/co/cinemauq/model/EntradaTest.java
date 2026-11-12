package uniquindio.edu.co.cinemauq.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

// Pruebas de lo común a toda Entrada (polimorfismo). Responsable: Jacob.
// Entrada es abstracta (no se puede hacer new Entrada), por eso se guardan las hijas
// en variables de tipo Entrada. Eso es polimorfismo: una variable "padre" que apunta a un objeto "hijo".
// assertEquals con decimales (double) lleva un tercer valor, 0.001: es el margen de error permitido,
// porque los decimales en el computador no son exactos. Sigue siendo assertEquals.

class EntradaTest {

    private Entrada general = new EntradaGeneral();
    private Entrada estudiante = new EntradaEstudiante(20.0);
    private Entrada nino = new EntradaNino(30.0);
    private Entrada adultoMayor = new EntradaAdultoMayor(25.0);

    // 1. Verifica que cada tipo de entrada tenga su código y su nombre.

    @Test
    void cadaTipoTieneSuCodigoYSuNombre() {
        assertEquals(1, general.getCodigoEntrada());
        assertEquals("General", general.getNombre());
        assertEquals(2, estudiante.getCodigoEntrada());
        assertEquals("Estudiante", estudiante.getNombre());
        assertEquals(3, nino.getCodigoEntrada());
        assertEquals("Niño", nino.getNombre());
        assertEquals(4, adultoMayor.getCodigoEntrada());
        assertEquals("Adulto Mayor", adultoMayor.getNombre());
    }

    // 2. Verifica que setPorcentajeDescuento cambie el porcentaje guardado.

    @Test
    void setPorcentajeDescuentoActualizaElPorcentaje() {
        assertEquals(20.0, estudiante.getPorcentajeDescuento(), 0.001);   // antes

        estudiante.setPorcentajeDescuento(50.0);

        assertEquals(50.0, estudiante.getPorcentajeDescuento(), 0.001);   // después
    }

    // 3. Verifica el polimorfismo: aunque todas las variables sean de tipo Entrada,
    // calcularValor() usa la fórmula del tipo REAL de cada objeto. Valor base: 10000.

    @Test
    void elPolimorfismoUsaLaFormulaDelTipoRealDeLaEntrada() {
        double valorBase = 10000.0;

        assertEquals(10000.0, general.calcularValor(valorBase), 0.001);      // General: sin descuento
        assertEquals(8000.0, estudiante.calcularValor(valorBase), 0.001);    // Estudiante: -20%
        assertEquals(7000.0, nino.calcularValor(valorBase), 0.001);          // Niño: -30%
        assertEquals(7500.0, adultoMayor.calcularValor(valorBase), 0.001);   // Adulto Mayor: -25%
    }
}