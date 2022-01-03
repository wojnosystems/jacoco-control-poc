package com.wojnosystems;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Simulates a webservice so that I can test how jacoco works with external integration tests
 */
public class Main {
    final static String ENV_WEB_PORT = "WEB_PORT";
    final static Integer DEFAULT_PORT = 8080;

    public static void main(final String[] args) throws IOException, InterruptedException {
        buildAndRunHttpServer((server) -> {
            server.createContext("/m1", (e) -> {
                Covered.m1();
                e.sendResponseHeaders(200, -1);
            });
            server.createContext("/m2", (e) -> {
                Covered.m2();
                e.sendResponseHeaders(200, -1);
            });
            server.createContext("/m1-m2", (e) -> {
                Covered.m1();
                Covered.m2();
                e.sendResponseHeaders(200, -1);
            });
        });
    }

    private static void buildAndRunHttpServer(final Consumer<HttpServer> context) throws IOException, InterruptedException {
        final Integer port = Optional.ofNullable(System.getenv(ENV_WEB_PORT)).
                filter((x) -> !x.isEmpty()).
                map(Integer::valueOf)
                .orElse(DEFAULT_PORT);

        final HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", port), 10);

        context.accept(server);

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
