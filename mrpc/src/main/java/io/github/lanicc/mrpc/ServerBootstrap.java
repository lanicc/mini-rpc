package io.github.lanicc.mrpc;

import io.github.lanicc.mrpc.remote.NettyServer;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
public class ServerBootstrap extends AbstractBootstrap {

    private NettyServer nettyServer;

    public ServerBootstrap(ServerConfig config) {
        super(config);
    }

    @Override
    protected void doInit() {
        nettyServer = new NettyServer((ServerConfig) config);
    }

    @Override
    protected void doStart() {
        nettyServer.start();
    }

    @Override
    protected void doStop() {
        nettyServer.stop();
    }
}
