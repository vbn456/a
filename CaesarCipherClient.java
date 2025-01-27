import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CaesarCipherClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 1234;

        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server.");

            // Get mode, text, and shift from user
            System.out.print("Enter mode (encrypt/decrypt): ");
            String mode = scanner.nextLine();

            System.out.print("Enter text: ");
            String text = scanner.nextLine();

            System.out.print("Enter shift value: ");
            int shift = scanner.nextInt();

            // Send data to server
            out.println(mode);
            out.println(text);
            out.println(shift);

            // Receive response from server
            String result = in.readLine();
            System.out.println("Result from server: " + result);

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}