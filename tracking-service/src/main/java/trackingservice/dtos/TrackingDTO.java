package trackingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import trackingservice.models.Courier;
import trackingservice.models.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingDTO {

    private String orderId;
    private Post post;
    private Courier courier;

}
