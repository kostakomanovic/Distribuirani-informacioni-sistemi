package trackingservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tracking {

    @Id
    private String id;
    private LocalDateTime timestamp;
    private String orderId;
    private Post post;
    private Courier courier;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tracking tracking = (Tracking) o;
        return Objects.equals(id, tracking.id) && Objects.equals(orderId, tracking.orderId) && Objects.equals(post.getId(), tracking.post.getId()) && Objects.equals(courier.getId(), tracking.courier.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, orderId, post, courier);
    }
}
