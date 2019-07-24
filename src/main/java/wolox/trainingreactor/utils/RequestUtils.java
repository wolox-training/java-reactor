package wolox.trainingreactor.utils;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class RequestUtils {

    private Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public List<NameValuePair> buildQuerryParams (Object requestParams) throws JSONException {
        String jsonString = gson.toJson(requestParams);
        JSONObject jsonRequest = new JSONObject(jsonString);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Iterator<String> iter = jsonRequest.keys(); iter.hasNext(); ) {
            String key = iter.next();
            String value = jsonRequest.getString(key);
            params.add(new BasicNameValuePair(key, value));
        }
        return params;
    }
}
