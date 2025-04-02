package io.github.captivecow;

import io.netty.channel.Channel;

public record ConnectedUser(String username, Channel channel) {
    @Override
    public String toString() {
        return "Username: " + this.username + ", Channel ID: " + channel.id();
    }
}
