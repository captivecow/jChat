package io.github.captivecow;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ChatClientInitializer extends ChannelInitializer<SocketChannel> {

    private final ChatClient client;

    public ChatClientInitializer(ChatClient client){
        this.client = client;
    }
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(new ChatEncoder());
        socketChannel.pipeline().addLast(new ChatDecoder(client));
    }
}

