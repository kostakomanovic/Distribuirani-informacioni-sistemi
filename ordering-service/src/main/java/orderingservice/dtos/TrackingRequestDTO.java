package orderingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import orderingservice.models.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingRequestDTO {

    private String orderId;
    private Post currentPost;
    private String status;

}
