package io.github.captivecow;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ChatServerInitializer extends ChannelInitializer<SocketChannel> {

    private final ChatServer server;

    public ChatServerInitializer(ChatServer server){
        this.server = server;
    }
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(new ChatEncoder());
        socketChannel.pipeline().addLast(new ChatDecoder(server));
    }
}
