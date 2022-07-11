package io.github.lanicc.mrpc.remote;

import io.github.lanicc.mrpc.remote.proto.Protocol;
import io.github.lanicc.mrpc.remote.proto.Request;
import io.github.lanicc.mrpc.remote.proto.Response;
import io.github.lanicc.mrpc.serialization.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.IOException;
import java.util.List;

/**
 * Created on 2022/7/9.
 *
 * @author lan
 */
public class ProtoCodec extends ByteToMessageCodec<Protocol> {

    private final Serializer serializer;

    public ProtoCodec(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Protocol protocol, ByteBuf out) throws Exception {
       encode(protocol, out);
    }

    private void encode(Protocol protocol, ByteBuf out) throws IOException {
        boolean isRequest = protocol instanceof Request;
        out.writeBoolean(isRequest);
        if (isRequest) {
            //((Request) protocol).isStream()
        }
        ByteBufOutputStream outputStream = new ByteBufOutputStream(out);
        serializer.write(protocol, outputStream);
        outputStream.close();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        boolean isRequest = in.readBoolean();
        Class<? extends Protocol> protoClass = isRequest ? Request.class : Response.class;
        ByteBufInputStream inputStream = new ByteBufInputStream(in);
        Protocol protocol = serializer.read(protoClass, inputStream);
        out.add(protocol);
    }
}
