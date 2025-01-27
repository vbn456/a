import java.io.*;
import java.net.*;
import java.util.Scanner;

public class RailFenceCipherClient {

    public static void main(String[] args) {

        String hostname = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(hostname, port)) {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            // Take input from the user
            System.out.print("Enter mode (encrypt/decrypt): ");
            String mode = scanner.nextLine();
            System.out.print("Enter text: ");
            String text = scanner.nextLine();
            System.out.print("Enter key (integer): ");
            int key = scanner.nextInt();

            // Send data to server
            out.println(mode);
            out.println(text);
            out.println(key);

            // Receive response from server
            String response = in.readLine();
            System.out.println("Server response: " + response);
			
            scanner.close();

        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());
        } 
		catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}

