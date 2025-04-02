package io.github.captivecow;

import com.google.protobuf.InvalidProtocolBufferException;
import io.github.captivecow.shared.ClientMessage;
import io.github.captivecow.shared.Disconnect;
import io.github.captivecow.shared.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ServerChatDecoder extends ChannelInboundHandlerAdapter {

    private final ChatServer server;

    public ServerChatDecoder(ChatServer server){
        this.server = server;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InvalidProtocolBufferException {
        try {
            ByteBuf in = (ByteBuf) msg;
            int length = in.readableBytes();
            byte[] array = new byte[length];
            in.getBytes(in.readerIndex(), array);
            ClientMessage message = ClientMessage.parseFrom(array);
            server.submit(message, ctx.channel());
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Disconnect disconnect = Disconnect
                .newBuilder()
                .setConnectionId(ctx.channel().id().asLongText())
                .build();
        ClientMessage message = ClientMessage
                .newBuilder()
                .setId(Message.CLIENT_DISCONNECT.getId())
                .setDisconnect(disconnect)
                .build();
        server.submit(message);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
