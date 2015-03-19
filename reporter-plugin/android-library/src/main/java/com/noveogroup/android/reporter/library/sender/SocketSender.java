package com.noveogroup.android.reporter.library.sender;

import com.google.gson.Gson;
import com.noveogroup.android.reporter.library.events.Message;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

public class SocketSender implements Sender {

    private static final Gson GSON = new Gson();

    private final String host;
    private final int port;
    private Socket socket;

    public SocketSender(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void send(String applicationId, String deviceId, List<Message<?>> messages) throws IOException {
        if (socket == null) {
            socket = new Socket(host, port);
        }


        PrintStream printStream = new PrintStream(socket.getOutputStream());

        printStream.println("---------- BEGIN ----------");
        printStream.println("application id : " + applicationId);
        printStream.println("device id      : " + deviceId);
        for (Message<?> message : messages) {
            printStream.println(GSON.toJson(message));
        }
        printStream.println("----------  END  ----------");
    }

}
