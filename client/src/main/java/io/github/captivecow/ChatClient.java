package io.github.captivecow;

import com.badlogic.gdx.Gdx;
import io.github.captivecow.shared.ClientMessage;
import io.github.captivecow.shared.Connect;
import io.github.captivecow.shared.Message;
import io.github.captivecow.shared.ServerMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatClient {
    private final String HOST = "localhost";
    private final int PORT = 8080;

    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private ChatEncoder chatEncoder;
    private Channel clientChannel;
    private final ExecutorService clientPool;
    private final ChatGame game;


    public ChatClient(ChatGame game) {
        group = new NioEventLoopGroup(1);
        bootstrap = new Bootstrap();
        chatEncoder = new ChatEncoder();
        clientPool = Executors.newFixedThreadPool(1);
        this.game = game;
    }

    public void connect(String username) {
        System.out.println("Connecting..");
        try {
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.remoteAddress(new InetSocketAddress(HOST, PORT));
            bootstrap.handler(new ChatClientInitializer(this));
            ChannelFuture f = bootstrap.connect().sync();
            f.addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    clientChannel = channelFuture.channel();
                    Connect connect = Connect.newBuilder()
                            .setUsername(username)
                            .build();
                    ClientMessage message = ClientMessage.newBuilder()
                            .setConnect(connect)
                            .setId(Message.CLIENT_CONNECT.getId()).build();
                    clientChannel.write(message.toByteArray());
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
        clientPool.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if (!clientPool.awaitTermination(1, TimeUnit.SECONDS)) {
                clientPool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!clientPool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            clientPool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    public void submit(ServerMessage message){
        clientPool.submit(() -> handleMessage(message));
    }

    public void handleMessage(ServerMessage message){
//        System.out.println(Thread.currentThread().getName());
        if(message.getId() == Message.SERVER_CONNECT.getId()){
            Gdx.app.postRunnable(() -> game.addInitialUsers(message.getUsers()));
        } else if (message.getId() == Message.SERVER_DISCONNECT.getId()) {
            Gdx.app.postRunnable(() -> game.removeUser(message.getDisconnect().getUsername()));
        } else if(message.getId() == Message.SERVER_JOIN.getId()){
            Gdx.app.postRunnable(() -> game.addJoinedUser(message.getJoin().getUsername()));
        }
    }
}
