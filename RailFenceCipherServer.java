import java.io.*;
import java.net.*;

public class RailFenceCipherServer {

	public static String encrypt(String text, int key) {

        char[][] rail = new char[key][text.length()];
        boolean down = false;
        int row = 0, col = 0;

        // Build the rail matrix
        for (int i = 0; i < text.length(); i++) {
            if (row == 0 || row == key - 1) down = !down;
            rail[row][col++] = text.charAt(i);
            row += down ? 1 : -1;
        }

        // Collect the characters row-wise
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < key; i++) {
		
            for (int j = 0; j < text.length(); j++) {
                if (rail[i][j] != '\0') result.append(rail[i][j]);
            }
        }
        return result.toString();
    }

	
	public static String decrypt(String cipher, int key) {

        char[][] rail = new char[key][cipher.length()];
        boolean down = false;
        int row = 0, col = 0;

        // Mark the positions to be filled
        for (int i = 0; i < cipher.length(); i++) {
		
            if (row == 0 || row == key - 1) down = !down;
            rail[row][col++] = '*';
            row += down ? 1 : -1;
        }
		
        // Fill the rail with the cipher text
        int index = 0;

        for (int i = 0; i < key; i++) {
		
            for (int j = 0; j < cipher.length(); j++) {

                if (rail[i][j] == '*' && index < cipher.length()) {

                    rail[i][j] = cipher.charAt(index++);
                }
            }
        }

        // Collect characters by following the zigzag pattern
        StringBuilder result = new StringBuilder();

        row = 0;
        col = 0;
        down = false;

        for (int i = 0; i < cipher.length(); i++) {

            if (row == 0 || row == key - 1) down = !down;
            result.append(rail[row][col++]);
            row += down ? 1 : -1;
        }

        return result.toString();
    }



    public static void main(String[] args) {

        int port = 12345;
		
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is listening on port " + port);

            while (true) {

                Socket socket = serverSocket.accept();
                System.out.println("Client connected.");
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				
                // Receive input from the client
                String mode = in.readLine();
                String text = in.readLine();
                int key = Integer.parseInt(in.readLine());

                String result;

                if (mode.equalsIgnoreCase("encrypt")) {

                    result = encrypt(text, key);
                } 
				
				else if (mode.equalsIgnoreCase("decrypt")) {

                    result = decrypt(text, key);
                } 
				
				else {

                    result = "Invalid mode. Please choose 'encrypt' or 'decrypt'.";
                }

                // Send result back to client
                out.println(result);
                socket.close();
            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}