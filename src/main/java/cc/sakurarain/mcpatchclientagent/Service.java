package cc.sakurarain.mcpatchclientagent;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service extends Thread {
    private HttpServer httpServer;
    private boolean status = false;
    private String url_path;
    private int port;

    public Service(int port, int threads, String url_prefix) {
        try {
            this.port = port;
            this.url_path = url_prefix;
            if (this.url_path.endsWith("/") || this.url_path.endsWith("\\")) {
                this.url_path.substring(0, this.url_path.length() - 1);
            }
            System.out.println("设置主目录: " + this.url_path);
            this.httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);
            this.httpServer.setExecutor(Executors.newFixedThreadPool(threads));
        } catch (IOException e) {
            this.status = true;
        }
    }

    @Override
    public void run() {
        if (status) {
            return;
        }
        System.out.println("开始监听端口: " + this.port);
        httpServer.createContext("/", new Handler(this.url_path));
        httpServer.start();
    }

}
