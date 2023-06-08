package lk.ijse.chat_application;

import lk.ijse.chat_application.service.clientManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

public class server {

    ServerSocket serverSocket;
    Socket localSocket;

    public server(ServerSocket serverSocket){
        this.serverSocket=serverSocket;


        try {

            while (!serverSocket.isClosed()){
                this.localSocket=serverSocket.accept();
                System.out.println("New Client connected..");

                clientManager newClient=new clientManager(localSocket);
                Thread thread=new Thread(newClient);
                thread.start();
            }

        }catch (IOException e){
            System.out.println(e.getMessage());
            System.out.println("Server couldn't creat !!!");
            e.printStackTrace();
            closeSever();
        }
    }

    public void closeSever(){

        try {
            if(serverSocket!=null){
                serverSocket.close();
            }
            if (localSocket!=null){
                localSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

 /*       public class GreetingServer extends Thread {
            private ServerSocket serverSocket;
            Socket server;

            public GreetingServer(int port) throws IOException, SQLException, ClassNotFoundException, Exception {
                serverSocket = new ServerSocket(port);
                serverSocket.setSoTimeout(180000);
            }

            public void run() {
                while (true) {
                    try {
                        server = serverSocket.accept();
                        BufferedImage img = ImageIO.read(ImageIO.createImageInputStream(server.getInputStream()));
                        JFrame frame = new JFrame();
                        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
                        frame.pack();
                        frame.setVisible(true);
                    } catch (SocketTimeoutException st) {
                        System.out.println("Socket timed out!");
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        }*/
}
