package wolox.trainingreactor.services;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sun.rmi.runtime.Log;
import wolox.trainingreactor.models.BootResponse;
import wolox.trainingreactor.models.Bot;
import wolox.trainingreactor.models.Talk;
import wolox.trainingreactor.utils.RequestUtils;

import javax.annotation.PostConstruct;
import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BotService {
    @Value("${api.bots.uri}")
    private  String BASE_PATH;
    @Autowired
    RequestUtils requestUtils;

    private WebClient client;

    private Random rand = new Random();

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

    public Mono<BootResponse> took (Talk talk) {
        try {
            Integer length =(talk.getLength() == null)?20:talk.getLength();
            log.info("Do a request : " + BASE_PATH+ "took?name=" + talk.getName() + "&length=" + length);
            return  client.get()
                    .uri("/took?name={name}&length={length}",
                            talk.getName(), length)
                    .retrieve()
                    .bodyToMono(BootResponse.class);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Error(e.getCause());
        }
    }

    public Mono<String> conversation(List<String> names) {
        List<Talk> requests = generateConversation(names);
        List<Mono<BootResponse>> list = requests.stream()
                .map(talk -> took(new Talk(talk.getName(),talk.getLength())))
                .collect(Collectors.toList());


        AtomicReference<String> conversation = new AtomicReference<>("");
        //String conversation = "";
        Mono.when(list).block();
        for (Mono<BootResponse> monoResponse:list) {
            monoResponse.subscribe(
                    response -> {

                        conversation.set(conversation.get() + response.getName() + " : " + response.getResponse() + "\n");
                    },
                    error -> log.error(error.getMessage())
            );

        }
        Mono.when(list).block();
        log.info(conversation.get());
        return Mono.just(conversation.get());
    }

    private List<Talk> generateConversation(List<String> names) {
        List<Talk> requests  = new ArrayList<>();
        for (String name:names) {
            int n = rand.nextInt(7) + 3;
            for (int i = 0; i < n; i++){
                int length = rand.nextInt(190)+ 10;
                Talk talk = new Talk(name,length);
                requests.add(talk);
            }
        }
        return requests;
                
    }

}
