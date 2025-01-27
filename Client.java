import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public int msgToNumber(String message) {
        StringBuilder number = new StringBuilder();
        for (char c : message.toUpperCase().toCharArray()) {
            number.append(c - 'A' + 1); // Converts A=1, B=2, ..., Z=26
        }
        return Integer.parseInt(number.toString());
    }

    public int encrypt(int plaintext, int e, int n) {
        return (int) (Math.pow(plaintext, e) % n);
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);

            Client rsaClient = new Client();

            // Receive public key from server
            int e = input.readInt();
            int n = input.readInt();
            System.out.println("Received public key (e, n): (" + e + ", " + n + ")");

            // Ask the user for a message to encrypt
            System.out.print("Enter a message to send to the server: ");
            String message = scanner.nextLine();

            // Convert message to numerical format
            int plaintext = rsaClient.msgToNumber(message);
            System.out.println("Plaintext (numerical): " + plaintext);

            // Encrypt the message
            int encryptedMessage = rsaClient.encrypt(plaintext, e, n);
            System.out.println("Encrypted message: " + encryptedMessage);

            // Send encrypted message to server
            output.writeInt(encryptedMessage);

            // Receive the server's response message (numerical format)
            int responseMessageNum = input.readInt();
            System.out.println("Server's response message (numerical): " + responseMessageNum);

            // Optionally, decrypt or interpret the server's response message here
            System.out.println("Interpret the numerical response back as required.");

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
