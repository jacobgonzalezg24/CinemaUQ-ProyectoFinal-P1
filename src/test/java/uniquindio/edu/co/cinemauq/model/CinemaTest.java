package uniquindio.edu.co.cinemauq.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/** Pruebas de Cinema. Responsable: Alison (se escribe al final, cuando las demás clases estén listas). */
class CinemaTest {

    // ---------- Películas ----------
    @Test @Disabled("Pendiente") void registrarPeliculaLaAgregaALaLista() {}
    @Test @Disabled("Pendiente") void registrarPeliculaConCodigoRepetidoLanzaPeliculaDuplicadaException() {}
    @Test @Disabled("Pendiente") void buscarPeliculaInexistenteLanzaPeliculaNoEncontradaException() {}
    @Test @Disabled("Pendiente") void deshabilitarPeliculaCambiaSuEstadoANoDisponible() {}

    // ---------- Salas ----------
    @Test @Disabled("Pendiente") void registrarSalaLaAgregaALaLista() {}
    @Test @Disabled("Pendiente") void registrarSalaConCodigoRepetidoLanzaSalaDuplicadaException() {}
    @Test @Disabled("Pendiente") void buscarSalaInexistenteLanzaSalaNoEncontradaException() {}
    @Test @Disabled("Pendiente") void deshabilitarSalaCambiaSuEstadoAFueraDeServicio() {}

    // ---------- Clientes y empleados ----------
    @Test @Disabled("Pendiente") void registrarClienteConIdentificacionRepetidaLanzaClienteDuplicadoException() {}
    @Test @Disabled("Pendiente") void buscarClienteInexistenteLanzaClienteNoEncontradoException() {}
    @Test @Disabled("Pendiente") void registrarEmpleadoConIdentificacionRepetidaLanzaEmpleadoDuplicadoException() {}
    @Test @Disabled("Pendiente") void buscarEmpleadoInexistenteLanzaEmpleadoNoEncontradoException() {}

    // ---------- Tipos de entrada ----------
    @Test @Disabled("Pendiente") void cinemaIniciaConLosCuatroTiposDeEntrada() {}
    @Test @Disabled("Pendiente") void buscarTipoEntradaInexistenteLanzaTipoEntradaNoEncontradaException() {}
    @Test @Disabled("Pendiente") void modificarDescuentoActualizaElTipoDeEntrada() {}

    // ---------- Funciones ----------
    @Test @Disabled("Pendiente") void programarFuncionEnSalaDisponibleLaAgregaALaLista() {}
    @Test @Disabled("Pendiente") void programarFuncionEnSalaFueraDeServicioLanzaSalaNoDisponibleException() {}
    @Test @Disabled("Pendiente") void buscarFuncionInexistenteLanzaFuncionNoEncontradaException() {}

    // ---------- Ventas ----------
    @Test @Disabled("Pendiente") void venderEntradaOcupaElAsientoYRegistraLaVenta() {}
    @Test @Disabled("Pendiente") void venderEnFuncionInexistenteLanzaFuncionNoEncontradaException() {}
    @Test @Disabled("Pendiente") void venderUnAsientoOcupadoLanzaAsientoOcupadoException() {}
    @Test @Disabled("Pendiente") void venderConLaFuncionLlenaLanzaSinAsientosDisponiblesException() {}
    @Test @Disabled("Pendiente") void cancelarVentaLiberaElAsientoYCambiaElEstadoDeLaVenta() {}
    @Test @Disabled("Pendiente") void cancelarVentaInexistenteLanzaVentaNoEncontradaException() {}

    // ---------- Reportes ----------
    @Test @Disabled("Pendiente") void totalEntradasVendidasCuentaSoloLasVentasActivas() {}
    @Test @Disabled("Pendiente") void totalIngresosSumaElValorPagadoDeLasVentasActivas() {}
    @Test @Disabled("Pendiente") void peliculaMasVendidaRetornaLaDeMayorCantidadDeEntradas() {}
    @Test @Disabled("Pendiente") void ocupacionFuncionRetornaDisponiblesYOcupados() {}
    @Test @Disabled("Pendiente") void funcionesPorFechaRetornaSoloLasDeEsaFecha() {}
}
