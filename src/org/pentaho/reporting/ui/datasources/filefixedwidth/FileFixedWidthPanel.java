package org.pentaho.reporting.ui.datasources.filefixedwidth;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JButton;

import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration.Field;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration.Record;

import javax.swing.JScrollPane;

public class FileFixedWidthPanel extends JPanel {
  private static final long serialVersionUID = -5158167375579708829L;
	
	private FileFixedWidthConfiguration config = new FileFixedWidthConfiguration();
	private JTextField textQueryName;
	private JTextField textFileLocation;
	private JTable tblRecords;
	private JTable tblFields;

	/**
	 * Create the panel.
	 */
	public FileFixedWidthPanel() {
	  setLayout(null);
	  
	  JLabel lblQueueName = new JLabel("Queue Name:");
	  lblQueueName.setHorizontalAlignment(SwingConstants.RIGHT);
	  lblQueueName.setBounds(26, 14, 95, 15);
	  add(lblQueueName);
	  
	  textQueryName = new JTextField();
	  lblQueueName.setLabelFor(textQueryName);
	  textQueryName.setBounds(129, 12, 335, 19);
	  add(textQueryName);
	  textQueryName.setColumns(100);
	  
	  JLabel lblLblfilelocation = new JLabel("File Location:");
	  lblLblfilelocation.setHorizontalAlignment(SwingConstants.RIGHT);
	  lblLblfilelocation.setBounds(26, 41, 95, 15);
	  add(lblLblfilelocation);
	  
	  textFileLocation = new JTextField();
	  lblLblfilelocation.setLabelFor(textFileLocation);
	  textFileLocation.setColumns(100);
	  textFileLocation.setBounds(129, 39, 335, 19);
	  add(textFileLocation);
	  
	  JLabel lblRecords = new JLabel("Record Headers:");
	  lblRecords.setBounds(26, 73, 139, 15);
	  add(lblRecords);
	  
	  JLabel lblRecordFields = new JLabel("Record Fields:");
	  lblRecordFields.setBounds(26, 266, 139, 15);
	  add(lblRecordFields);
	  
	  JButton btnRecordDel = new JButton("Del");
	  btnRecordDel.setBounds(421, 68, 39, 25);
	  add(btnRecordDel);
	  
	  JButton btnRecordAdd = new JButton("All");
	  btnRecordAdd.setBounds(370, 68, 39, 25);
	  add(btnRecordAdd);
	  
	  JButton btnAddField = new JButton("All");
	  btnAddField.setBounds(370, 261, 39, 25);
	  add(btnAddField);
	  
	  JButton btnDeleteField = new JButton("Del");
	  btnDeleteField.setBounds(421, 261, 39, 25);
	  add(btnDeleteField);
	  

	  
	  tblRecords = new JTable();
	  tblRecords.setModel(new DefaultTableModel(
	    new Object[][] {
	    },
	    new String[] {
	      "Description", "Identifier"
	    }
	  ));

	   JScrollPane scrollPane = new JScrollPane();
	   scrollPane.setBounds(26, 95, 435, 154);
	   scrollPane.setViewportView(tblRecords);
	   add(scrollPane);
	   
	   JScrollPane scrollPane_1 = new JScrollPane();
	   scrollPane_1.setBounds(26, 289, 435, 154);
	   add(scrollPane_1);
	   
	   tblFields = new JTable();
	   tblFields.setModel(new DefaultTableModel(
	     new Object[][] {
	       {null, null, null, null},
	     },
	     new String[] {
	       "Field Name", "Data Type", "Start", "End"
	     }
	   ) {
	     private static final long serialVersionUID = -4535986836647150193L;
	     @SuppressWarnings("rawtypes")
      Class[] columnTypes = new Class[] {
	       String.class, Object.class, Integer.class, Integer.class
	     };
	     @SuppressWarnings({ "rawtypes", "unchecked" })
      public Class getColumnClass(int columnIndex) {
	       return columnTypes[columnIndex];
	     }
	   });
	   tblFields.getColumnModel().getColumn(0).setPreferredWidth(177);
	   scrollPane_1.setViewportView(tblFields);
	}
	
	public void setQueryName(String queryName){
	  textQueryName.setText(queryName);
	}
	
	public String getQueryName(){
	  return textQueryName.getText();
	}
	
	public FileFixedWidthConfiguration getConfiguration(){
	  this.config.setFileLocation(textFileLocation.getText());
	  return this.config;
	}
	
	public void setConfiguration(FileFixedWidthConfiguration config){
	  if (config != null)
	    this.config = config;
	  
	  textFileLocation.setText(this.config.getFileLocation());
	  DefaultTableModel recordModel = (DefaultTableModel) tblRecords.getModel();
	  recordModel.setRowCount(0);
	  
	  Record[] records = config.getRecords();
	  for (int i = 0; i < records.length; i++){
	    recordModel.addRow(new Object[]{
	        records[i].getDescription(),
	        records[i].getIdentifier()
	    });
	  }

	  /* Fill the field table with the list from the first record */
	  DefaultTableModel fieldModel = (DefaultTableModel) tblFields.getModel();
	  fieldModel.setRowCount(0);
    
	  if (records.length > 0){
      Field[] fields = records[0].getFields();
      for (int i = 0; i < fields.length; i++){
        fieldModel.addRow(new Object[]{
            fields[i].getFieldName(),
            fields[i].getFieldType(),
            fields[i].getStart(),
            fields[i].getEnd()
        });
      }
	  }

	}
}
