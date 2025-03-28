package io.github.captivecow;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;

public class ChatEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ByteBufAllocator allocator = ctx.alloc();
        ByteBuf message = allocator.directBuffer().writeBytes((byte[]) msg);
        ChannelFuture f = ctx.writeAndFlush(message);
        f.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                System.out.println("Successful write to server..");
            } else {
                System.out.println(channelFuture.cause());
            }
        });
        ReferenceCountUtil.release(message);
        promise.setSuccess();
    }
}
