package li.ijse.chat_application.service;

import javafx.scene.layout.VBox;
import li.ijse.chat_application.controller.ChatFormController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class client {

    private Socket remoteSocket = null;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;


    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public client(Socket remoteSocket,String clientUserName){
        this.remoteSocket=remoteSocket;
        this.clientUserName=clientUserName;
        try {
            this.bufferedReader=new BufferedReader(new InputStreamReader(remoteSocket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(remoteSocket.getOutputStream()));
            this.dataOutputStream=new DataOutputStream(remoteSocket.getOutputStream());
            this.dataInputStream=new DataInputStream(remoteSocket.getInputStream());

        } catch (IOException e) {
            System.out.println("Can not create client");
            closeServer();
            throw new RuntimeException(e);
        }
        sendUserName(clientUserName);
    }

    public void closeServer(){
        try {
            if (remoteSocket != null){
                remoteSocket.close();
            }
            if (bufferedReader!=null){
                bufferedReader.close();
            }
            if (bufferedWriter!=null){
                bufferedWriter.close();
            }
        }catch (IOException e){
            System.out.println("closed client");
            throw new RuntimeException(e);
        }
    }

    public void sendUserName(String clientUserName){
        try {
            bufferedWriter.write(clientUserName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Can't sent Client Username");
            closeServer();
            throw new RuntimeException(e);
        }
    }

    public void sentMessage(String messageToSent) {
        try {
            bufferedWriter.write(messageToSent);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Error!!! Can't sent message to group");
            closeServer();
            throw new RuntimeException(e);
        }
    }

    public void receiveMessage(VBox vbox_messages) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (remoteSocket.isConnected()){
                    try {
                        String  receiveMessage= bufferedReader.readLine();
                        ChatFormController.receiveMessage(receiveMessage,vbox_messages);
                    } catch (IOException e) {
                        System.out.println("Error!!! Can't read received message from client");
                        closeServer();
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }

    public void sentImage(File file,String clientUserName) {
        try {

            BufferedImage bufferedImage = ImageIO.read(file);


            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);

            byte[] size= ByteBuffer.allocate(256).putInt(byteArrayOutputStream.size()).array();


            sentMessage("iMg*->");
            sentMessage(clientUserName);
            dataOutputStream.write(size);
            dataOutputStream.write(byteArrayOutputStream.toByteArray());
            dataOutputStream.flush();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
