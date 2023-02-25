package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.ManagedChannelBuilder;
import ru.otus.protobuf.generated.SequenceNumbersRequest;
import ru.otus.protobuf.generated.SequenceNumbersServiceGrpc;
import ru.otus.streamobserver.SequenceNumbersObserver;

public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final int MAX_COUNTER = 50;
    private static final long SEQUENCE_START = 0;
    private static final long SEQUENCE_END = 30;

    public static void main(String[] args) {

        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT).usePlaintext().build();
        var stub = SequenceNumbersServiceGrpc.newStub(channel);

        log.info("Client starts");

        var streamObserver = new SequenceNumbersObserver();
        stub.generate(SequenceNumbersRequest.newBuilder().setStart(SEQUENCE_START).setEnd(SEQUENCE_END).build(), streamObserver);

        var currentValue = 0L;
        var lastReceived = SEQUENCE_START - 1;
        for(var i = 0; i < MAX_COUNTER; i++) {
            currentValue++;
            var received = streamObserver.getResponseNumber();
            if (received != lastReceived) {
                currentValue += received;
                lastReceived = received;
            }
            log.info("Last received is {}, current value is {}", lastReceived, currentValue);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                log.error("Thread is not sleep", ex);
            }
        }
        channel.shutdown();
        log.info("Client stopped");
    }
}
