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
* Copyright (c) 2008 - 2009 Pentaho Corporation, .  All rights reserved.
* Copyright (c) 2011 - 2012 De Bortoli Wines Pty Limited (Australia). All Rights Reserved.
*/

package org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 * Copyright (c) 2008 - 2009 Pentaho Corporation, .  All rights reserved.
 * Copyright (c) 2011 - 2012 De Bortoli Wines Pty Limited (Australia). All Rights Reserved.
 */

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.DataFactoryReadHandler;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration.Field;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration.Record;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthDataFactory;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.parser.ConfigReadHandler;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.parser.RecordReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;

/**
 * @author Pieter van der Merwe
 */
public class FileFixedWidthDataSourceReadHandler extends AbstractXmlReadHandler implements DataFactoryReadHandler {
  private ConfigReadHandler configReadHandler;
  private RecordReadHandler currentRecordReadHandler;
  private ArrayList<RecordReadHandler> recordReadHandlers = new ArrayList<RecordReadHandler>();
  private FileFixedWidthDataFactory dataFactory;
  @SuppressWarnings("unused")
  private static final Log logger = LogFactory.getLog( FileFixedWidthDataSourceReadHandler.class );

  public FileFixedWidthDataSourceReadHandler() {
  }

  /**
   * Returns the handler for a child element.
   *
   * @param uri     the URI of the namespace of the current element.
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild( final String uri,
                                               final String tagName,
                                               final Attributes atts ) throws SAXException {
    if ( isSameNamespace( uri ) == false ) {
      return null;
    }
    
    if ( "config".equals( tagName ) ) {
      configReadHandler = new ConfigReadHandler();
      return configReadHandler;
    }

    if ( "record".equals( tagName ) ) {
      currentRecordReadHandler = new RecordReadHandler();
      recordReadHandlers.add( currentRecordReadHandler );
      return currentRecordReadHandler;
    }
    
    return null;
  }

  /**
   * Done parsing - For this element only, it isn't the same as EndElement
   *
   * @throws SAXException if there is a parsing error.
   */
  protected void doneParsing() throws SAXException {
    final FileFixedWidthDataFactory srdf = new FileFixedWidthDataFactory();
    if ( configReadHandler == null ) {
      throw new ParseException( "Required element 'config' is missing.", getLocator() );
    }

    srdf.setQueryName( configReadHandler.getQueryName() );

    FileFixedWidthConfiguration config = configReadHandler.getConfig();
    srdf.setConfig( config );
    
    for(RecordReadHandler rh : recordReadHandlers){
      Record r = config.new Record();
      r.setIdentifier(rh.getIdentifier());
      r.setDescription(rh.getDescription());
      
      for(FieldReadHandler fh : rh.getFieldHandles()){
        Field f = config.new Field();
        f.setFieldName(fh.getFieldName());
        f.setFieldType(fh.getFieldType());
        f.setFormat(fh.getFormat());
        f.setStart(fh.getStart());
        f.setEnd(fh.getEnd());
        
        r.getFields().add(f);
      }
        
      config.getRecords().add(r);
    }

    dataFactory = srdf;
  }

  /**
   * Returns the object for this element or null, if this element does not create an object.
   *
   * @return the object.
   * @throws SAXException if an parser error occured.
   */
  public Object getObject() throws SAXException {
    return dataFactory;
  }

  public DataFactory getDataFactory() {
    return dataFactory;
  }
}
