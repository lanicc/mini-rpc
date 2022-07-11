package io.github.lanicc.mrpc.remote;

import io.github.lanicc.mrpc.ServerConfig;
import io.github.lanicc.mrpc.ServiceInvoker;
import io.github.lanicc.mrpc.remote.proto.Protocol;
import io.github.lanicc.mrpc.remote.proto.Request;
import io.github.lanicc.mrpc.remote.proto.Response;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<Protocol> {

    static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private final ServiceInvoker invoker;

    public ServerHandler(ServerConfig config) {
        this.invoker = new ServiceInvoker(config);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol protocol) throws Exception {
        logger.debug(protocol.toString());
        if (protocol instanceof Request) {
            try {
                Object result = invoker.invoke((Request) protocol);
                Response response = new Response();
                response.setRequestId(protocol.getRequestId());
                response.setData(result);
                ctx.channel().writeAndFlush(response);
            } catch (Exception e) {
                logger.error("invoke service error", e);
            }
        } else {
            logger.warn("receive unexpected message: {}", protocol);
        }
    }
}
