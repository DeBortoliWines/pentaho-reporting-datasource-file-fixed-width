package org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth;

import java.io.Serializable;
import java.util.ArrayList;

public class FileFixedWidthConfiguration implements Cloneable, Serializable{
	private static final long serialVersionUID = -3246299894560148827L;
	private String fileLocation;
	private ArrayList<Record> records = new ArrayList<Record>();
	
	@Override
	public FileFixedWidthConfiguration clone() {
		FileFixedWidthConfiguration newObject = new FileFixedWidthConfiguration();
    
		newObject.setFileLocation(this.getFileLocation());
		for(Record r : this.getRecords()){
		  Record newRecord = newObject.new Record();
		  newRecord.setDescription(r.getDescription());
		  newRecord.setIdentifier(r.getIdentifier());
		  newRecord.setRecordIndex(r.getRecordIndex());
		  
		  for (Field f: r.getFields()){
		    Field newField = newObject.new Field();
		    newField.setFieldName(f.getFieldName());
		    newField.setFieldType(f.getFieldType());
		    newField.setFormat(f.getFormat());
		    newField.setStart(f.getStart());
		    newField.setEnd(f.getEnd());
		    
		    newRecord.getFields().add(newField);
		  }
		  
		  newObject.getRecords().add(newRecord);
		}
		return newObject;
	}

	public String getFileLocation() {
		return this.fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	
	public ArrayList<Record> getRecords() {
    return this.records;
  }

  public class Record implements Cloneable, Serializable{
    private static final long serialVersionUID = -1627296106303828712L;
    
    private String description;
    private String identifier;
    private ArrayList<Field> fields = new ArrayList<Field>();
    
    // This is calculated by the report generator, no need to store it to file.
    private int recordIndex;
    
    public String getDescription() {
      return this.description;
    }
    public void setDescription(String description) {
      this.description = description;
    }
    public String getIdentifier() {
      return this.identifier;
    }
    public void setIdentifier(String identifier) {
      this.identifier = identifier;
    }
    public ArrayList<Field> getFields() {
      return this.fields;
    }
    public int getRecordIndex() {
      return recordIndex;
    }
    public void setRecordIndex(int recordIndex) {
      this.recordIndex = recordIndex;
    }
	}
  
  public class Field implements Cloneable, Serializable{
    private static final long serialVersionUID = 6987503753238362391L;
    
    private String fieldName;
    private String fieldType;
    private String format;
    private int start;
    private int end;
    
    // This is calculated by the report generator, no need to store it to file.
    private int columnIndex;
    
    public String getFieldName() {
      return this.fieldName;
    }
    public void setFieldName(String fieldName) {
      this.fieldName = fieldName;
    }
    public String getFieldType() {
      return this.fieldType;
    }
    public void setFieldType(String fieldType) {
      this.fieldType = fieldType;
    }
    public int getStart() {
      return this.start;
    }
    public void setStart(int start) {
      this.start = start;
    }
    public int getEnd() {
      return this.end;
    }
    public void setEnd(int end) {
      this.end = end;
    }
    public String getFormat() {
      return format;
    }
    public void setFormat(String format) {
      this.format = format;
    }
    public int getColumnIndex() {
      return columnIndex;
    }
    public void setColumnIndex(int columnIndex) {
      this.columnIndex = columnIndex;
    }
  }
}
