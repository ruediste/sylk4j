package ch.ecamos.sylk4j.sheet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class SylkTableRow {
	public Map<String, Integer> mapping;
	public SylkSheet sheet;
	public int row;

	public String getString(String column) {
		return getCell(column).getString();
	}

	public LocalDate getDate(String column) {
		return getCell(column).getDate();
	}

	public BigDecimal getNumber(String column) {
		return getCell(column).getNumber();
	}

	private SylkCell getCell(String column) {
		return sheet.getCell(row, mapping.get(column));
	}
}
