import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private int p, q, n, phi, e, d;

    public int gcd(int x, int y) {
        while (y != 0) { int temp = y; y = x % y; x = temp; } return x;
    }

    public int selectPrime() {
        int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97};
        return primes[new Random().nextInt(primes.length)];
    }

    public void generateKeys() {
        p = selectPrime(); q = selectPrime(); n = p * q; phi = (p - 1) * (q - 1);
        e = 2; while (e < phi && gcd(e, phi) != 1) e++;
        d = (1 + 2 * phi) / e;
    }

    // Improved message-to-number conversion
    public int msgToNumber(String msg) {
        msg = msg.toUpperCase().replaceAll("[^A-Z]", "");  // Remove non-alphabetical characters
        if (msg.isEmpty()) {
            System.out.println("Invalid input. Please enter a message with valid characters.");
            return -1;  // Return an invalid value
        }

        StringBuilder number = new StringBuilder();
        for (char c : msg.toCharArray()) {
            number.append(c - 'A' + 1);  // Convert each character to its corresponding number (A=1, B=2, ...)
        }

        try {
            return Integer.parseInt(number.toString());  // Convert the concatenated numbers string to an integer
        } catch (NumberFormatException e) {
            System.out.println("Error in converting message to number.");
            return -1;
        }
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            Server rsaServer = new Server();
            rsaServer.generateKeys();
            System.out.println("Server started... Public key: (" + rsaServer.e + ", " + rsaServer.n + "), Private key: " + rsaServer.d);

            Socket clientSocket = serverSocket.accept();
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            output.writeInt(rsaServer.e); output.writeInt(rsaServer.n);

            int encryptedMessage = input.readInt();
            System.out.println("Encrypted message: " + encryptedMessage);
            int decryptedMessage = (int) Math.pow(encryptedMessage, rsaServer.d) % rsaServer.n;
            System.out.println("Decrypted message: " + decryptedMessage);

            System.out.print("Enter response: ");
            Scanner scanner = new Scanner(System.in);
            String responseMessage = scanner.nextLine();

            int responseMessageNum = rsaServer.msgToNumber(responseMessage);
            if (responseMessageNum != -1) {
                output.writeInt(responseMessageNum);
            }

            clientSocket.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
