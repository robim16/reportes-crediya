package co.com.crediya.api;

import co.com.crediya.usecase.reportes.ReportesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final ReportesUseCase useCase;

    public Mono<ServerResponse> obtener(ServerRequest request) {
        return useCase.obtenerCantidadPrestamos()
                .flatMap(res -> ServerResponse.ok().bodyValue(res));
    }
}
