package ch.ecamos.sylk4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import ch.ecamos.sylk4j.record.SylkRecord;
import ch.ecamos.sylk4j.record.SylkRecordCellContent;
import ch.ecamos.sylk4j.record.SylkRecordId;
import ch.ecamos.sylk4j.sheet.SylkSheet;
import ch.ecamos.sylk4j.sheet.SylkTableRow;

public class SylkParserTest {

	@Test
	public void simple() throws IOException {
		List<SylkRecord> parsed = parse("simple.slk");
		assertEquals(8, parsed.size());
		assertTrue(parsed.get(0) instanceof SylkRecordId);
		{
			SylkRecordCellContent cell = (SylkRecordCellContent) parsed.get(1);
			assertEquals("Row 1", cell.valueQuoted);
			assertEquals(1, (int) cell.x);
			assertEquals((Integer) 1, cell.y);
		}
	}

	private List<SylkRecord> parse(String name) throws IOException {
		SylkParser parser = new SylkParser();
		List<SylkRecord> parsed = parser.parseRecords(
				new InputStreamReader(getClass().getResourceAsStream("/" + name), Charset.forName("UTF-8")));
		return parsed;
	}

	@Test
	public void test1() throws IOException {
		List<SylkRecord> parsed = parse("test1.slk");
		SylkSheet sheet = new SylkSheet(parsed);
		assertEquals("ECAMOS UCITS ICAV", sheet.getCell(2, 4).valueStr);
		assertEquals(12, sheet.getCell(2, 5).valueNumber.doubleValue(), 0.001);
		assertEquals(123.12, sheet.getCell(2, 18).valueNumber.doubleValue(), 0.001);
		List<SylkTableRow> rows = sheet.getTableRows(1);
		assertEquals(1, rows.size());
		SylkTableRow row = rows.get(0);
		assertEquals(123.12, row.getNumber("AMOUNT_EST_EUR").doubleValue(), 0.001);
		assertEquals(LocalDate.of(2019, 3, 26), row.getDate("VALUE_DATE"));
	}

	@Test
	public void test2() throws IOException {
		SylkSheet sheet = new SylkSheet(parse("test2.slk"));
		List<SylkTableRow> rows = sheet.getTableRows(4);
		assertEquals(1, rows.size());
		SylkTableRow row = rows.get(0);
		assertEquals(123, row.getNumber("Register Number").doubleValue(), 0.001);
	}

}
