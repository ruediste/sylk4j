package ch.ecamos.sylk4j.record;

public class SylkRecordUnknown extends SylkRecord {

	public SylkRecordUnknown() {
	}

	public SylkRecordUnknown(SylkReader sylkReader) {
		sylkReader.nextRestOfLine();
	}

	@Override
	public <T> T accept(SylkRecordVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
