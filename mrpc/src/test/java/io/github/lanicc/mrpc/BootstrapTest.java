//package io.github.lanicc.mrpc;
//
//import io.github.lanicc.mrpc.serialization.fastjson.FastJsonSerializer;
//import io.github.lanicc.mrpc.user.User;
//import io.github.lanicc.mrpc.user.UserApi;
//import io.github.lanicc.mrpc.user.UserServiceImpl;
//import org.junit.jupiter.api.Test;
//
//import java.net.InetSocketAddress;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created on 2022/7/11.
// *
// * @author lan
// */
//class BootstrapTest {
//
//    @Test
//    void start() throws InterruptedException {
//        Config config =
//                new Config()
//                .setRefClasses(Collections.singletonList(UserApi.class))
//                .setSerializer(new FastJsonSerializer())
//                .setServerSocketAddr(new InetSocketAddress("127.0.0.1", 8091));
//
//        ServerConfig serverConfig = ServerConfig.copy(config);
//        serverConfig.exportService(UserApi.class, new UserServiceImpl());
//
//        ServerBootstrap serverBootstrap = new ServerBootstrap(serverConfig);
//        serverBootstrap.init();
//        serverBootstrap.start();
//
//        ClientBootstrap clientBootstrap = new ClientBootstrap(config);
//        clientBootstrap.init();
//        clientBootstrap.start();
//
//        UserApi userApi = clientBootstrap.getReference(UserApi.class);
//        userApi.add(new User("1", "lanicc", new Date()));
//        User user = userApi.findById("1");
//        System.out.println(user);
//        List<User> users = userApi.list();
//        System.out.println(users);
//
//        TimeUnit.SECONDS.sleep(10);
//        clientBootstrap.stop();
//        serverBootstrap.stop();
//    }
//
//    public static void main(String[] args) {
//        Config config =
//                new Config()
//                        .setRefClasses(Collections.singletonList(UserApi.class))
//                        .setSerializer(new FastJsonSerializer())
//                        .setServerSocketAddr(new InetSocketAddress("127.0.0.1", 8091));
//        //ServerBootstrap serverBootstrap = new ServerBootstrap(config);
//        //serverBootstrap.init();
//        //serverBootstrap.start();
//    }
//}
