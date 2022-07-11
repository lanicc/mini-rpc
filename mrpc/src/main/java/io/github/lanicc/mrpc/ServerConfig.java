package io.github.lanicc.mrpc;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
public class ServerConfig extends Config {

    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();

    public <T> ServerConfig exportService(Class<T> inf, T service) {
        services.put(inf, service);
        return this;
    }

    public Map<Class<?>, Object> getServices() {
        return Collections.unmodifiableMap(services);
    }

    public static ServerConfig copy(Config config) {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setServerSocketAddr(config.getServerSocketAddr());
        serverConfig.setSerializer(config.getSerializer());
        return serverConfig;
    }

}
