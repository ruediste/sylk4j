package ch.ecamos.sylk4j.sheet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import ch.ecamos.sylk4j.record.SylkRecord;
import ch.ecamos.sylk4j.record.SylkRecordCellContent;
import ch.ecamos.sylk4j.record.SylkRecordComment;
import ch.ecamos.sylk4j.record.SylkRecordFormat;
import ch.ecamos.sylk4j.record.SylkRecordId;
import ch.ecamos.sylk4j.record.SylkRecordUnknown;
import ch.ecamos.sylk4j.record.SylkRecordVisitor;

/**
 * Spreadsheet representation, consisting of a rectangular grid of cells
 *
 */
public class SylkSheet {

	public TreeMap<Integer, TreeMap<Integer, SylkCell>> cells = new TreeMap<>();

	public SylkSheet() {
	}

	public SylkCell getCell(int row, int column) {
		TreeMap<Integer, SylkCell> rowTree = cells.get(row);
		if (rowTree == null)
			return null;
		return rowTree.get(column);
	}

	public List<SylkTableRow> getTableRows(int startRow) {
		Map<String, Integer> mapping = new HashMap<>();
		{
			TreeMap<Integer, SylkCell> row = cells.get(startRow);
			row.forEach((idx, cell) -> {
				if (cell.getString() != null)
					mapping.put(cell.getString(), idx);
			});
		}

		List<SylkTableRow> result = new ArrayList<SylkTableRow>();
		for (Entry<Integer, TreeMap<Integer, SylkCell>> entry : cells.tailMap(startRow, false).entrySet()) {
			SylkTableRow row = new SylkTableRow();
			row.mapping = mapping;
			row.row = entry.getKey();
			row.sheet = this;
			result.add(row);
		}
		return result;
	}

	public SylkCell getOrCreateCell(int row, int column) {
		TreeMap<Integer, SylkCell> rowTree = cells.computeIfAbsent(row, x -> new TreeMap<>());
		return rowTree.computeIfAbsent(column, x -> new SylkCell());
	}

	public SylkSheet(List<SylkRecord> records) {
		new Object() {
			Integer lastY = null;
			Integer lastX = null;
			{
				for (SylkRecord record : records) {
					record.accept(new SylkRecordVisitor<Object>() {

						@Override
						public Object visit(SylkRecordId record) {
							return null;
						}

						@Override
						public Object visit(SylkRecordUnknown record) {
							return null;
						}

						@Override
						public Object visit(SylkRecordCellContent record) {
							if (record.y != null)
								lastY = record.y;
							if (record.x != null)
								lastX = record.x;

							boolean valueUnquotedPresent = record.valueUnquoted != null
									&& !"".equals(record.valueUnquoted);
							if (record.valueQuoted != null || valueUnquotedPresent) {
								if (lastY == null)
									throw new RuntimeException(
											record.lineNr + ": No last row available and no row specified");
								SylkCell cell = getOrCreateCell(lastY, lastX);
								if (record.valueQuoted != null)
									cell.valueStr = record.valueQuoted;
								else if (valueUnquotedPresent)
									cell.valueNumber = new BigDecimal(record.valueUnquoted);
							}
							return null;
						}

						@Override
						public Object visit(SylkRecordComment record) {
							return null;
						}

						@Override
						public Object visit(SylkRecordFormat record) {
							if (record.y != null)
								lastY = record.y;
							if (record.x != null)
								lastX = record.x;
							return null;
						}
					});
				}
			}
		};

	}
}
