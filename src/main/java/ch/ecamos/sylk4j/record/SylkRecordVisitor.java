package ch.ecamos.sylk4j.record;

public interface SylkRecordVisitor<T> {

	T visit(SylkRecordId record);

	T visit(SylkRecordUnknown record);

	T visit(SylkRecordCellContent record);

	T visit(SylkRecordComment record);

	T visit(SylkRecordFormat record);

}
