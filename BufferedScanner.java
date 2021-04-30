import java.io.*;
import java.util.*;

/**
 * @authors Henrique Campos Ferreira - 55065 Clara Sousa - 58403 Mix in
 */

public class BufferedScanner implements Closeable {

	private static final String DELIMITER = " ";

	private BufferedReader reader;

	// Current line being read
	private String line;
	// Current position in the line being read
	private int currentLinePos;
	// Array of tokens
	private String[] tokens;
	// Current position in the tokens array
	private int currentTokensPos;

	// True if a new line is to be read
	private boolean nextLine;
	// True if a line has already been parsed
	private boolean tokenized;
	// True if there are no more lines to be read
	private boolean finished;
	// True if the scan is closed
	private boolean closed;

	/**
	 * Creates a new BufferedScanner given the reader
	 * 
	 * @param reader
	 */
	public BufferedScanner(Reader reader) throws IOException {
		this.reader = new BufferedReader(reader);
		finished = false;
		closed = false;
		getLine();
	}

	/**
	 * Creates a new BufferedScanner given the reader
	 * 
	 * @param reader     - reader
	 * @param bufferSize - size of the buffer
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public BufferedScanner(Reader reader, int bufferSize) throws IOException, IllegalArgumentException {
		this.reader = new BufferedReader(reader, bufferSize);
		finished = false;
		closed = false;
		getLine();
	}

	/**
	 * Scans the next token of the input as an integer
	 * 
	 * @return the integer scanned from the input
	 * @throws IOException
	 * @throws NoSuchElementException - if input is exhausted
	 * @throws InputMismatchException - if the next token does not match the Integer
	 *                                regular
	 * @throws IllegalStateException  - if this scanner is closed
	 */
	public int nextInt() throws IOException, NoSuchElementException, InputMismatchException, IllegalStateException {
		if (closed)
			throw new IllegalStateException();

		if (finished || (nextLine && !getLine()))
			throw new NoSuchElementException();

		if (hasFinishedLine())
			throw new InputMismatchException();

		if (!tokenized)
			parseLine();

		try {
			int result = Integer.parseInt(tokens[currentTokensPos]);
			currentLinePos += tokens[currentTokensPos++].length() + 1; // add 1 to count the delimiter.
			return result;
		} catch (Exception ex) {
			throw new InputMismatchException();
		}
	}

	/**
	 * Advances this scanner past the current line and returns the input that was
	 * skipped. This method returns the rest of the current line, excluding any line
	 * separator at the end. The position is set to the beginning of the next line.
	 * Since this method continues to search through the input looking for a line
	 * separator, it may buffer all of the input searching for the line to skip if
	 * no line separators are present.
	 * 
	 * @return the line that was skipped
	 * @throws IOException
	 * @throws NoSuchElementException - if input is exhausted
	 * @throws IllegalStateException  - if this scanner is closed
	 */
	public String nextLine() throws IOException, NoSuchElementException, IllegalStateException {
		if (closed)
			throw new IllegalStateException();

		if (finished)
			return null;

		if (nextLine) {
			getLine();
			nextLine = true;
			return line;
		}

		nextLine = true;
		// Return the rest of the line
		if (!hasFinishedLine())
			return line.substring(currentLinePos);
		else
			return "";
	}

	/**
	 * Scans the next token of the input as a String
	 * 
	 * @return the String scanned from the input
	 * @throws IOException
	 * @throws NoSuchElementException - if input is exhausted
	 * @throws IllegalStateException  - if this scanner is closed
	 */
	public String next() throws IOException, NoSuchElementException, IllegalStateException {

		if (closed)
			throw new IllegalStateException();

		if (finished || (nextLine && !getLine()))
			throw new NoSuchElementException();

		if (hasFinishedLine())
			throw new NoSuchElementException();

		if (!tokenized)
			parseLine();

		String result = tokens[currentTokensPos];
		currentLinePos += tokens[currentTokensPos++].length() + 1; // add 1 to count the delimiter.

		return result;

	}

	/**
	 * Checking if the current line has ended
	 * 
	 * @return true if the current line has ended, false if otherwise
	 */
	private boolean hasFinishedLine() {
		return currentLinePos >= line.length();
	}

	/**
	 * Getting the tokens from a line (that is, strings separated by spaces)
	 */
	private void parseLine() {
		tokens = line.split(DELIMITER);
		currentTokensPos = 0;
		tokenized = true;
	}

	/**
	 * Reading a new line
	 * 
	 * @return true if there is new line to be read, false if otherwise
	 * @throws IOException
	 */
	private boolean getLine() throws IOException {
		line = reader.readLine();
		nextLine = false;
		tokens = null;
		tokenized = false;

		if (line == null)
			finished = true;
		else
			currentLinePos = 0;

		return !finished;
	}

	@Override
	public void close() throws IOException {
		reader.close();
		closed = true;
		line = null;
		tokens = null;
	}

}
