package cc.sakurarain.mcpatchclientagent;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Handler implements HttpHandler {
    private String url_prefix;

    public Handler(String url_prefix) {
        this.url_prefix = url_prefix;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String filename = exchange.getRequestURI().getPath().substring(1);
        InputStream inputStream = Tools.getInputStream(this.url_prefix + filename, null);
        exchange.sendResponseHeaders(200, 0);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            exchange.getResponseBody().write(buffer, 0, len);
        }
        exchange.getResponseBody().close();

    }
}
