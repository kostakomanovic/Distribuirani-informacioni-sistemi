package postsservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import postsservice.controllers.UtilsController;

@RunWith(SpringRunner.class)
@WebFluxTest(UtilsController.class)
public class PostServiceUtilsControllerIntegrationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getDistanceTest() {
        webTestClient.get()
                .uri("/mileage/from/123/to/456")
                .exchange()
                .expectStatus().isOk();
    }
}
