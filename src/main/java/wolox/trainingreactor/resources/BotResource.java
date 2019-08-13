package wolox.trainingreactor.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import wolox.trainingreactor.models.BootResponse;
import wolox.trainingreactor.models.Bot;
import wolox.trainingreactor.models.Talk;
import wolox.trainingreactor.services.BotService;

import java.util.List;

@RestController
public class BotResource {

    @Autowired
    BotService botService;

    @PostMapping (path = "/feed", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<Void> feed(@RequestBody Bot bot) {
        return  botService.feed(bot);
    }


    @GetMapping (path = "/took", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<BootResponse> took(@RequestParam String name, @RequestParam(required = false) Integer length) {
        return  botService.took(new Talk(name,length));
    }

    @GetMapping (path = "/conversation", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<String> conversation(@RequestParam List<String> names) {
        return  botService.conversation(names);
    }
}
