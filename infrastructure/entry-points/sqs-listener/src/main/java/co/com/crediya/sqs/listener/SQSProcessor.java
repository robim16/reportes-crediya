package co.com.crediya.sqs.listener;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.usecase.reportes.ReportesUseCase;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final ReportesUseCase reportesUseCase;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> apply(Message message) {
        return Mono.just(message)
                .doOnNext(msg -> {
                    System.out.println("Mensaje recibido desde SQS:");
                    System.out.println(msg.body());
                })
                .flatMap(msg -> {
                    try {
                        JsonNode root = objectMapper.readTree(msg.body());
                        JsonNode payload = root.get("payload");
                        Solicitud solicitud = objectMapper.treeToValue(payload, Solicitud.class);

                        return reportesUseCase.incrementarPrestamos(solicitud);
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error al deserializar mensaje", e));
                    }
                })
                .onErrorResume(ex -> {
                    System.err.println("Error procesando mensaje: " + ex.getMessage());
                    ex.printStackTrace();
                    return Mono.empty();
                });
    }
}
