package io.github.lanicc.mrpc.stream;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
public interface StreamObserver<T> {


    void onNext(T t);

    void onCompleted();

    StreamObserver<?> INSTANCE = new StreamObserver<Object>() {
        @Override
        public void onNext(Object o) {

        }

        @Override
        public void onCompleted() {

        }
    };
}
