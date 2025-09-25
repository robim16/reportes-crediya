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
        return getById(REPORT_ID);
    }

    @Override
    public Mono<Void> incrementarContador() {
        return getById(REPORT_ID)
                .flatMap(reporte -> {
                    reporte.setCantidad(reporte.getCantidad() + 1);
                    return save(reporte).then();
                });
    }
}
