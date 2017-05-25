package org.pentaho.reporting.ui.datasources.filefixedwidth;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;

import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration.Field;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration.Record;

import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;

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
	  
	  JButton btnRecordDel = new JButton("");
	  btnRecordDel.setIcon(new ImageIcon(FileFixedWidthPanel.class.getResource("/org/pentaho/reporting/ui/datasources/filefixedwidth/resources/Remove.png")));
	  btnRecordDel.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
	      if (tblRecords.getSelectedRow() != -1) {
            // remove selected row from the model
	        ((headerTableModel) tblRecords.getModel()).removeRow(tblRecords.getSelectedRow());
        }
	    }
	  });
	  btnRecordDel.setBounds(421, 68, 39, 25);
	  add(btnRecordDel);
	  
	  JButton btnRecordAdd = new JButton("");
	  btnRecordAdd.setIcon(new ImageIcon(FileFixedWidthPanel.class.getResource("/org/pentaho/reporting/ui/datasources/filefixedwidth/resources/Add.png")));
	  btnRecordAdd.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
	      ((headerTableModel) tblRecords.getModel()).addRow();
	    }
	  });
	  btnRecordAdd.setBounds(370, 68, 39, 25);
	  add(btnRecordAdd);
	  
	  JButton btnAddField = new JButton("");
	  btnAddField.setIcon(new ImageIcon(FileFixedWidthPanel.class.getResource("/org/pentaho/reporting/ui/datasources/filefixedwidth/resources/Add.png")));
	  btnAddField.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
	      ((fieldTableModel) tblFields.getModel()).addRow();
	    }
	  });
	  btnAddField.setBounds(370, 261, 39, 25);
	  add(btnAddField);
	  
	  JButton btnDeleteField = new JButton("");
	  btnDeleteField.setIcon(new ImageIcon(FileFixedWidthPanel.class.getResource("/org/pentaho/reporting/ui/datasources/filefixedwidth/resources/Remove.png")));
	  btnDeleteField.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
	      ((fieldTableModel) tblFields.getModel()).removeRows(tblFields.getSelectedRows());
	    }
	  });
	  btnDeleteField.setBounds(421, 261, 39, 25);
	  add(btnDeleteField);
	  
	  tblRecords = new JTable();
	  tblRecords.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	  tblRecords.setModel(new headerTableModel());
	  
	  tblRecords.getSelectionModel().addListSelectionListener(new ListSelectionListener() {     
      @Override
      public void valueChanged(ListSelectionEvent event) {
        if (tblRecords.getSelectedRow() < 0)
          return;
        
        ArrayList<Field> fields = ((headerTableModel) tblRecords.getModel()).getFields(tblRecords.getSelectedRow());
        
        /* Fill the field table with the list from the first record */
        fieldTableModel fieldModel = (fieldTableModel) tblFields.getModel();
        fieldModel.setFields(config, fields);
      }
    });

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setBounds(26, 95, 435, 154);
    scrollPane.setViewportView(tblRecords);
    add(scrollPane);
   
    JScrollPane scrollPane_1 = new JScrollPane();
    scrollPane_1.setBounds(26, 289, 435, 154);
    add(scrollPane_1);
   
    JComboBox<String> comboBox = new JComboBox<String>();
    comboBox.addItem("String");
    comboBox.addItem("Integer");
    comboBox.addItem("Boolean");
    comboBox.addItem("Date");
    comboBox.addItem("Float");
    
    tblFields = new JTable();
    tblFields.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    tblFields.setModel(new fieldTableModel());
    tblFields.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));;
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
    ((headerTableModel) tblRecords.getModel()).setConfig(this.config);
	}
	
	class headerTableModel extends AbstractTableModel{
	  private static final long serialVersionUID = 1106635947117698455L;
    private String[] columnNames = { "Description", "Identifier" };
    FileFixedWidthConfiguration config;
    
    public void setConfig(FileFixedWidthConfiguration config){
      this.config = config;
      this.fireTableDataChanged();
    }
    
    public void addRow(){
      Record rec = this.config.new Record();
      this.config.getRecords().add(rec);
      this.fireTableDataChanged();
    }
    
    public void removeRow(int row){
      this.config.getRecords().remove(row);
      this.fireTableDataChanged();
    }
    
    public ArrayList<Field> getFields(int row){
      return this.config.getRecords().get(row).getFields();
    }
    
    @Override
	  public String getColumnName(int col) {
      return columnNames[col];
	  }
	  
    @Override
    public int getColumnCount() {
      return columnNames.length;
    }

    @Override
    public int getRowCount() {
      if (this.config == null)
        return 0;
            
      return this.config.getRecords().size();
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
      return true;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
      Record rec = this.config.getRecords().get(row);
      
      if (col == 0)
        return rec.getDescription();
      else if (col == 1)
        return rec.getIdentifier();
      else return null;
    }
    
    @Override
    public void setValueAt(Object aValue, int row, int col) {
      Record rec = this.config.getRecords().get(row);
      
      if (col == 0)
        rec.setDescription(aValue.toString());
      else if (col == 1)
        rec.setIdentifier(aValue.toString());
    }
	}
	
	class fieldTableModel extends AbstractTableModel{
    private static final long serialVersionUID = 1106635947117698454L;
    private String[] columnNames = { "Field Name", "Field Type", "Format", "Start", "End" };
    ArrayList<Field> fields = new ArrayList<Field>();
    FileFixedWidthConfiguration config;
    
    public void setFields(FileFixedWidthConfiguration config, ArrayList<Field> fields){
      this.config = config;
      this.fields = fields;
      
      this.fireTableDataChanged();
    }
    
    public void addRow(){
      Field fld = this.config.new Field();
      this.fields.add(fld);
      this.fireTableDataChanged();
    }
    
    public void removeRows(int[] rows){
      for(int i = rows.length - 1; i >= 0; i--){
        this.fields.remove(rows[i]);
      }
      this.fireTableDataChanged();
    }
    
    @Override
    public String getColumnName(int col) {
      return columnNames[col];
    }
    
    @Override
    public int getColumnCount() {
      return columnNames.length;
    }

    @Override
    public int getRowCount() {
      return this.fields.size();
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
      return true;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
      Field fld = this.fields.get(row);
      
      if (col == 0)
        return fld.getFieldName();
      else if (col == 1)
        return fld.getFieldType();
      else if (col == 2)
        return fld.getFormat();
      else if (col == 3)
        return fld.getStart();
      else if (col == 4)
        return fld.getEnd();
      else return null;
    }
    
    @Override
    public void setValueAt(Object aValue, int row, int col) {
      Field fld = this.fields.get(row);
      
      if (col == 0)
        fld.setFieldName(aValue.toString());
      else if (col == 1)
        fld.setFieldType(aValue.toString());
      else if (col == 2)
        fld.setFormat(aValue.toString());
      else if (col == 3)
        fld.setStart(Integer.parseInt(aValue.toString()));
      else if (col == 4)
        fld.setEnd(Integer.parseInt(aValue.toString()));
    }
  }
}
