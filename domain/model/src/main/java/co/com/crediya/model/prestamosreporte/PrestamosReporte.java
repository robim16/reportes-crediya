package co.com.crediya.model.prestamosreporte;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PrestamosReporte {
    private String id;
    private Long cantidad;
    private Long montoTotal;
}
