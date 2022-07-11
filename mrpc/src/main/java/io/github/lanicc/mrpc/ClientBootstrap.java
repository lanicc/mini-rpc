package io.github.lanicc.mrpc;

import io.github.lanicc.mrpc.remote.NettyClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created on 2022/7/9.
 *
 * @author lan
 */
public class ClientBootstrap extends AbstractBootstrap {

    private Map<Class<?>, Object> references;

    private NettyClient nettyClient;

    private ProxyFactory proxyFactory;

    public ClientBootstrap(Config config) {
        super(config);
    }

    @Override
    public void doInit() {
        this.nettyClient = new NettyClient(config);
        this.proxyFactory = new ProxyFactory(this.nettyClient);
        initReference();
    }
    @Override
    public void doStart() {
        nettyClient.start();
    }

    @Override
    public void doStop() {
        nettyClient.shutdown();
    }

    private void initReference() {
        Map<Class<?>, Object> referencesTmp = new HashMap<>();

        List<Class<?>> refClasses = config.getRefClasses();
        for (Class<?> refClass : refClasses) {
            Object proxy = proxyFactory.newProxy(refClass);
            referencesTmp.put(refClass, proxy);
        }

        this.references = Collections.unmodifiableMap(referencesTmp);
    }

    @SuppressWarnings("unchecked")
    public <T> T getReference(Class<T> clazz) {
        checkRunning();
        Object o = references.get(clazz);
        if (Objects.isNull(o)) {
            throw new NullPointerException("no such service: " + clazz);
        }
        return (T) o;
    }


}
