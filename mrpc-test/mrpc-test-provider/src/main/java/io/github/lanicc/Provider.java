package io.github.lanicc;

import io.github.lanicc.mrpc.Config;
import io.github.lanicc.mrpc.ServerBootstrap;
import io.github.lanicc.mrpc.ServerConfig;
import io.github.lanicc.mrpc.serialization.fastjson.FastJsonSerializer;
import io.github.lanicc.mrpc.test.api.UserApi;

import java.net.InetSocketAddress;
import java.util.Collections;

/**
 * Created on ${DATE}.
 *
 * @author lan
 */
public class Provider {
    public static void main(String[] args) {
        Config config =
                new Config()
                        .setRefClasses(Collections.singletonList(UserApi.class))
                        .setSerializer(new FastJsonSerializer())
                        .setServerSocketAddr(new InetSocketAddress("127.0.0.1", 8091));
        ServerConfig serverConfig = ServerConfig.copy(config);
        serverConfig.exportService(UserApi.class, new UserApiImpl());

        ServerBootstrap serverBootstrap = new ServerBootstrap(serverConfig);
        serverBootstrap.init();
        serverBootstrap.start();

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> serverBootstrap.stop());

    }
}
