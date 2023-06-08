package lk.ijse.chat_application.service;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class clientManager implements Runnable{

    public static ArrayList<clientManager> clientManagers=new ArrayList<>();
    private Socket localSocket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

    public clientManager(Socket localSocket) {

        try {
            this.localSocket=localSocket;
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(localSocket.getOutputStream()));
            this.bufferedReader=new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
            System.out.println("**************");
            this.clientUserName=bufferedReader.readLine();
            System.out.println(clientUserName);
            clientManagers.add(this);
            broadCastMessage("* "+clientUserName+" has been connected");
        }catch (IOException e){
            close();
            throw new RuntimeException(e);
        }





    }


    @Override
    public void run() {

        String receivedMessage;

        while (localSocket.isConnected()){
            try {
                /**Blocking operation*/
                receivedMessage=bufferedReader.readLine();
                if(receivedMessage.equalsIgnoreCase("left")){
                    removeClientManager();
                }else {
                    broadCastMessage(receivedMessage);
                }
            }catch (IOException e){
                e.printStackTrace();
                close();
                break;
            }
        }

    }

    private void broadCastMessage(String message) {
        for (clientManager client : clientManagers) {
            try{
                if(!this.clientUserName.equals(client.clientUserName)){
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("Can't broadcast message");
                close();
            }
        }
    }


    public void removeClientManager(){
        clientManagers.remove(this);
        broadCastMessage("* "+clientUserName+" has been left");
    }


    private void close(){
        try {
            if (localSocket!=null){
                localSocket.close();
            }
            if (bufferedWriter!=null){
                bufferedWriter.close();
            }
            if (bufferedReader!=null){
                bufferedReader.close();
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
