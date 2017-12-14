/*
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
* Foundation.
*
* You should have received a copy of the GNU Lesser General Public License along with this
* program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
* or from the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU Lesser General Public License for more details.
*
* Copyright (c) 2011, 2012, 2017 De Bortoli Wines Pty Limited (Australia). All Rights Reserved.
*/

package org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.AbstractDataFactory;
import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.util.PropertyLookupParser;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration.Field;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration.Record;

import javax.swing.table.TableModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.lang.StringIndexOutOfBoundsException;

/**
 * @author Pieter van der Merwe and Ben Letchford
 */
public class FileFixedWidthDataFactory extends AbstractDataFactory {
  private static final long serialVersionUID = -6235833289788643577L;
  private static final Log logger = LogFactory.getLog( FileFixedWidthDataFactory.class );

  private FileFixedWidthConfiguration config;
  private String queryName;

  public FileFixedWidthDataFactory() {
  }

  /**
   * Checks whether the query would be executable by this datafactory. This performs a rough check, not a full query.
   *
   * @param query
   * @param parameters
   * @return
   */
  public boolean isQueryExecutable( final String query, final DataRow parameters ) {
    return queryName.equals( query );
  }

  public String[] getQueryNames() {
    return new String[] { queryName };
  }

  /**
   * Queries a datasource. The string 'query' defines the name of the query. The Parameterset given here may contain
   * more data than actually needed.
   * <p/>
   * The dataset may change between two calls, do not assume anything!
   *
   * @param query
   * @param parameters
   * @return
   */
  public synchronized TableModel queryData( final String query, final DataRow parameters )
    throws ReportDataFactoryException {

    /// TODO: Add more validation Here
    if ( config == null ) {
      throw new ReportDataFactoryException( "Configuration is empty." ); //$NON-NLS-1$
    }

    final TypedTableModel resultSet = new TypedTableModel();

    final int queryLimit = calculateQueryLimit( parameters );

    // All fields that are defined are returned.  They are simply joined together.
    for (int i = 0; i < this.config.getRecords().size(); i++) {
      Record rec = this.config.getRecords().get(i);
      rec.setRecordIndex(i);

      for(Field fld : rec.getFields()){
        fld.setColumnIndex(resultSet.getColumnCount());
        resultSet.addColumn(fld.getFieldName(), convertFieldType( fld.getFieldType() ) );
      }
    }

    // Called by the designer to get column layout, return a empty resultSet with columns already set
    if ( queryLimit == 1 ) {
      return resultSet;
    }

    // Parameter parser to replace parameters in strings
    final PropertyLookupParser parameterParser = new PropertyLookupParser() {
      private static final long serialVersionUID = -7264648195698966110L;

      @Override
      protected String lookupVariable( final String property ) {
        return parameters.get( property ).toString();
      }
    };

    // Get the data
    // All rows are effectively 'inner join'ed into one row where every next record is added to the back.
    // If a record repeats then a new line is created with all the earlier / parent data repeated.
    Object[] rowData = null;
    try {
      int prevRecordIndex = -1;
      try (BufferedReader br = new BufferedReader(new FileReader(this.config.getFileLocation()))){
        while (true){
          String newline = br.readLine();

          // End of file, commit the last record and go
          if (newline == null){
            if (rowData != null)
              resultSet.addRow( rowData );

            break;
          }

          // Identify the record being loaded
          Record newRecord = null;
          for (Record r : this.config.getRecords()){
            if (r.getIdentifier().equals(newline.substring(0, r.getIdentifier().length()))){
              newRecord = r;
              break;
            }
          }

          // Didn't define the record for this line
          if (newRecord == null)
            continue;

          if (rowData == null)
            rowData = new Object[resultSet.getColumnCount()];
          else if (newRecord.getRecordIndex() <= prevRecordIndex){
            // If we are getting a repeating record, get a new line

            Object[] newRow = new Object[rowData.length];
            if (rowData != null){
              // Save previous row first
              resultSet.addRow( rowData );

              // Copy parent fields
              for (Record r: this.config.getRecords()){
                if (r.getRecordIndex() < newRecord.getRecordIndex()){
                  for (Field f: r.getFields()){
                    newRow[f.getColumnIndex()] = rowData[f.getColumnIndex()];
                  }
                }
              }
            }

            rowData = newRow;
          }

          prevRecordIndex = newRecord.getRecordIndex();

          // Extract the data and pop it in the row
          for(Field f : newRecord.getFields()){

            Object data;
            try {
              data = newline.substring(f.getStart() - 1, (f.getStart() - 1) + f.getEnd());
              switch( f.getFieldType() ) {
                case "Boolean":
                data = Boolean.parseBoolean(data.toString());
                case "Integer":
                data = Integer.parseInt(data.toString());
                case "Float":
                data =  Float.parseFloat(data.toString());
                case "Date":
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(f.getFormat());
                data = LocalDateTime.from(dtf.parse(data.toString()));
                default:
                data = data.toString();
              }
            } catch (StringIndexOutOfBoundsException e) {
              data = "";
            }

            rowData[f.getColumnIndex()] = data;
          }
        } // While true
      } // Try
    } catch (FileNotFoundException e1) {
      throw new ReportDataFactoryException( "Could not find file: "
          + this.config.getFileLocation(), e1 );
    } catch ( Exception e ) {
      throw new ReportDataFactoryException( e.getMessage(), e );
    }

    return resultSet;
  }

  private Class<?> convertFieldType( final String fieldType ) {
    if (fieldType == null)
      return String.class;
    else switch( fieldType ) {
      case "Boolean":
        return Boolean.class;
      case "Integer":
        return Integer.class;
      case "Float":
        return Float.class;
      case "Date":
        return Date.class;
      default:
        return String.class;
    }
  }

  public void setConfig( final FileFixedWidthConfiguration config ) {
    this.config = config;
  }

  public String getQueryName() {
    return queryName;
  }

  public void setQueryName( final String queryName ) {
    this.queryName = queryName;
  }

  public void close() {
  }

  public FileFixedWidthDataFactory clone() {
    final FileFixedWidthDataFactory dataFactory = (FileFixedWidthDataFactory) super.clone();
    if ( this.config != null ) {
      dataFactory.config = this.config.clone();
    }
    return dataFactory;
  }

  public FileFixedWidthConfiguration getConfig() {
    return config;
  }
}
