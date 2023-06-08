
import lk.ijse.chat_application.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) {

        try {
            server server= new server(new ServerSocket(4000));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}