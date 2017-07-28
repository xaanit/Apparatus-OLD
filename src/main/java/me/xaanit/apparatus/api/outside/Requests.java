package me.xaanit.apparatus.api.outside;

import me.xaanit.apparatus.GlobalVars;
import me.xaanit.apparatus.objects.Cat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class Requests {

    private static Random random = new Random();

    public static String getCuteImage() {
        try {
            return GlobalVars.gson.fromJson(get("http://random.cat/meow"), Cat.class).getFile();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "https://images-cdn.9gag.com/photo/4333047_700b.jpg";
        }
    }

    /**
     * Gets the JSON string of a URL
     *
     * @param urlString The URL string
     * @return The JSON string
     * @throws IOException If there was a problem grabbing the bot
     */
    public static String get(String urlString) throws IOException {
        String[] regex = new String[]{"<(\\/)?pre>", "<(\\/)?html>", "<(\\/)?head>", "<(\\/)?body>",
                "(<pre .+>)\\["};
        URL url = new URL(urlString);
        URLConnection con = url.openConnection(); // (HttpURLConnection)
        con.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0");
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        String fin = "";
        while ((line = br.readLine()) != null) {
            fin += line;
        }
        for (String str : regex) {
            if (str.startsWith("(pre")) {
                fin.replaceAll(str, "[");
            } else {
                fin.replaceAll(str, "");
            }
        }
        return fin;
    }
}
