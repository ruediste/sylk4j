package ch.ecamos.sylk4j.record;

public class SylkRecordId extends SylkRecord {

	public SylkRecordId(SylkReader sylkReader) {
		sylkReader.nextRestOfLine();
	}

	@Override
	public <T> T accept(SylkRecordVisitor<T> visitor) {
		return visitor.visit(this);
	}

}
