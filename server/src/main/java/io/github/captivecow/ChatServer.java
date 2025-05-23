package io.github.captivecow;

import io.github.captivecow.shared.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
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

    private ConcurrentHashMap<String, ConnectedUser> clients;
    private final ExecutorService servicePool;


    public ChatServer() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(2);
        bootstrap = new ServerBootstrap();
        clients = new ConcurrentHashMap<>();
        servicePool = Executors.newFixedThreadPool(1);
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
    public void submit(ClientMessage clientMessage){
        servicePool.submit(() -> handleMessage(clientMessage));
    }

    public void handleMessage(ClientMessage clientMessage, Channel channel){
        if (clientMessage.getId() == Message.CLIENT_CONNECT.getId()){
            ConnectedUser newUser = new ConnectedUser(clientMessage.getConnect().getUsername(), channel);
            clients.put(channel.id().asLongText(), newUser);

            Users.Builder users = Users.newBuilder();
            for(ConnectedUser user: clients.values()){
                users.addUser(user.username());
            }
            ServerMessage serverMessage = ServerMessage.newBuilder()
                    .setUsers(users.build())
                    .setId(Message.SERVER_CONNECT.getId())
                    .build();
            channel.writeAndFlush(serverMessage.toByteArray());

            ServerJoin serverJoin = ServerJoin
                    .newBuilder()
                    .setUsername(clientMessage.getConnect().getUsername())
                    .build();

            ServerMessage joinServerMessage = ServerMessage
                    .newBuilder()
                    .setId(Message.SERVER_JOIN.getId())
                    .setJoin(serverJoin)
                    .build();

            byte[] joinMessageBytes = joinServerMessage.toByteArray();

            for(ConnectedUser user: clients.values()){
                if(user.channel().id() != channel.id()){
                    user.channel().writeAndFlush(joinMessageBytes);
                }
            }
        }
    }

    public void handleMessage(ClientMessage clientMessage){
        if(clientMessage.getId() == Message.CLIENT_DISCONNECT.getId()){
            ConnectedUser removedUser = clients.remove(clientMessage.getDisconnect().getConnectionId());
            ServerDisconnect disconnect = ServerDisconnect
                    .newBuilder()
                    .setUsername(removedUser.username())
                    .build();
            ServerMessage serverMessage = ServerMessage
                    .newBuilder()
                    .setId(Message.SERVER_DISCONNECT.getId())
                    .setDisconnect(disconnect)
                    .build();
            byte[] serverMessageBytes = serverMessage.toByteArray();
            for(ConnectedUser user: clients.values()){
                user.channel().writeAndFlush(serverMessageBytes);
            }
        }
    }
}
