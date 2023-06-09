package com.TCP.socketProgramming.model;

import com.TCP.socketProgramming.controller.ClientManagementController;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

/**
 * @author : Ansh & RIshav
 
 **/
public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error creating client.");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessageSever(String messageToSever){
        try {
            bufferedWriter.write(messageToSever);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending message to the Sever");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void receivedMessageFormSever(VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String massageFromSever = bufferedReader.readLine();
                        ClientManagementController.addLabel(massageFromSever, vBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error receiving message from the Sever");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }

            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
