package co.com.crediya.model.prestamosreporte.gateways;

import co.com.crediya.model.prestamosreporte.PrestamosReporte;
import co.com.crediya.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public interface PrestamosReporteRepository {
    Mono<PrestamosReporte> obtenerReporte();
    Mono<Void> incrementarContador(Solicitud solicitud);
}
