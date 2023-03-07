package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.SequenceNumbersRequest;
import ru.otus.protobuf.generated.SequenceNumbersServiceGrpc;
import ru.otus.protobuf.generated.SequenceNumbersResponse;

import java.util.Random;

public class SequenceNumbersServiceImpl extends SequenceNumbersServiceGrpc.SequenceNumbersServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(SequenceNumbersServiceImpl.class);
    @Override
    public void generate(SequenceNumbersRequest request, StreamObserver<SequenceNumbersResponse> responseStreamObserver) {
        for (var i = request.getStart(); i <= request.getEnd(); i++) {
            responseStreamObserver.onNext(SequenceNumbersResponse.newBuilder().setNumber(i).build());
            try {
                Thread.sleep(new Random().nextLong(5000) + 1);
            } catch (InterruptedException ex) {
                log.error("Thread is not sleep", ex);
            }
        }
        responseStreamObserver.onCompleted();
    }
}
