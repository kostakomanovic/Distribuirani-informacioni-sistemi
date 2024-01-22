package trackingservice.utils;

import trackingservice.dtos.TrackingDTO;
import trackingservice.models.Tracking;

import java.time.LocalDateTime;

public class Mapper {

    public static Tracking trackingDTOtoEntity(TrackingDTO trackingDTO) {
        return new Tracking(null, LocalDateTime.now(), trackingDTO.getOrderId(), trackingDTO.getPost(), trackingDTO.getCourier());
    }

}
