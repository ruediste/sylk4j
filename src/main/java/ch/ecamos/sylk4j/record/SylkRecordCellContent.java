package ch.ecamos.sylk4j.record;

public class SylkRecordCellContent extends SylkRecord {

	public Integer x;
	public String valueQuoted;
	public String valueUnquoted;
	public Integer y;

	public SylkRecordCellContent(SylkReader sylkReader) {
		while (true) {
			switch (sylkReader.peek()) {
			case STRING_UNQUOTED: {
				String field = sylkReader.nextStringUnquoted();
				switch (field.charAt(0)) {
				case 'X':
					x = Integer.parseInt(field.substring(1));
					sylkReader.removeEndField();
					break;
				case 'Y':
					y = Integer.parseInt(field.substring(1));
					sylkReader.removeEndField();
					break;
				case 'K':
					if (field.length() == 1)
						valueQuoted = sylkReader.nextStringQuoted();
					else
						valueUnquoted = field.substring(1);
					sylkReader.removeEndField();
					break;
				default:
					sylkReader.removeRestOfField();
					sylkReader.removeEndField();
				}
				break;
			}
			default:
				return;

			}
		}
	}

	@Override
	public <T> T accept(SylkRecordVisitor<T> visitor) {
		return visitor.visit(this);
	}

}
