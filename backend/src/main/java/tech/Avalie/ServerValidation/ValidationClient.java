package tech.Avalie.ServerValidation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ValidationClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;

    public static boolean validate(String data) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(data);

            String response = in.readLine();
            return "valid".equalsIgnoreCase(response);
        } catch (Exception e) {
            System.err.println("Erro ao conectar com o servidor de validação: " + e.getMessage());
            return false;
        }
    }
}