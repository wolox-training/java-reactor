package wolox.trainingreactor.services;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterService {

    private static ConnectableFlux twitterStream;

    public static synchronized ConnectableFlux getTwitterStream() {
        if (twitterStream == null) {
            initTwitterStream();
        }
        return twitterStream;
    }

    private static void initTwitterStream() {
        Flux<Status> stream = Flux.create(emitter -> {
            StatusListener listener = new StatusListener() {

                @Override
                public void onException(Exception e) {
                    emitter.error(e);
                }
                @Override
                public void onDeletionNotice(StatusDeletionNotice arg) {
                }
                @Override
                public void onScrubGeo(long userId, long upToStatusId) {
                }
                @Override
                public void onStallWarning(StallWarning warning) {
                }
                @Override
                public void onStatus(Status status) {
                    emitter.next(status);
                }
                @Override
                public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                    System.out.println(numberOfLimitedStatuses);
                }
            };

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                .setOAuthConsumerKey("CONSUMER_KEY")
                .setOAuthConsumerSecret("CONSUMER_SECRET")
                .setOAuthAccessToken("ACCESS_TOKEN")
                .setOAuthAccessTokenSecret("ACCESS_TOKEN_SECRET");

            TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
            twitterStream.addListener(listener);
            twitterStream.sample();

        });
        twitterStream = stream.publish();
        twitterStream.connect();
    }

}
