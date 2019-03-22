package ch.ecamos.sylk4j.record;

public class SylkRecordComment extends SylkRecord {

	public String comment;

	public SylkRecordComment() {
	}

	public SylkRecordComment(SylkReader sylkReader) {
		comment = sylkReader.nextRestOfLine();
	}

	@Override
	public <T> T accept(SylkRecordVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
