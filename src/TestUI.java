import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JDialog;

import org.apache.xmlbeans.impl.common.IdentityConstraint.FieldState;
import org.codehaus.groovy.transform.FieldASTTransformation;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration.Field;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration.Record;
import org.pentaho.reporting.ui.datasources.filefixedwidth.FileFixedWidthPanel;

public class TestUI {

  public static void main(String[] args) {
    FileFixedWidthConfiguration config = new FileFixedWidthConfiguration();
    config.setFileLocation("/home/pietercvdm/Documents/Werk/Invoice/invoice.txt");
    
    ArrayList<Record> records = config.getRecords();
    
    Record rec = config.new Record();
    rec.setDescription("Header");
    rec.setIdentifier("0");
    
    ArrayList<Field> fields = rec.getFields();
    Field fld = config.new Field();
    fld.setFieldName("invoice_number");
    fld.setFieldType("String");
    fld.setStart(1);
    fld.setEnd(8);
    fields.add(fld);
    
    fld = config.new Field();
    fld.setFieldName("company_name");
    fld.setFieldType("String");
    fld.setStart(9);
    fld.setEnd(47);
    fields.add(fld);    
    
    records.add(rec);
    
    rec = config.new Record();
    rec.setDescription("Detail");
    rec.setIdentifier("2");
    
    fields = rec.getFields();
    fld = config.new Field();
    fld.setFieldName("customer_name");
    fld.setFieldType("String");
    fld.setStart(2);
    fld.setEnd(39);
    fields.add(fld);
    
    records.add(rec);
    
    // Open the dialog to configure your data source
    JDialog dialog = new JDialog();
    dialog.getContentPane().setLayout(new BorderLayout());
    FileFixedWidthPanel panel = new FileFixedWidthPanel();
    panel.setQueryName("Queue1");
    panel.setConfiguration(config);
    panel.setVisible(true);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dialog.getContentPane().add(panel,BorderLayout.CENTER);
    dialog.setBounds(100, 100, 1024, 768);
    dialog.setModal(true);
    dialog.setVisible(true);
  }
}
