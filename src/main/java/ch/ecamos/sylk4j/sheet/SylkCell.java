package ch.ecamos.sylk4j.sheet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class SylkCell {

	public String valueStr;
	public BigDecimal valueNumber;

	public String getString() {
		return valueStr;
	}

	public void setNull() {
		valueNumber = null;
		valueStr = null;
	}

	public void setString(String value) {
		valueStr = value;
		valueNumber = null;
	}

	public BigDecimal getNumber() {
		return valueNumber;
	}

	public void setNumber(BigDecimal value) {
		valueStr = null;
		valueNumber = value;
	}

	public LocalDate getDate() {
		if (getNumber() == null)
			return null;
		return LocalDate.ofEpochDay(((long) getNumber().doubleValue()) - 25569);
	}

	public void setDate(LocalDate date) {
		valueStr = null;
		valueNumber = BigDecimal.valueOf(date.toEpochDay() + 25569);
	}

	public LocalDateTime getDateTime() {
		if (getNumber() == null)
			return null;
		return LocalDateTime.ofEpochSecond((long) (getNumber().doubleValue() - 25569) * 86400, 0, ZoneOffset.UTC);
	}

	public void setDateTime(LocalDateTime dateTime) {
		valueStr = null;
		valueNumber = BigDecimal.valueOf(dateTime.toEpochSecond(ZoneOffset.UTC) / 86400. + 25569);
	}
}
