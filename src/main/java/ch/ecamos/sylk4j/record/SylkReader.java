package ch.ecamos.sylk4j.record;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class SylkReader {

	private BufferedReader reader;
	private SylkToken peeked;
	private String line;
	private int index;
	private int lineNr = 1;

	public SylkReader(Reader reader) {
		this.reader = new BufferedReader(reader);
	}

	public SylkToken peek() {
		if (peeked == null)
			doPeek();
		return peeked;
	}

	private void doPeek() {
		if (line == null) {
			try {
				line = reader.readLine();
				if (line == null) {
					peeked = SylkToken.END_DOCUMENT;
					return;
				}
				index = 0;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		if (index >= line.length()) {
			peeked = SylkToken.END_LINE;
			return;
		}

		int cp = line.codePointAt(index);
		switch (cp) {
		case ';':
			peeked = SylkToken.END_FIELD;
			break;
		case '"':
			peeked = SylkToken.STRING_QUOTED;
			break;
		default:
			peeked = SylkToken.STRING_UNQUOTED;
			break;
		}
	}

	private int currentCodePoint() {
		return line.codePointAt(index);
	}

	private void consumeCodePoint() {
		index = line.offsetByCodePoints(index, 1);
	}

	public void checkPeek(SylkToken expectedToken) {
		if (peek() != expectedToken)
			throwError("Expected " + expectedToken + " , but was " + peek());

	}

	public String nextStringUnquoted() {
		checkPeek(SylkToken.STRING_UNQUOTED);
		peeked = null;
		int startIndex = index;
		while (index < line.length()) {
			int cp = currentCodePoint();
			if (cp == ';' || cp == '"')
				break;
			consumeCodePoint();
		}
		return line.substring(startIndex, index);
	}

	public String nextStringQuoted() {
		checkPeek(SylkToken.STRING_QUOTED);
		peeked = null;
		index++;
		int startIndex = index;
		while (true) {
			int cp = currentCodePoint();
			if (cp == '"')
				return line.substring(startIndex, index++);
			consumeCodePoint();
		}
	}

	public void nextEndLine() {
		checkPeek(SylkToken.END_LINE);
		peeked = null;
		line = null;
		lineNr++;
	}

	public void removeEndField() {
		if (peek() == SylkToken.END_FIELD)
			nextEndField();
	}

	public void nextEndField() {
		checkPeek(SylkToken.END_FIELD);
		peeked = null;
		index++;
	}

	public String nextRestOfLine() {
		peeked = null;
		String result = line.substring(index);
		index = line.length();
		return result;
	}

	public int index() {
		return index;
	}

	public int lineNr() {
		return lineNr;
	}

	public void throwError(String message) {
		throw new RuntimeException("Line " + lineNr + "/" + (index + 1) + ": " + message);
	}
}
