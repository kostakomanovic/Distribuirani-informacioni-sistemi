package orderingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import orderingservice.models.Post;
import orderingservice.utils.OrderStatus;
import orderingservice.utils.TypeOfService;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private String id;
    private LocalDateTime timestamp;
    private OrderStatus status;

    private String userId;
    private String userName;
    private String userLastName;
    private String userAddress;
    private String userPhoneNumber;

    private String productName;
    private double productWeight;
    private Post sourcePost;
    private Post destinationPost;
    private double price;
    private TypeOfService typeOfService;

}
