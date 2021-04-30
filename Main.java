import java.io.*;

/**
 * @authors Henrique Campos Ferreira - 55065 Clara Sousa - 58403
 */

public class Main {

    // Default value for buffer size
    private static final int BUFFER_SIZE = 10000;

    public static void main(String[] args) throws IllegalArgumentException, IOException {

        // Creating the scanner
        BufferedScanner in = new BufferedScanner(new InputStreamReader(System.in), BUFFER_SIZE);

        int numTests = in.nextInt();
        in.nextLine();

        // Going through all the tests
        for (int i = 0; i < numTests; i++) {

            int numPiles = in.nextInt();
            int depth = in.nextInt();
            in.nextLine();

            GameOfBeansII g = new GameOfBeansII(numPiles, depth);

            for (int j = 0; j < numPiles; j++)
                g.addPile(in.nextInt());

            in.nextLine();

            String player = in.nextLine();
            boolean firstPlayer = true;

            if (player.equals("Jaba"))
                firstPlayer = false;

            long result = g.calcJabaFinalScore(firstPlayer);

            System.out.println(result);
        }

        in.close();
    }
}
