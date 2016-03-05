package fr.neatmonster.agaria;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ServerConnector {
    private static final long INIT_KEY = 2200049715L;

    private static String[] getServer(final String url, final String data) {
        try {
            final HttpClient httpClient = HttpClients.createDefault();
            final HttpPost httpPost = new HttpPost("http://m.agar.io" + url);
            httpPost.setHeader("Origin", "http://agar.io");
            httpPost.setHeader("Referer", "http://agar.io");
            httpPost.setHeader("Host", "m.agar.io");
            httpPost.setEntity(new StringEntity(data));
            final HttpResponse httpResp = httpClient.execute(httpPost);
            final String entity = EntityUtils.toString(httpResp.getEntity()).trim();
            return entity.split("\n");
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getFFAServer(final String region) {
        return getServer("/", region + "\n" + INIT_KEY);
    }

    public static String[] getTeamsServer(final String region) {
        return getServer("/", region + ":teams\n" + INIT_KEY);
    }

    public static String[] getPartyServer(final String region) {
        return getServer("/", region + ":party\n" + INIT_KEY);
    }

    public static String[] getExperimentalServer(final String region) {
        return getServer("/", region + ":experimental\n" + INIT_KEY);
    }
}
