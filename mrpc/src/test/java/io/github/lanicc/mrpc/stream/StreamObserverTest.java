package io.github.lanicc.mrpc.stream;

import org.junit.jupiter.api.Test;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
class StreamObserverTest {

    @Test
    void st() {
        StreamObserver<String> clientSideObserver =
                new StreamObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        System.out.println("on next " + s);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("on completed");
                    }
                };

        StreamObserver<String> serverSideObserver = proxy(clientSideObserver);
        serverSideObserver.onNext("hello");
        serverSideObserver.onNext("hello you");

    }

    private <T> StreamObserver<T> proxy(StreamObserver<T> streamObserver) {
        return streamObserver;
    }


}
