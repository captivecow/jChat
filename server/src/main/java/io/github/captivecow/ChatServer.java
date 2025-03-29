package io.github.captivecow;

import io.github.captivecow.shared.ClientMessage;
import io.github.captivecow.shared.Message;
import io.github.captivecow.shared.ServerMessage;
import io.github.captivecow.shared.Users;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private final int PORT = 8080;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;

    private ConcurrentHashMap<ChannelId, ConnectedUser> clients;
    private ChatDecoder decoder;
    private final ExecutorService servicePool;


    public ChatServer() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(2);
        bootstrap = new ServerBootstrap();
        clients = new ConcurrentHashMap<>();
        servicePool = Executors.newFixedThreadPool(1);
        decoder = new ChatDecoder(this);
    }

    public void start() {
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.localAddress(new InetSocketAddress(PORT));
        bootstrap.childHandler(new ChatServerInitializer(this));
        try {
            ChannelFuture f = bootstrap.bind().sync();
            f.addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    System.out.println("Server started on port: " + PORT);
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void submit(ClientMessage clientMessage, Channel channel){
        servicePool.submit(() -> handleMessage(clientMessage, channel));
    }

    public void handleMessage(ClientMessage clientMessage, Channel channel){
        if (clientMessage.getId() == Message.CONNECT.getId()){
            ConnectedUser newUser = new ConnectedUser(clientMessage.getConnect().getUsername(), channel);
            clients.put(channel.id(), newUser);

            Users.Builder users = Users.newBuilder();
            for(ConnectedUser user: clients.values()){
                users.addUser(user.username());
            }
            ServerMessage serverMessage = ServerMessage.newBuilder()
                    .setUsers(users.build())
                    .setId(1)
                    .build();
            System.out.println("Here now?");
            channel.writeAndFlush(serverMessage.toByteArray());
        }
    }
}
