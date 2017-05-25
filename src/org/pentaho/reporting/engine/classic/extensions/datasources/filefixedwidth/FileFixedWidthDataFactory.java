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
    
    // Build a hashmap to pass all parameters as a dictionary to a custom OpenERP procedure
    for ( final String paramName : parameters.getColumnNames() ) {
      Object value = parameters.get( paramName );
      if ( value == null ) {
        value = false;
      }

      // TODO: insert parameter values here
    }

    // TODO: Build column list
    /* Can't get selected fields from config, because we may be calling a custom function
    ArrayList<OpenERPFieldInfo> selectedFields = null;
    try {
      selectedFields = helper.getFields( targetConfig, openERPParams );
    } catch ( Exception e1 ) {
      throw new ReportDataFactoryException( "Failed to select field", e1 );
    }

    // Build a field list
    for ( final OpenERPFieldInfo selectedFld : selectedFields ) {
      resultSet.addColumn( selectedFld.getRenamedFieldName(), convertFieldType( selectedFld.getFieldType() ) );
    }
    */
    
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

    // Replace parameterized filters with values from parameters
    /*if ( configFilters != null ) {
      for ( final OpenERPFilterInfo filter : configFilters ) {
        // You could have set a filter without using the Designer.  Then the filter could be any data type that
        // should not be converted to a String.
        if ( filter.getValue() instanceof String ) {
          try {
            final String realFilterValue = filter.getValue().toString();

            // If you specify the filter on its own, try in get the object value
            // Not all parameter values are a string.  Could be an Object[] of ids for example in a multi-select
            // parameter
            final Object filterValue;
            if ( realFilterValue.length() >= 4
              && realFilterValue.substring( 0, 2 ).equals( "${" )
              && realFilterValue.endsWith( "}" ) ) {

              final String parameterName = realFilterValue.substring( 2, realFilterValue.length() - 1 );
              filterValue = parameters.get( parameterName );
            }
            // Cater for cases where users specify compound filer: "name" "like" "some${post_fix}"
            else {
              filterValue = parameterParser.translateAndLookup( realFilterValue, parameters );
            }

            // If the value is null, this may be a dependent query and it is waiting for a parameter.
            // just return and wait
            if ( filterValue == null ) {
              return resultSet;
            }

            filter.setValue( filterValue );
          } catch ( Exception e ) {
            throw new ReportDataFactoryException( e.getMessage(), e );
          }
        }
      }
    }
*/
    
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

  /*private Class<?> convertFieldType( final FieldType fieldType ) {
    switch( fieldType ) {
      case BINARY:
        return Byte[].class;
      case BOOLEAN:
        return Boolean.class;
      case INTEGER:
        return Integer.class;
      case FLOAT:
        return Float.class;
      case DATETIME:
      case DATE:
        return Date.class;
      case MANY2ONE:
        return Integer.class;
      case ONE2MANY:
      case MANY2MANY:
      case CHAR:
      case TEXT:
      default:
        return String.class;
    }
    
  }
  */
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
