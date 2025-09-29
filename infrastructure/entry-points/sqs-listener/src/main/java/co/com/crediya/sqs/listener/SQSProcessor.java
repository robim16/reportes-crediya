package co.com.crediya.sqs.listener;

import co.com.crediya.usecase.reportes.ReportesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final ReportesUseCase reportesUseCase;

    @Override
    public Mono<Void> apply(Message message) {
        return Mono.just(message)
                .doOnNext(msg -> {
                    System.out.println("Mensaje recibido desde SQS:");
                    System.out.println(msg.body());
                })
                .flatMap(msg -> reportesUseCase.incrementarPrestamos())
                .onErrorResume(ex -> {
                    System.err.println("Error procesando mensaje: " + ex.getMessage());
                    ex.printStackTrace();
                    return Mono.empty();
                });
    }
}
