package uniquindio.edu.co.cinemauq.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import uniquindio.edu.co.cinemauq.model.enums.Cargo;
import uniquindio.edu.co.cinemauq.model.enums.Clasificacion;
import uniquindio.edu.co.cinemauq.model.enums.EstadoPelicula;
import uniquindio.edu.co.cinemauq.model.enums.EstadoSala;
import uniquindio.edu.co.cinemauq.model.enums.EstadoVenta;
import uniquindio.edu.co.cinemauq.model.exceptions.*;

import java.time.LocalDate;
import java.time.LocalTime;

// Cómo está hecho:
// - "throws Exception" en varios métodos: Cinema tiene métodos que pueden lanzar excepciones y Java obliga a declararlas.
// Con "throws Exception" se cubren todas sin escribir una lista larga. No cambia lo que se prueba.
// Un Cinema nuevo ya trae: 5 películas, 4 tipos de entrada (General, Estudiante, Niño, Adulto Mayor)
// y 1 empleado administrador (código 111, "Jacob Gonzalez"). Los códigos de función empiezan en 101 y los de venta en 1001.
// - assertEquals con decimales lleva un tercer valor (0.001): el margen de error permitido.

class CinemaTest {

    // Cinema nuevo en cada prueba (JUnit crea un objeto nuevo de esta clase por cada @Test).
    private Cinema cinema = new Cinema();

    // Fecha de ejemplo para programar funciones.
    private LocalDate fecha = LocalDate.of(2026, 11, 20);

    // Método de ayuda (NO es una prueba: no lleva @Test). Deja listo lo necesario para probar ventas:
    //  una sala de 3x4 (12 asientos), un cliente (1001) y una función (código 101) de la película 101 a las 18:00, valor base 10000.

    private void prepararEscenario() throws Exception {
        cinema.registrarSala(new Sala("S1", "Sala 1", 3, 4, EstadoSala.DISPONIBLE));
        cinema.registrarCliente(new Cliente("1001", "Ana"));
        cinema.programarFuncion(101, "S1", fecha, LocalTime.of(18, 0), 10000.0);
    }

    // ---------- Películas ----------

    // 1. Verifica que registrar una película nueva la agregue a la lista.

    @Test
    void registrarPeliculaLaAgregaALaLista() throws Exception {
        Pelicula nueva = new Pelicula(200, "Matrix", "Acción", 136,
                Clasificacion.MAYORES_15, EstadoPelicula.DISPONIBLE, "matrix.jpg");

        cinema.registrarPelicula(nueva);

        assertEquals(6, cinema.getListaPeliculas().size());      // 5 base + 1
        assertTrue(cinema.verificarPelicula(200));
        assertEquals(nueva, cinema.buscarPelicula(200));
    }

    // 2. Verifica que registrar una película con un código que ya existe lance PeliculaDuplicadaException.

    @Test
    void registrarPeliculaConCodigoRepetidoLanzaPeliculaDuplicadaException() {
        // el código 101 ya existe entre las películas base
        Pelicula repetida = new Pelicula(101, "Otra", "Drama", 100,
                Clasificacion.TODOS, EstadoPelicula.DISPONIBLE, "otra.jpg");

        assertThrows(PeliculaDuplicadaException.class, () -> cinema.registrarPelicula(repetida));
        assertEquals(5, cinema.getListaPeliculas().size());      // no se agregó
    }

    //3. Verifica que buscar una película que no existe lance PeliculaNoEncontradaException.

    @Test
    void buscarPeliculaInexistenteLanzaPeliculaNoEncontradaException() {
        assertFalse(cinema.verificarPelicula(999));
        assertThrows(PeliculaNoEncontradaException.class, () -> cinema.buscarPelicula(999));
    }

    // 4. Verifica que deshabilitar una película la deje NO_DISPONIBLE.

