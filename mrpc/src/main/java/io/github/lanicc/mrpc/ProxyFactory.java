package io.github.lanicc.mrpc;

import io.github.lanicc.mrpc.remote.NettyClient;
import net.sf.cglib.proxy.Enhancer;

import java.util.Objects;

/**
 * Created on 2022/7/9.
 *
 * @author lan
 */
public class ProxyFactory {

    private final NettyClient nettyClient;

    public ProxyFactory(NettyClient nettyClient) {
        this.nettyClient = Objects.requireNonNull(nettyClient);
    }

    @SuppressWarnings("unchecked")
    public <T> T newProxy(Class<T> inf) {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{inf});
        enhancer.setCallback(new Invoker(inf, nettyClient));
        return (T) enhancer.create();
    }
}
