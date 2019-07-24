package wolox.trainingreactor.services;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import wolox.trainingreactor.models.Bot;
import wolox.trainingreactor.models.Took;
import wolox.trainingreactor.utils.RequestUtils;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BotService {
    @Value("${api.bots.uri}")
    private  String BASE_PATH;
    @Autowired
    RequestUtils requestUtils;

    private WebClient client;

    @PostConstruct
    private void configure(){
        log.info("Try to set base url of boot service : " + BASE_PATH);
        client = WebClient
                .builder()
                .baseUrl(BASE_PATH)
                .build();
    }

    public Mono<Void> feed (Bot bot) {
        try {
            Mono<Bot> monoBot = Mono.just(bot);
            Mono<Void> result =  client.post().uri("/feed").body(monoBot,Bot.class).retrieve().bodyToMono(Void.class);
            log.info("Bot  " + bot.getName()  + " was feeded . ");


            return  Mono.empty() ;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Error(e.getCause());
        }
    }

    public Mono<String> took (Took took) {
        try {
            return  client.get()
                    .uri("/took?name={name}",took.getName())
                    .retrieve()
                    .bodyToMono(String.class);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Error(e.getCause());
        }
    }
}
