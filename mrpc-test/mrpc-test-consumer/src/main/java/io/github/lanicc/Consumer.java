package io.github.lanicc;

import io.github.lanicc.mrpc.ClientBootstrap;
import io.github.lanicc.mrpc.Config;
import io.github.lanicc.mrpc.serialization.fastjson.FastJsonSerializer;
import io.github.lanicc.mrpc.stream.StreamObserver;
import io.github.lanicc.mrpc.test.api.User;
import io.github.lanicc.mrpc.test.api.UserApi;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created on ${DATE}.
 *
 * @author lan
 */
public class Consumer {
    public static void main(String[] args) throws InterruptedException {
        Config config =
                new Config()
                        .setRefClasses(Collections.singletonList(UserApi.class))
                        .setSerializer(new FastJsonSerializer())
                        .setServerSocketAddr(new InetSocketAddress("127.0.0.1", 8091));


        ClientBootstrap clientBootstrap = new ClientBootstrap(config);
        clientBootstrap.init();
        clientBootstrap.start();

        UserApi userApi = clientBootstrap.getReference(UserApi.class);

        int userNum = 10;

        CountDownLatch stopWatch = new CountDownLatch(userNum);

        for (int i = 0; i < userNum; i++) {
            userApi.add(new User(i + "", "lanicc_" + i, new Date()));
        }
        //User user = userApi.findById("1");
        //System.out.println(user);
        //List<User> users = userApi.list();
        //System.out.println(users);

        userApi.iterate(new StreamObserver<User>() {
            @Override
            public void onNext(User user) {
                System.out.println("onNext: " + user);
                stopWatch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }
        });

        System.out.println("stream request called");

        stopWatch.await();
        clientBootstrap.stop();
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> clientBootstrap.stop());
    }
}
