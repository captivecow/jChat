package io.github.captivecow;

import io.github.captivecow.shared.Connect;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Objects;

public class ChatClient {
    private final String HOST = "localhost";
    private final int PORT = 8080;

    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private ChatEncoder chatEncoder;
    private Channel clientChannel;

    public ChatClient() {
        group = new NioEventLoopGroup(1);
        bootstrap = new Bootstrap();
        chatEncoder = new ChatEncoder();
    }

    public void connect() {
        System.out.println("Connecting..");
        try {
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.remoteAddress(new InetSocketAddress(HOST, PORT));
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline().addLast(chatEncoder);
                }
            });
            ChannelFuture f = bootstrap.connect().sync();
            f.addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    clientChannel = channelFuture.channel();
                    System.out.println("Success! Saved client channel");
                    Connect connect = Connect.newBuilder().setId(99).build();
                    clientChannel.write(connect.toByteArray());
                }
            });
        } catch (Exception e) {
            disconnect();
            throw new RuntimeException(e);
        }
    }

    public void disconnect(){
        if(Objects.nonNull(clientChannel) && clientChannel.isActive()){
            clientChannel.close().awaitUninterruptibly();
            group.shutdownGracefully();
        }
    }
}