    @Test
    void deshabilitarPeliculaCambiaSuEstadoANoDisponible() throws Exception {
        cinema.deshabilitarPelicula(101);

        assertEquals(EstadoPelicula.NO_DISPONIBLE, cinema.buscarPelicula(101).getEstado());
        assertEquals(4, cinema.consultarPeliculasDisponibles().size());   // una menos disponible
        assertEquals(5, cinema.consultarPeliculas().size());              // pero la película sigue existiendo
    }

    // ---------- Salas ----------

    // 5. Verifica que registrar una sala la agregue a la lista.

    @Test
    void registrarSalaLaAgregaALaLista() throws Exception {
        Sala sala = new Sala("S1", "Sala 1", 3, 4, EstadoSala.DISPONIBLE);

        cinema.registrarSala(sala);

        assertEquals(1, cinema.getListaSalas().size());
        assertTrue(cinema.verificarSala("S1"));
        assertEquals(sala, cinema.buscarSala("S1"));
    }

    // 6. Verifica que registrar una sala con código repetido lance SalaDuplicadaException.

    @Test
    void registrarSalaConCodigoRepetidoLanzaSalaDuplicadaException() throws Exception {
        cinema.registrarSala(new Sala("S1", "Sala 1", 3, 4, EstadoSala.DISPONIBLE));
        Sala repetida = new Sala("S1", "Otra sala", 2, 2, EstadoSala.DISPONIBLE);

        assertThrows(SalaDuplicadaException.class, () -> cinema.registrarSala(repetida));
        assertEquals(1, cinema.getListaSalas().size());           // no se agregó
    }

    // 7. Verifica que buscar una sala que no existe lance SalaNoEncontradaException.

    @Test
    void buscarSalaInexistenteLanzaSalaNoEncontradaException() {
        assertFalse(cinema.verificarSala("S99"));
        assertThrows(SalaNoEncontradaException.class, () -> cinema.buscarSala("S99"));
    }

    // 8. Verifica que deshabilitar una sala la deje FUERA_DE_SERVICIO.


    @Test
    void deshabilitarSalaCambiaSuEstadoAFueraDeServicio() throws Exception {
        cinema.registrarSala(new Sala("S1", "Sala 1", 3, 4, EstadoSala.DISPONIBLE));

        cinema.deshabilitarSala("S1");

        assertEquals(EstadoSala.FUERA_DE_SERVICIO, cinema.buscarSala("S1").getEstado());
    }

    // ---------- Clientes y empleados ----------

    //9. Verifica que registrar un cliente con una identificación repetida lance ClienteDuplicadoException.

    @Test
    void registrarClienteConIdentificacionRepetidaLanzaClienteDuplicadoException() throws Exception {
        cinema.registrarCliente(new Cliente("1001", "Ana"));
        Cliente repetido = new Cliente("1001", "Otra persona");     // misma identificación

        assertThrows(ClienteDuplicadoException.class, () -> cinema.registrarCliente(repetido));
        assertEquals(1, cinema.getListaClientes().size());          // no se agregó
    }

    //10. Verifica que buscar un cliente que no existe lance ClienteNoEncontradoException.

    @Test
    void buscarClienteInexistenteLanzaClienteNoEncontradoException() {
        assertFalse(cinema.verificarCliente("9999"));
        assertThrows(ClienteNoEncontradoException.class, () -> cinema.buscarCliente("9999"));
    }

    // 11. Verifica que registrar un empleado repetido lance EmpleadoDuplicadoException.
    // OJO: Cinema detecta el duplicado por el CÓDIGO del empleado (no por la identificación),
    // por eso aquí se repite el código 111, que es el del administrador que ya viene creado.

    @Test
    void registrarEmpleadoConIdentificacionRepetidaLanzaEmpleadoDuplicadoException() {
        Empleado repetido = new Empleado(111, "12345678", "Otro", Cargo.OPERADOR);

        assertThrows(EmpleadoDuplicadoException.class, () -> cinema.registrarEmpleado(repetido));
        assertEquals(1, cinema.getListaEmpleados().size());         // sigue solo el administrador
    }

