package httpServer;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpServer {
    private URL url;
    private HttpsURLConnection connection;
    private int responseCode;
    private String responseLine;
    private int sizeOfAsset;
    private String name;

    public HttpServer() {
    }

    public void makeGetRequest(String requestUrl) {
        try {
            url = new URL(requestUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            responseCode = connection.getResponseCode();
            responseLine = connection.getResponseMessage();
            sizeOfAsset = connection.getContentLength() / 1024;
            name = url.getFile().substring(url.getFile().lastIndexOf("/") + 1, url.getFile().length());
        }catch (Exception ignored) {
        }
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseLine() {
        return responseLine;
    }

    public int getSizeOfAsset() {
        return sizeOfAsset;
    }

    public String getName() {
        return name;
    }
}
