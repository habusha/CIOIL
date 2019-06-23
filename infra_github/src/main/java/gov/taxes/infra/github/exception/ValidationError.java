package gov.taxes.infra.github.exception;

import java.util.ArrayList;
import java.util.List;

import gov.taxes.infra.github.enums.SeverityEnum;

/**
* @author Dan Erez
*
*         This class holds validation error info
*/
public class ValidationError {
	private List<String> fieldMessages;
	private List<String> fields;
	private SeverityEnum severity;

	public ValidationError() {
		fieldMessages = new ArrayList<>();
		fields = new ArrayList<>();
	}

	public ValidationError(String field, String fieldMessage, SeverityEnum severity) {
		this();
		this.fields.add(field);
		this.fieldMessages.add(fieldMessage);
		this.severity = severity;
	}

	public ValidationError(String field, List<String> fieldMessages, SeverityEnum severity) {
		this();
		this.fields.add(field);
		this.fieldMessages = fieldMessages;
		this.severity = severity;
	}

	public ValidationError(List<String> fields, List<String> fieldMessages, SeverityEnum severity) {
		this();
		this.fields = fields;
		this.fieldMessages = fieldMessages;
		this.severity = severity;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public List<String> getFieldMessage() {
		return fieldMessages;
	}

	public void setFieldMessages(List<String> fieldMessages) {
		this.fieldMessages = fieldMessages;
	}

	public SeverityEnum getSeverity() {
		return severity;
	}

	public void setSeverity(SeverityEnum severity) {
		this.severity = severity;
	}

	@Override
	public String toString() {
		return "ValidationError [fieldMessages=" + fieldMessages + ", fields=" + fields + ", severity=" + severity
				+ "]";
	}

}