    // 12. Verifica que buscar un empleado que no existe lance EmpleadoNoEncontradoException.

    @Test
    void buscarEmpleadoInexistenteLanzaEmpleadoNoEncontradoException() {
        assertFalse(cinema.verificarEmpleado(999));
        assertThrows(EmpleadoNoEncontradoException.class, () -> cinema.buscarEmpleado(999));
    }

    // ---------- Tipos de entrada ----------

    // 13. Verifica que el Cinema arranque con los 4 tipos de entrada.

    @Test
    void cinemaIniciaConLosCuatroTiposDeEntrada() throws Exception {
        assertEquals(4, cinema.consultarTiposEntrada().size());
        assertEquals("General", cinema.buscarTipoEntrada(1).getNombre());
        assertEquals("Estudiante", cinema.buscarTipoEntrada(2).getNombre());
        assertEquals("Niño", cinema.buscarTipoEntrada(3).getNombre());
        assertEquals("Adulto Mayor", cinema.buscarTipoEntrada(4).getNombre());
    }

    // 14. Verifica que buscar un tipo de entrada que no existe lance TipoEntradaNoEncontradaException.

    @Test
    void buscarTipoEntradaInexistenteLanzaTipoEntradaNoEncontradaException() {
        assertThrows(TipoEntradaNoEncontradaException.class, () -> cinema.buscarTipoEntrada(99));
    }

    // 15. Verifica que modificarDescuento cambie el porcentaje del tipo de entrada.

    @Test
    void modificarDescuentoActualizaElTipoDeEntrada() throws Exception {
        assertEquals(20.0, cinema.buscarTipoEntrada(2).getPorcentajeDescuento(), 0.001);   // Estudiante: 20% al inicio

        cinema.modificarDescuento(2, 50.0);

        assertEquals(50.0, cinema.buscarTipoEntrada(2).getPorcentajeDescuento(), 0.001);
        assertEquals(5000.0, cinema.buscarTipoEntrada(2).calcularValor(10000.0), 0.001);   // y el valor calculado cambia
    }

    // ---------- Funciones ----------

    // 16. Verifica que programar una función en una sala disponible la agregue a la lista.

    @Test
    void programarFuncionEnSalaDisponibleLaAgregaALaLista() throws Exception {
        cinema.registrarSala(new Sala("S1", "Sala 1", 3, 4, EstadoSala.DISPONIBLE));

        Funcion f = cinema.programarFuncion(101, "S1", fecha, LocalTime.of(18, 0), 10000.0);

        assertEquals(101, f.getCodigoFuncion());                  // el primer código de función es 101
        assertEquals(1, cinema.getListaFunciones().size());
        assertTrue(cinema.verificarFuncion(101));
        assertEquals(12, f.contarDisponibles());                  // sala 3x4 = 12 asientos libres
    }

    // 17. Verifica que programar una función en una sala FUERA_DE_SERVICIO lance SalaNoDisponibleException.

    @Test
    void programarFuncionEnSalaFueraDeServicioLanzaSalaNoDisponibleException() throws Exception {
        cinema.registrarSala(new Sala("S1", "Sala 1", 3, 4, EstadoSala.FUERA_DE_SERVICIO));

        assertThrows(SalaNoDisponibleException.class,
                () -> cinema.programarFuncion(101, "S1", fecha, LocalTime.of(18, 0), 10000.0));
        assertEquals(0, cinema.getListaFunciones().size());       // no se programó
    }

    // 18. Verifica que buscar una función que no existe lance FuncionNoEncontradaException.

    @Test
    void buscarFuncionInexistenteLanzaFuncionNoEncontradaException() {
        assertFalse(cinema.verificarFuncion(999));
        assertThrows(FuncionNoEncontradaException.class, () -> cinema.buscarFuncion(999));
    }

    // ---------- Ventas ----------

