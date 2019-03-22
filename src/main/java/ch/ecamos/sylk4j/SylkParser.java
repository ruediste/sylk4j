package ch.ecamos.sylk4j;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import ch.ecamos.sylk4j.record.SylkReader;
import ch.ecamos.sylk4j.record.SylkRecord;
import ch.ecamos.sylk4j.record.SylkRecordCellContent;
import ch.ecamos.sylk4j.record.SylkRecordComment;
import ch.ecamos.sylk4j.record.SylkRecordId;
import ch.ecamos.sylk4j.record.SylkRecordUnknown;
import ch.ecamos.sylk4j.record.SylkToken;
import ch.ecamos.sylk4j.sheet.SylkSheet;

public class SylkParser {

	public SylkSheet parseSheet(Reader reader) {
		return new SylkSheet(parseRecords(reader));
	}

	public List<SylkRecord> parseRecords(Reader reader) {
		SylkReader sylkReader = new SylkReader(reader);

		ArrayList<SylkRecord> result = new ArrayList<>();
		loop: while (true) {
			if (sylkReader.peek() == SylkToken.END_DOCUMENT)
				break;
			if (sylkReader.peek() == SylkToken.END_LINE)
				continue;

			int lineNr = sylkReader.lineNr();

			String recordType;
			if (sylkReader.peek() == SylkToken.END_FIELD)
				recordType = "";
			else
				recordType = sylkReader.nextStringUnquoted();
			sylkReader.removeEndField();

			SylkRecord record;
			switch (recordType) {
			case "ID":
				record = new SylkRecordId(sylkReader);
				break;
			case "C":
				record = new SylkRecordCellContent(sylkReader);
				break;
			case "":
				record = new SylkRecordComment(sylkReader);
				break;
			default:
				record = new SylkRecordUnknown(sylkReader);
			}
			record.lineNr = lineNr;
			result.add(record);
			switch (sylkReader.peek()) {
			case END_DOCUMENT:
				break loop;
			case END_LINE:
				sylkReader.nextEndLine();
				break;
			default:
				sylkReader.throwError("Expected end document or end line, but found " + sylkReader.peek() + " at index "
						+ sylkReader.index());
			}

		}
		return result;
	}
}
