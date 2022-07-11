package io.github.lanicc;

import io.github.lanicc.mrpc.ClientBootstrap;
import io.github.lanicc.mrpc.Config;
import io.github.lanicc.mrpc.serialization.fastjson.FastJsonSerializer;
import io.github.lanicc.mrpc.test.api.User;
import io.github.lanicc.mrpc.test.api.UserApi;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created on ${DATE}.
 *
 * @author lan
 */
public class Consumer {
    public static void main(String[] args) {
        Config config =
                new Config()
                        .setRefClasses(Collections.singletonList(UserApi.class))
                        .setSerializer(new FastJsonSerializer())
                        .setServerSocketAddr(new InetSocketAddress("127.0.0.1", 8091));


        ClientBootstrap clientBootstrap = new ClientBootstrap(config);
        clientBootstrap.init();
        clientBootstrap.start();

        UserApi userApi = clientBootstrap.getReference(UserApi.class);
        userApi.add(new User("1", "lanicc", new Date()));
        User user = userApi.findById("1");
        System.out.println(user);
        List<User> users = userApi.list();
        System.out.println(users);

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> clientBootstrap.stop());
    }
}
