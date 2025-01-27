import java.io.*;
import java.net.*;

public class CaesarCipherServer {

    // Encrypt method
    public static String encrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        shift = shift % 26;
        
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                result.append((char) ((ch - base + shift) % 26 + base));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    // Decrypt method
    public static String decrypt(String text, int shift) {
        return encrypt(text, 26 - (shift % 26));
    }

    public static void main(String[] args) {
        int port = 1234;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Waiting for client connection...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected.");

                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    String mode = in.readLine(); // "encrypt" or "decrypt"
                    String text = in.readLine(); // text to process
                    int shift = Integer.parseInt(in.readLine()); // shift value

                    String result;
                    if ("encrypt".equalsIgnoreCase(mode)) {
                        result = encrypt(text, shift);
                    } else if ("decrypt".equalsIgnoreCase(mode)) {
                        result = decrypt(text, shift);
                    } else {
                        result = "Invalid mode. Use 'encrypt' or 'decrypt'.";
                    }

                    out.println(result);
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}