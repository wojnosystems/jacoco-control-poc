package com.wojnosystems;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.function.Consumer;

public class Main {
    private static final String  ENV_DUMPER_PORT = "DUMPER_PORT";
    private static final Integer DUMPER_PORT_DEFAULT = 8082;
    private static final String  ENV_JACOCO_CLI_PATH = "JACOCO_PATH";
    private static final String  JACOCO_CLI_PATH_DEFAULT = "jacoco/jacococli.jar";
    private static final String  ENV_SERVER_HOST = "SERVER_HOST";
    private static final String  SERVER_HOST_DEFAULT = "web-instrumented";
    private static final String  ENV_SERVER_PORT = "SERVER_PORT";
    private static final String  SERVER_PORT_DEFAULT = "8081";
    private static final String  ENV_REPORT_OUTPUT_PATH = "REPORT_OUTPUT_PATH";
    private static final String  REPORT_OUTPUT_PATH_DEFAULT = "/tmp/cover-dumper-reports";

    public static void main(final String[] args) throws IOException, InterruptedException {
        buildAndRunHttpServer((server) -> {
            server.createContext("/health/ready", (e) -> {
                e.sendResponseHeaders(200, -1);
            });
            server.createContext("/reset", (e) -> {
                final String jacocoPath = Optional.ofNullable(System.getenv(ENV_JACOCO_CLI_PATH)).orElse(JACOCO_CLI_PATH_DEFAULT);
                final String cmd = "java -jar " + jacocoPath + " report "
                final Process process = Runtime.getRuntime().exec("");
                e.sendResponseHeaders(200, -1);
            });
            server.createContext("/dump-for-test", (e) -> {
                e.sendResponseHeaders(200, -1);
            });
        });
    }

    private static void buildAndRunHttpServer(final Consumer<HttpServer> serverHandler) throws IOException, InterruptedException {
        final Integer port = Optional.ofNullable(System.getenv(ENV_DUMPER_PORT)).
                filter((x) -> !x.isEmpty()).
                map(Integer::valueOf)
                .orElse(DUMPER_PORT_DEFAULT);

        final HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", port), 10);

        serverHandler.accept(server);

        final Object exitSignal = new Object();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            synchronized (exitSignal) {
                exitSignal.notifyAll();
            }
        }));

        server.start();
        System.out.println("server running on port: " + server.getAddress().getPort());

        synchronized (exitSignal) {
            exitSignal.wait();
        }
        System.out.println("server stopping");
        server.stop(0);
        System.out.println("server stopped");
    }
}
