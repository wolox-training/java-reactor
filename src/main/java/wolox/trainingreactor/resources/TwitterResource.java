package wolox.trainingreactor.resources;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import twitter4j.Place;
import twitter4j.Status;
import wolox.trainingreactor.services.BotService;
import wolox.trainingreactor.services.TwitterService;

@RestController
@RequestMapping("/tweets")
public class TwitterResource {

    @GetMapping(path = "/filtered", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> filtered() {
        ConnectableFlux<Status> flux = TwitterService.getTwitterStream();

        return flux
            .filter(status -> status.getText().contains("the"))
            .map(status -> status.getText());

    }

    @GetMapping(path = "/onePerSecond", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> onePerSecond() {
        ConnectableFlux<Status> flux = TwitterService.getTwitterStream();

        Flux<Status> filtered = flux.filter(status -> {
            Place place = status.getPlace();
            if (place != null) {
                return status.getPlace().getCountryCode().equalsIgnoreCase("us");
            }
            return false;
        });

        return filtered
            .map(status -> status.getCreatedAt().toGMTString() + " " + status.getPlace().getCountryCode() + " " + status.getText());

    }

    @GetMapping(path = "/grouped", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux grouped() {
        ConnectableFlux<Status> flux = TwitterService.getTwitterStream();

        Flux<Status> filtered = flux.filter(status -> {
            Place place = status.getPlace();
            if (place != null) {
                return status.getPlace().getCountryCode().equalsIgnoreCase("us");
            }
            return false;
        });

        return Flux.interval(Duration.ofSeconds(1))
            .zipWith(filtered, (tick, status) -> status)
            .map(status -> status.getText());
    }

}