    // 19. Verifica que vender una entrada ocupe el asiento y registre la venta.
    // Entrada tipo 2 (Estudiante, 20%) sobre valor base 10000 = 8000.

    @Test
    void venderEntradaOcupaElAsientoYRegistraLaVenta() throws Exception {
        prepararEscenario();

        Venta venta = cinema.venderEntrada("1001", 111, 101, 2, "A1");

        assertEquals(1001, venta.getCodigoVenta());               // el primer código de venta es 1001
        assertEquals(8000.0, venta.getValorPagado(), 0.001);
        assertEquals(1, cinema.getListaVentas().size());
        assertTrue(cinema.verificarVenta(1001));
        assertFalse(cinema.buscarFuncion(101).buscarAsiento("A1").estaDisponible());   // el asiento quedó ocupado
    }

    // 20. Verifica que vender en una función que no existe lance FuncionNoEncontradaException.

    @Test
    void venderEnFuncionInexistenteLanzaFuncionNoEncontradaException() throws Exception {
        prepararEscenario();

        assertThrows(FuncionNoEncontradaException.class,
                () -> cinema.venderEntrada("1001", 111, 999, 1, "A1"));
        assertEquals(0, cinema.getListaVentas().size());          // no se registró nada
    }

    // 21. Verifica que vender un asiento ya ocupado lance AsientoOcupadoException.

    @Test
    void venderUnAsientoOcupadoLanzaAsientoOcupadoException() throws Exception {
        prepararEscenario();
        cinema.venderEntrada("1001", 111, 101, 1, "A1");          // se vende A1

        assertThrows(AsientoOcupadoException.class,
                () -> cinema.venderEntrada("1001", 111, 101, 1, "A1"));   // venderlo otra vez falla
        assertEquals(1, cinema.getListaVentas().size());          // solo existe la primera venta
    }

    // 22. Verifica que vender cuando la función está llena lance SinAsientosDisponiblesException.
    // Se usa una sala mínima de 1x1 (un solo asiento) para llenarla con una venta.

    @Test
    void venderConLaFuncionLlenaLanzaSinAsientosDisponiblesException() throws Exception {
        cinema.registrarSala(new Sala("S2", "Sala Mini", 1, 1, EstadoSala.DISPONIBLE));
        cinema.registrarCliente(new Cliente("1001", "Ana"));
        cinema.programarFuncion(101, "S2", fecha, LocalTime.of(18, 0), 10000.0);
        cinema.venderEntrada("1001", 111, 101, 1, "A1");          // con esto la sala queda llena

        assertThrows(SinAsientosDisponiblesException.class,
                () -> cinema.venderEntrada("1001", 111, 101, 1, "A1"));
    }

    // 23. Verifica que cancelar una venta libere el asiento y cambie el estado de la venta a CANCELADA.

    @Test
    void cancelarVentaLiberaElAsientoYCambiaElEstadoDeLaVenta() throws Exception {
        prepararEscenario();
        cinema.venderEntrada("1001", 111, 101, 1, "A1");
        assertFalse(cinema.buscarFuncion(101).buscarAsiento("A1").estaDisponible());   // antes: ocupado

        cinema.cancelarVenta(1001);

        assertEquals(EstadoVenta.CANCELADA, cinema.buscarVenta(1001).getEstado());
        assertTrue(cinema.buscarFuncion(101).buscarAsiento("A1").estaDisponible());    // después: libre
    }

    // 24. Verifica que cancelar una venta que no existe lance VentaNoEncontradaException.

    @Test
    void cancelarVentaInexistenteLanzaVentaNoEncontradaException() {
        assertFalse(cinema.verificarVenta(9999));
        assertThrows(VentaNoEncontradaException.class, () -> cinema.cancelarVenta(9999));
    }

    // ---------- Reportes ----------

    // 25. Verifica que totalEntradasVendidas cuente solo las ventas ACTIVAS (las canceladas no cuentan).

