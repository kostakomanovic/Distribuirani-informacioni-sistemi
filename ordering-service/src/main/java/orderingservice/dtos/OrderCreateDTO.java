package orderingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import orderingservice.models.Post;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDTO {

    private String userId;
    private String userName;
    private String userLastName;
    private String userAddress;
    private String userPhoneNumber;

    private String productName;
    private double productWeight;
    private Post sourcePost;
    private Post destinationPost;
    private String typeOfService;

}
