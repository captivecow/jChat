package io.github.captivecow.shared;

public enum Message {
    CONNECT(1) {
        @Override
        void handle() {

        }
    },
    DISCONNECT(2) {
        @Override
        void handle() {

        }
    },
    CHAT(3) {
        @Override
        void handle() {

        }
    };

    private final int id;

    Message(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    abstract void handle();

}
