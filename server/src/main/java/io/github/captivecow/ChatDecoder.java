package io.github.captivecow;

import com.google.protobuf.InvalidProtocolBufferException;
import io.github.captivecow.shared.ClientMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ChatDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InvalidProtocolBufferException {
        try {
            ByteBuf in = (ByteBuf) msg;
            int length = in.readableBytes();
            byte[] array = new byte[length];
            in.getBytes(in.readerIndex(), array);

            ClientMessage message = ClientMessage.parseFrom(array);
            System.out.println(message.toString());
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
