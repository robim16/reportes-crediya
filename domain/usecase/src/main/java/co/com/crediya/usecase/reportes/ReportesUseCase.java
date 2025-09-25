package co.com.crediya.usecase.reportes;

import co.com.crediya.model.prestamosreporte.PrestamosReporte;
import co.com.crediya.model.prestamosreporte.gateways.PrestamosReporteRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReportesUseCase {
    private final PrestamosReporteRepository prestamosReporteRepository;
    public Mono<PrestamosReporte> obtenerCantidadPrestamos() {
        return prestamosReporteRepository.obtenerReporte();
    }
    public Mono<Void> incrementarPrestamos() {
        return prestamosReporteRepository.incrementarContador();
    }
}
