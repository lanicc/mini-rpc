# mini-rpc
rpc demo, 支持普通rpc和流式调用(server-side)



## 使用方式

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


## 流式通信Streaming

接口
```java
void iterate(StreamObserver<User> userStreamObserver);
```

客户端调用
```java

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
```

服务端流式回写

```java
    @Override
    public void iterate(StreamObserver<User> userStreamObserver) {
        logger.info("iterate all user: {}", userStreamObserver.getClass());

        try {
            for (User user : users.values()) {
                userStreamObserver.onNext(user);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            userStreamObserver.onCompleted();
        }
    }

```
