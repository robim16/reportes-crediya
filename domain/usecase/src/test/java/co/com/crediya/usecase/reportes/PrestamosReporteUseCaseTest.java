package co.com.crediya.usecase.reportes;


import co.com.crediya.model.prestamosreporte.PrestamosReporte;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.prestamosreporte.gateways.PrestamosReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PrestamosReporteUseCaseTest {

    private PrestamosReporteRepository prestamosReporteRepository;
    private ReportesUseCase prestamosReporteUseCase;

    @BeforeEach
    void setUp() {
        prestamosReporteRepository = Mockito.mock(PrestamosReporteRepository.class);
        prestamosReporteUseCase = new ReportesUseCase(prestamosReporteRepository);
    }

    @Test
    void debeObtenerCantidadDePrestamos() {

        PrestamosReporte reporte = new PrestamosReporte();
        reporte.setId("prestamos-aprobados");
        reporte.setCantidad(5L);

        when(prestamosReporteRepository.obtenerReporte())
                .thenReturn(Mono.just(reporte));

        StepVerifier.create(prestamosReporteUseCase.obtenerCantidadPrestamos())
                .expectNextMatches(r -> r.getCantidad() == 5L && "prestamos-aprobados".equals(r.getId()))
                .verifyComplete();

        verify(prestamosReporteRepository, times(1)).obtenerReporte();
    }

    @Test
    void debeIncrementarPrestamos() {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(BigInteger.valueOf(1));

        when(prestamosReporteRepository.incrementarContador(any(Solicitud.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(prestamosReporteUseCase.incrementarPrestamos(solicitud))
                .verifyComplete();

        verify(prestamosReporteRepository, times(1)).incrementarContador(solicitud);
    }

    @Test
    void debePropagarErrorCuandoFalleObtenerReporte() {

        when(prestamosReporteRepository.obtenerReporte())
                .thenReturn(Mono.error(new RuntimeException("Error en DynamoDB")));

        StepVerifier.create(prestamosReporteUseCase.obtenerCantidadPrestamos())
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().equals("Error en DynamoDB"))
                .verify();

        verify(prestamosReporteRepository).obtenerReporte();
    }

    @Test
    void debePropagarErrorCuandoFalleIncrementarPrestamos() {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(BigInteger.valueOf(3));

        when(prestamosReporteRepository.incrementarContador(any(Solicitud.class)))
                .thenReturn(Mono.error(new RuntimeException("Error al incrementar")));

        StepVerifier.create(prestamosReporteUseCase.incrementarPrestamos(solicitud))
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().equals("Error al incrementar"))
                .verify();

        verify(prestamosReporteRepository).incrementarContador(solicitud);
    }
}
