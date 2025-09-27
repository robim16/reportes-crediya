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

        System.out.println("mensaje recibido de la cola " + message);
        try {

            return reportesUseCase.incrementarPrestamos();

        } catch (Exception e) {
            return Mono.error(new RuntimeException("Error al procesar el mensaje de SQS", e));
        }
    }
}
