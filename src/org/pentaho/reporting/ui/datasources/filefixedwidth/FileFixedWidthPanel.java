package org.pentaho.reporting.ui.datasources.filefixedwidth;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.pentaho.reporting.engine.classic.core.designtime.DesignTimeContext;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration.Field;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration.Record;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.designtime.swing.BorderlessButton;
import org.pentaho.reporting.libraries.designtime.swing.VerticalLayout;

public class FileFixedWidthPanel extends JPanel {
	private class BrowseButtonAction extends AbstractAction {
		private BrowseButtonAction() {
			putValue(Action.NAME, "Browse..");
		}

		public void actionPerformed(final ActionEvent e) {
			final File initiallySelectedFile;
			final String fileName = filenameField.getText();
			if (StringUtils.isEmpty(fileName, true) == false) {
				initiallySelectedFile = new File(fileName);
			} else {
				initiallySelectedFile = new File("");
			}

			final JFileChooser fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(initiallySelectedFile);
			final int success = fileChooser.showOpenDialog(FileFixedWidthPanel.this);
			if (success != JFileChooser.APPROVE_OPTION) {
				return;
			}
			final File file = fileChooser.getSelectedFile();
			if (file == null) {
				return;
			}

			final String path = file.getPath();

			filenameField.setText(path);
		}
	}

	private static final long serialVersionUID = -5158167375579708829L;
	private FileFixedWidthConfiguration config = new FileFixedWidthConfiguration();

	private DesignTimeContext designTimeContext;
	private Preferences preferences;

	private JTextField textQueryName;
	private JTextField filenameField;
	private JTable tblRecords;
	private JTable tblFields;

