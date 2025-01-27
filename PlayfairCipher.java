import java.util.Scanner;

public class PlayfairCipher {

    private static char[][] matrix = new char[5][5];

    public static void constructMatrix(String keyword) {
        boolean[] seen = new boolean[26];
        StringBuilder key = new StringBuilder();

        for (char ch : keyword.toUpperCase().toCharArray()) {
            if (ch == 'J') ch = 'I';
            if (!seen[ch - 'A']) {
                key.append(ch);
                seen[ch - 'A'] = true;
            }
        }
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            if (ch == 'J') continue;
            if (!seen[ch - 'A']) key.append(ch);
        }

        for (int i = 0, k = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                matrix[i][j] = key.charAt(k++);
    }

    public static String process(String text, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase().replace('J', 'I').replaceAll("\\s", "");
        if (text.length() % 2 != 0) text += "Z";

        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i), b = text.charAt(i + 1);
            int[] pos1 = findPosition(a), pos2 = findPosition(b);

            if (pos1[0] == pos2[0]) { // Same row
                result.append(matrix[pos1[0]][(pos1[1] + (encrypt ? 1 : 4)) % 5]);
                result.append(matrix[pos2[0]][(pos2[1] + (encrypt ? 1 : 4)) % 5]);
            } else if (pos1[1] == pos2[1]) { // Same column
                result.append(matrix[(pos1[0] + (encrypt ? 1 : 4)) % 5][pos1[1]]);
                result.append(matrix[(pos2[0] + (encrypt ? 1 : 4)) % 5][pos2[1]]);
            } else { // Rectangle rule
                result.append(matrix[pos1[0]][pos2[1]]);
                result.append(matrix[pos2[0]][pos1[1]]);
            }
        }
        return result.toString();
    }

    private static int[] findPosition(char ch) {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                if (matrix[i][j] == ch)
                    return new int[] { i, j };
        return null;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter keyword:");
        constructMatrix(sc.nextLine());

        System.out.println("Matrix:");
        for (char[] row : matrix)
            System.out.println(String.valueOf(row));

        System.out.println("Enter plaintext:");
        String encrypted = process(sc.nextLine(), true);
        System.out.println("Encrypted: " + encrypted);

        System.out.println("Enter ciphertext:");
        String decrypted = process(sc.nextLine(), false);
        System.out.println("Decrypted: " + decrypted);

        sc.close();
    }
}
