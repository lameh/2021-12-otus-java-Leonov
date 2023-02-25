package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.service.SequenceNumbersServiceImpl;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Server {

    private static final Logger log = LoggerFactory.getLogger(Server.class);
    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var remoteServer = new SequenceNumbersServiceImpl();
        var server = ServerBuilder.forPort(SERVER_PORT).addService(remoteServer).build();
        server.start();
        log.info("Server started");

        server.awaitTermination();
        log.info("Server stopped");
    }
}
