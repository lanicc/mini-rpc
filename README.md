# mini-rpc
rpc demo


启动provider

```java
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
```

启动consumer
```java
        Config config =
                new Config()
                        .setRefClasses(Collections.singletonList(UserApi.class))
                        .setSerializer(new FastJsonSerializer())
                        .setServerSocketAddr(new InetSocketAddress("127.0.0.1", 8091));


        ClientBootstrap clientBootstrap = new ClientBootstrap(config);
        clientBootstrap.init();
        clientBootstrap.start();

```

引用&调用测试
```java
        UserApi userApi = clientBootstrap.getReference(UserApi.class);
        userApi.add(new User("1", "lanicc", new Date()));
        User user = userApi.findById("1");
        System.out.println(user);
        List<User> users = userApi.list();
        System.out.println(users);
```
