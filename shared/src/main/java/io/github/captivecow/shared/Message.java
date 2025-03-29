package io.github.captivecow.shared;

public enum Message {
    CONNECT(1),
    CHAT(2);

    private final int id;

    Message(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Message getMessageEnum(int id){
        return switch (id) {
            case 1 -> CONNECT;
            case 2 -> CHAT;
            default -> throw new RuntimeException("Enum " + id + "does not exist.");
        };
    }

}
