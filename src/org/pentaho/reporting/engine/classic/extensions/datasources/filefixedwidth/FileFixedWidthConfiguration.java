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
	}
  
  public class Field implements Cloneable, Serializable{
    private static final long serialVersionUID = 6987503753238362391L;
    
    private String fieldName;
    private String fieldType;
    private int start;
    private int end;
    
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
  }
}
