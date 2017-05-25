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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Pieter van der Merwe
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
    int colIndex = 0;
    for (int i = 0; i < this.config.getRecords().size(); i++) {
      Record rec = this.config.getRecords().get(i);
      rec.setRecordIndex(i);
      
      ArrayList<Field> fields = this.config.getRecords().get(i).getFields();
      for(int j = 0; j < fields.size(); j++){
        Field fld = fields.get(j);
        fld.setColumnIndex(colIndex++);
        
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
    final Object[][] rows;
    try {
      // TODO: Get Data
      rows = null;
    } catch ( Exception e ) {
      throw new ReportDataFactoryException( e.getMessage(), e );
    }

    // Add data to resultSet and do some data transformations
    for ( int row = 0; row < rows.length; row++ ) {
      final Object[] rowData = rows[ row ];

      resultSet.addRow( rowData );
    }
    return resultSet;
  }

  private Class<?> convertFieldType( final String fieldType ) {
    switch( fieldType ) {
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
