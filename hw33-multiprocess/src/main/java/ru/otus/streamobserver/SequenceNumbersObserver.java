package ru.otus.streamobserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.SequenceNumbersResponse;

public class SequenceNumbersObserver implements StreamObserver<SequenceNumbersResponse> {

    public static final Logger log = LoggerFactory.getLogger(SequenceNumbersObserver.class);
    private long responseNumber;

    @Override
    public void onCompleted() {
        log.info("Response completed");
    }

    @Override
    public void onNext(SequenceNumbersResponse response) {
        setResponseNumber(response.getNumber());
    }

    @Override
    public void onError(Throwable error) {
        log.info("Error was received", error);
    }

    public synchronized long getResponseNumber() {
        return responseNumber;
    }

    public synchronized void setResponseNumber(long responseNumber) {
        this.responseNumber = responseNumber;
    }
}
