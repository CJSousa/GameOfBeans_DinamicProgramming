
/**
 * @authors Henrique Campos Ferreira - 55065 Clara Sousa - 58403
 */

/*
 * This class presents the Game of Beans II, where the best score Jaba can
 * achieve is found
 */

public class GameOfBeansII {

    /**
     * The sequence of piles.
     */
    private int[] seq;

    /**
     * The next pile to be added.
     */
    private int nextPile;

    /**
     * The maximum number of piles that can be removed in a single play.
     */
    private int depth;

    /**
     * The matrix to save the computed score of Jaba, when Pieton is the first.
     * player.
     */
    private long[][] pieton;

    /**
     * The matrix to save the computed score of Jaba, when Jaba is the first player.
     */
    private long[][] jaba;

    /**
     * Creates a new Game of Beans for the specified number of piles and depth.
     * 
     * @param nPiles - The number of piles.
     * @param depth  - The maximum piles a player can take in a single play.
     */
    public GameOfBeansII(int nPiles, int depth) {
        this.depth = depth;
        seq = new int[nPiles];
        nextPile = 0;
        pieton = new long[nPiles][nPiles];
        jaba = new long[nPiles][nPiles];
    }

    /**
     * Adds a pile of beans to the sequence.
     * 
     * @param beans - The number of beans.
     */
    public void addPile(int beans) {
        seq[nextPile++] = beans;
    }

    /**
     * Calculates the final score of Jaba.
     * 
     * @param firstPlayer - The first player: true for Pieton, false for Jaba.
     * @return The final score of Jaba.
     */
    public long calcJabaFinalScore(boolean firstPlayer) {

        /*
         * Base Case: Filling up the diagonals of the matrix of when jaba is the first
         * player. Notice there is no need to fill in the entries where "0" should be,
         * since Java does so automatically
         */
        for (int i = 0; i < seq.length; i++) {
            jaba[i][i] = seq[i];
        }

        for (int j = 1; j < seq.length; j++) {

            for (int i = j - 1; i >= 0; i--) {

                int updated_depth = Math.min(depth, j - i + 1);

                // Filling the matrix for Pieton
                long[] leftMaxPieton = this.getMaxLeftPieton(i, updated_depth);
                long[] rightMaxPieton = this.getMaxRightPieton(j, updated_depth);

                if (leftMaxPieton[0] >= rightMaxPieton[0]) {

                    if (i + (int) leftMaxPieton[1] > j)
                        pieton[i][j] = 0;
                    else
                        pieton[i][j] = jaba[i + (int) leftMaxPieton[1]][j];
                }

                else {
                    if (i > j - (int) rightMaxPieton[1])
                        pieton[i][j] = 0;
                    else
                        pieton[i][j] = jaba[i][j - (int) rightMaxPieton[1]];
                }

                // Filling the matrix for Jaba
                jaba[i][j] = Math.max(this.maxLeftJaba(i, j, updated_depth), this.maxRightJaba(i, j, updated_depth));

            }

        }

        if (firstPlayer)
            return pieton[0][seq.length - 1];
        else
            return jaba[0][seq.length - 1];
    }

    /* Auxiliary Methods */

    /**
     * Returning the maximum number of beans Pieton can get by playing on the left,
     * along with the minimum number of piles needed to achieve such score
     * 
     * @param i - index to begin the search
     * @param d - depth
     * @return array of maximum score and minimum piles removed
     */
    private long[] getMaxLeftPieton(int i, int d) {
        long max = Long.MIN_VALUE;
        long sum = 0;
        long nPilesRemoved = 0;
        long[] result = new long[2];

        for (int k = i; k <= i + d - 1; k++) {
            sum += seq[k];
            if (sum > max) {
                max = sum;
                nPilesRemoved = k - i + 1;
            }

        }

        result[0] = max;
        result[1] = nPilesRemoved;

        return result;
    }

    /**
     * Returning the maximum number of beans Pieton can get by playing on the right,
     * along with the minimum number of piles needed to achieve such score
     * 
     * @param j - index to begin the search
     * @param d - depth
     * @return array of maximum score and minimum piles removed
     */
    private long[] getMaxRightPieton(int j, int d) {

        long max = Long.MIN_VALUE;
        long sum = 0;
        long nPilesRemoved = 0;
        long[] result = new long[2];

        for (int k = j; k >= j - d + 1; k--) {
            sum += seq[k];
            if (sum > max) {
                max = sum;
                nPilesRemoved = j - k + 1;
            }

        }

        result[0] = max;
        result[1] = nPilesRemoved;

        return result;
    }

    /**
     * Returning the maximum number of beans Jaba can get by playing on the left
     * 
     * @param i - index where subsequence begins
     * @param j - index where subsequence ends
     * @param d - depth
     * @return maximum score
     */
    private long maxLeftJaba(int i, int j, int d) {

        long sum = 0;
        long max = Long.MIN_VALUE;

        for (int k = i; k <= i + d - 1; k++) {

            sum += seq[k];

            long current = sum;

            // Accessing possible positions
            if (k + 1 <= j)
                current += pieton[k + 1][j];

            if (current > max)
                max = current;
        }

        return max;
    }

    /**
     * Returning the maximum number of beans Jaba can get by playing on the right
     * 
     * @param i - index where subsequence begins
     * @param j - index where subsequence ends
     * @param d - depth
     * @return maximum score
     */
    private long maxRightJaba(int i, int j, int d) {

        long sum = 0;

        long max = Long.MIN_VALUE;

        for (int k = j; k >= j - d + 1; k--) {

            sum += seq[k];
            long current = sum;

            // Accessing possible positions
            if (i <= k - 1)
                current += pieton[i][k - 1];

            if (current > max)
                max = current;

        }

        return max;
    }
}
