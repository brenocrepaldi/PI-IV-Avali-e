package tech.Avalie.ServerValidation;


import java.io.*;
import java.net.*;

public class ValidationServer {
    private ServerSocket serverSocket;

    public ValidationServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() {
        System.out.println("Servidor iniciado...");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new ValidationHandler(clientSocket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void stop() throws IOException {
        serverSocket.close();
    }
}
