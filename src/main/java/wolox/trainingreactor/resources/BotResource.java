package wolox.trainingreactor.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twitter4j.Status;
import wolox.trainingreactor.models.Bot;
import wolox.trainingreactor.models.Took;
import wolox.trainingreactor.services.BotService;
import wolox.trainingreactor.services.TwitterService;

@RestController
public class BotResource {

    @Autowired
    BotService botService;

    @PostMapping (path = "/feed", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<Void> feed(@RequestBody Bot bot) {
        return  botService.feed(bot);
    }


    @GetMapping (path = "/took", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<String> took(@RequestParam String name, @RequestParam(required = false) Integer length) {
        return  botService.took(new Took(name,length));
    }
}
