package co.com.crediya.dynamodb;

import co.com.crediya.dynamodb.helper.TemplateAdapterOperations;
import co.com.crediya.model.prestamosreporte.PrestamosReporte;
import co.com.crediya.model.prestamosreporte.gateways.PrestamosReporteRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;


import java.util.List;


@Repository
public class DynamoDBTemplateAdapter extends TemplateAdapterOperations<PrestamosReporte, String, ReporteEntity> implements PrestamosReporteRepository {

    private static final String REPORT_ID = "prestamos-aprobados";
    public DynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
        super(connectionFactory, mapper, d -> mapper.map(d, PrestamosReporte.class ), "prestamos" );
    }

    @Override
    public Mono<PrestamosReporte> obtenerReporte() {
        return getById(REPORT_ID)
                .doOnSubscribe(sub -> System.out.println("Consultando reporte con ID: " + REPORT_ID))
                .doOnNext(r -> System.out.println("Reporte obtenido: " + r))
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró el reporte con ID: " + REPORT_ID)))
                .onErrorResume(ex -> {
                    System.err.println("Error al obtener el reporte: " + ex.getMessage());
                    ex.printStackTrace();
                    return Mono.error(new RuntimeException("Error obteniendo reporte desde DynamoDB", ex));
                });
    }

    @Override
    public Mono<Void> incrementarContador() {
        return getById(REPORT_ID)
                .filter(reporte -> reporte != null)
                .flatMap(reporte -> {
                    Long cantidadActual = reporte.getCantidad() != null ? reporte.getCantidad() : 0L;
                    Long nuevaCantidad = cantidadActual + 1;
                    reporte.setCantidad(nuevaCantidad);

                    System.out.printf("Incrementando contador: %d → %d%n", cantidadActual, nuevaCantidad);

                    return save(reporte)
                            .doOnSuccess(r -> System.out.println("Contador actualizado correctamente"))
                            .then();
                })
                .switchIfEmpty(Mono.defer(() -> {
                    System.out.println("No existía el reporte, creando uno nuevo con cantidad = 1");
                    PrestamosReporte nuevo = new PrestamosReporte();
                    nuevo.setId(REPORT_ID);
                    nuevo.setCantidad(1L);
                    return save(nuevo)
                            .doOnSuccess(r -> System.out.println("Reporte inicial creado"))
                            .then();
                }))
                .onErrorResume(ex -> {
                    System.err.println("Error incrementando el contador: " + ex.getMessage());
                    ex.printStackTrace();
                    return Mono.empty();
                });
    }


}