	/**
	 * Create the panel.
	 */
	public FileFixedWidthPanel() {
		setLayout(new VerticalLayout(5, VerticalLayout.BOTH));

		// Query Name
		JLabel queryNameStringLabel = new JLabel("Query Name");
		queryNameStringLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 8, 0));
		add(queryNameStringLabel);

		final JPanel queryNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		textQueryName = new JTextField(null, 0);
		textQueryName.setColumns(30);
		queryNamePanel.add(textQueryName);
		add(queryNamePanel);
		// End Query Name

		// File Browser
		JLabel fixedFileLabel = new JLabel("Fixed File Location");
		fixedFileLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 8, 0));
		add(fixedFileLabel);

		final JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filenameField = new JTextField(null, 0);
		fixedFileLabel.setLabelFor(filenameField);
		filenameField.setColumns(30);
		filePanel.add(filenameField);
		filePanel.add(new JButton(new BrowseButtonAction()));
		add(filePanel);
		// End File Browser

		// Row Identifier
		final JPanel rowIdentifierPanel = new JPanel(new BorderLayout());

		JLabel rowIdentifierLabel = new JLabel("Row Identifiers");
		rowIdentifierLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 8, 0));
		rowIdentifierPanel.add(rowIdentifierLabel, BorderLayout.WEST);

		final JPanel rowIdentifierButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		BorderlessButton btnRecordAdd = new BorderlessButton("");
		btnRecordAdd.setIcon(new ImageIcon(FileFixedWidthPanel.class
				.getResource("/org/pentaho/reporting/ui/datasources/filefixedwidth/resources/Add.png")));
		btnRecordAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				((headerTableModel) tblRecords.getModel()).addRow();
			}
		});
		rowIdentifierButtonPanel.add(btnRecordAdd);

		BorderlessButton btnRecordDel = new BorderlessButton("");
		btnRecordDel.setIcon(new ImageIcon(FileFixedWidthPanel.class
				.getResource("/org/pentaho/reporting/ui/datasources/filefixedwidth/resources/Remove.png")));
		btnRecordDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tblRecords.getSelectedRow() != -1) {
					// remove selected row from the model
					((headerTableModel) tblRecords.getModel()).removeRow(tblRecords.getSelectedRow());
				}
			}
		});
		rowIdentifierButtonPanel.add(btnRecordDel);

		rowIdentifierPanel.add(rowIdentifierButtonPanel, BorderLayout.EAST);

		add(rowIdentifierPanel);

		tblRecords = new JTable();
		tblRecords.setPreferredScrollableViewportSize(
				new Dimension(tblRecords.getPreferredSize().width, tblRecords.getRowHeight() * 5));
		tblRecords.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblRecords.setModel(new headerTableModel());

		tblRecords.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (tblRecords.getSelectedRow() < 0)
					return;

				ArrayList<Field> fields = ((headerTableModel) tblRecords.getModel())
						.getFields(tblRecords.getSelectedRow());

				/* Fill the field table with the list from the first record */
				fieldTableModel fieldModel = (fieldTableModel) tblFields.getModel();
				fieldModel.setFields(config, fields);
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(tblRecords);
		add(scrollPane);
		// End Row Identifier

		// Field Definitions
		final JPanel fieldDefinitionsPanel = new JPanel(new BorderLayout());

		JLabel fieldDefinitionsLabel = new JLabel("Field Definitions");
		fieldDefinitionsLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 8, 0));
		fieldDefinitionsPanel.add(fieldDefinitionsLabel, BorderLayout.WEST);

		final JPanel fieldDefinitionsButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		BorderlessButton btnAddField = new BorderlessButton("");
		btnAddField.setIcon(new ImageIcon(FileFixedWidthPanel.class
				.getResource("/org/pentaho/reporting/ui/datasources/filefixedwidth/resources/Add.png")));
		btnAddField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				((fieldTableModel) tblFields.getModel()).addRow();
			}
		});
		fieldDefinitionsButtonPanel.add(btnAddField);

		BorderlessButton btnDeleteField = new BorderlessButton("");
		btnDeleteField.setIcon(new ImageIcon(FileFixedWidthPanel.class
				.getResource("/org/pentaho/reporting/ui/datasources/filefixedwidth/resources/Remove.png")));
		btnDeleteField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((fieldTableModel) tblFields.getModel()).removeRows(tblFields.getSelectedRows());
			}
		});
		fieldDefinitionsButtonPanel.add(btnDeleteField);

		fieldDefinitionsPanel.add(fieldDefinitionsButtonPanel, BorderLayout.EAST);
		add(fieldDefinitionsPanel);

		JScrollPane scrollPane_1 = new JScrollPane();
		add(scrollPane_1);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItem("String");
		comboBox.addItem("Integer");
		comboBox.addItem("Boolean");
		comboBox.addItem("Date");
		comboBox.addItem("Float");

		tblFields = new JTable();
		tblFields.setPreferredScrollableViewportSize(
				new Dimension(tblFields.getPreferredSize().width, tblFields.getRowHeight() * 20));
		tblFields.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tblFields.setModel(new fieldTableModel());
		tblFields.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));
		scrollPane_1.setViewportView(tblFields);
		// End Field Definitions

		((headerTableModel) tblRecords.getModel()).setConfig(this.config);
	}

	public void setQueryName(String queryName) {
		textQueryName.setText(queryName);
	}

	public String getQueryName() {
		return textQueryName.getText();
	}

	public FileFixedWidthConfiguration getConfiguration() {
		this.config.setFileLocation(filenameField.getText());
		return this.config;
	}

	public void setConfiguration(FileFixedWidthConfiguration config) {
		if (config != null)
			this.config = config;

		filenameField.setText(this.config.getFileLocation());
		((headerTableModel) tblRecords.getModel()).setConfig(this.config);
	}

	class headerTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1106635947117698455L;
		private String[] columnNames = { "Description", "Identifier" };
		FileFixedWidthConfiguration config;

		public void setConfig(FileFixedWidthConfiguration config) {
			this.config = config;
			this.fireTableDataChanged();
		}

		public void addRow() {
			Record rec = this.config.new Record();
			this.config.getRecords().add(rec);
			this.fireTableDataChanged();
		}

		public void removeRow(int row) {
			this.config.getRecords().remove(row);
			this.fireTableDataChanged();
		}

		public ArrayList<Field> getFields(int row) {
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
			else
				return null;
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

	class fieldTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1106635947117698454L;
		private String[] columnNames = { "Field Name", "Field Type", "Format", "Start", "Length" };
		ArrayList<Field> fields = new ArrayList<Field>();
		FileFixedWidthConfiguration config;

		public void setFields(FileFixedWidthConfiguration config, ArrayList<Field> fields) {
			this.config = config;
			this.fields = fields;

			this.fireTableDataChanged();
		}

		public void addRow() {
			Field fld = this.config.new Field();
			this.fields.add(fld);
			this.fireTableDataChanged();
		}

		public void removeRows(int[] rows) {
			for (int i = rows.length - 1; i >= 0; i--) {
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
			else
				return null;
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
