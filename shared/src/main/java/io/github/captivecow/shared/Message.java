package io.github.captivecow.shared;

public enum Message {
    CLIENT_CONNECT(1),
    SERVER_CONNECT(2),
    SERVER_JOIN(3),
    CHAT(4),
    CLIENT_DISCONNECT(5),
    SERVER_DISCONNECT(6);

    private final int id;

    Message(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

//    public static Message getMessageEnum(int id){
//        return switch (id) {
//            case 1 -> CONNECT;
//            case 2 -> CHAT;
//            default -> throw new RuntimeException("Enum " + id + "does not exist.");
//        };
//    }

}
