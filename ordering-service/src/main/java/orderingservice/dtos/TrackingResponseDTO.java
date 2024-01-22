package orderingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import orderingservice.models.Courier;
import orderingservice.models.Post;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingResponseDTO {

    private String id;
    private LocalDateTime timestamp;
    private String orderId;
    private Post post;
    private Courier courier;

}
