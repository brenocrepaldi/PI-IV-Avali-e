package tech.Avalie.ServerValidation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ValidationHandler extends Thread {
    private Socket clientSocket;

    public ValidationHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String data = in.readLine();

            // Validação de RA
            if (data.matches("^[0-9]{8}$")) {
                out.println("valid");
            }
            // Validação de email
            else if (data.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                out.println("valid");
            }
            // Validação de ObjectId
            else if (data.matches("^[a-fA-F0-9]{24}$")) {
                out.println("valid");
            }
            // Validação de telefone
            else if (data.matches("^\\+?[0-9]{10,15}$")) {
                out.println("valid");
            }
            // Validação de active (true ou false)
            else if (data.equalsIgnoreCase("true") || data.equalsIgnoreCase("false")) {
                out.println("valid");
            }
            // Caso nenhum formato seja válido
            else {
                out.println("invalid");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
