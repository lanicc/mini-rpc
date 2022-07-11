package io.github.lanicc.mrpc;

import io.github.lanicc.mrpc.serialization.Serializer;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created on 2022/7/9.
 *
 * @author lan
 */
public class Config {

    private InetSocketAddress serverSocketAddr;

    private Serializer serializer;

    private List<Class<?>> refClasses;


    public InetSocketAddress getServerSocketAddr() {
        return serverSocketAddr;
    }

    public Config setServerSocketAddr(InetSocketAddress serverSocketAddr) {
        this.serverSocketAddr = serverSocketAddr;
        return this;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public Config setSerializer(Serializer serializer) {
        this.serializer = serializer;
        return this;
    }


    public List<Class<?>> getRefClasses() {
        return refClasses;
    }

    public Config setRefClasses(List<Class<?>> refClasses) {
        this.refClasses = refClasses;
        return this;
    }
}
