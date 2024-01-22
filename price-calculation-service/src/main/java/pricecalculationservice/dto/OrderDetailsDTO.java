package pricecalculationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pricecalculationservice.utils.TypeOfService;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDTO {

    private double weight;
    private TypeOfService typeOfService;
    private String sourcePostId;
    private String destinationPostId;

}