    @Test
    void totalEntradasVendidasCuentaSoloLasVentasActivas() throws Exception {
        prepararEscenario();
        cinema.venderEntrada("1001", 111, 101, 1, "A1");          // venta 1001
        cinema.venderEntrada("1001", 111, 101, 1, "A2");          // venta 1002
        cinema.venderEntrada("1001", 111, 101, 1, "A3");          // venta 1003
        assertEquals(3, cinema.totalEntradasVendidas());

        cinema.cancelarVenta(1002);                               // cancelamos una

        assertEquals(2, cinema.totalEntradasVendidas());          // quedan 2 activas
    }

    // 26. Verifica que totalIngresos sume el valor pagado de las ventas ACTIVAS.
    // General (10000) + Estudiante (8000) = 18000. Si se cancela la de 8000, quedan 10000.

    @Test
    void totalIngresosSumaElValorPagadoDeLasVentasActivas() throws Exception {
        prepararEscenario();
        cinema.venderEntrada("1001", 111, 101, 1, "A1");          // General: 10000 (venta 1001)
        cinema.venderEntrada("1001", 111, 101, 2, "A2");          // Estudiante: 8000 (venta 1002)
        assertEquals(18000.0, cinema.totalIngresos(), 0.001);

        cinema.cancelarVenta(1002);

        assertEquals(10000.0, cinema.totalIngresos(), 0.001);
    }

    // 27. Verifica que peliculaMasVendida devuelva la película con más entradas vendidas.
    // Película 101: 2 entradas; película 103 (Enredados): 1 entrada.

    @Test
    void peliculaMasVendidaRetornaLaDeMayorCantidadDeEntradas() throws Exception {
        prepararEscenario();                                      // función 101 = película 101
        cinema.registrarSala(new Sala("S2", "Sala 2", 3, 4, EstadoSala.DISPONIBLE));
        cinema.programarFuncion(103, "S2", fecha, LocalTime.of(18, 0), 10000.0);   // función 102 = película 103
        cinema.venderEntrada("1001", 111, 101, 1, "A1");
        cinema.venderEntrada("1001", 111, 101, 1, "A2");
        cinema.venderEntrada("1001", 111, 102, 1, "A1");

        Pelicula masVendida = cinema.peliculaMasVendida();

        assertEquals(101, masVendida.getCodigoPelicula());
    }

    // 28. Verifica que ocupacionFuncion informe los asientos totales, disponibles y ocupados.

    @Test
    void ocupacionFuncionRetornaDisponiblesYOcupados() throws Exception {
        prepararEscenario();
        cinema.venderEntrada("1001", 111, 101, 1, "A1");
        cinema.venderEntrada("1001", 111, 101, 1, "A2");

        String ocupacion = cinema.ocupacionFuncion(101);

        assertTrue(ocupacion.contains("Totales: 12"));
        assertTrue(ocupacion.contains("Disponibles: 10"));
        assertTrue(ocupacion.contains("Ocupados: 2"));
    }

    // 29. Verifica que funcionesPorFecha devuelva solo las funciones de la fecha pedida.

    @Test
    void funcionesPorFechaRetornaSoloLasDeEsaFecha() throws Exception {
        cinema.registrarSala(new Sala("S1", "Sala 1", 3, 4, EstadoSala.DISPONIBLE));
        cinema.programarFuncion(101, "S1", fecha, LocalTime.of(14, 0), 10000.0);                 // función 101: día 20
        cinema.programarFuncion(101, "S1", fecha, LocalTime.of(18, 0), 10000.0);                 // función 102: día 20
        cinema.programarFuncion(101, "S1", fecha.plusDays(1), LocalTime.of(14, 0), 10000.0);     // función 103: día 21

        assertEquals(2, cinema.funcionesPorFecha(fecha).size());
        assertEquals(1, cinema.funcionesPorFecha(fecha.plusDays(1)).size());
        assertEquals(0, cinema.funcionesPorFecha(fecha.plusDays(5)).size());
    }
}
