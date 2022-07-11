package io.github.lanicc.mrpc.test.api;

import io.github.lanicc.mrpc.stream.StreamObserver;

import java.util.List;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
public interface UserApi {

    void add(User user);

    User findById(String id);

    List<User> list();

    void iterate(StreamObserver<User> userStreamObserver);
}
