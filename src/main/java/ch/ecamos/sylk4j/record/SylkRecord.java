package ch.ecamos.sylk4j.record;

public abstract class SylkRecord {

	public int lineNr;

	public abstract <T> T accept(SylkRecordVisitor<T> visitor);
}
