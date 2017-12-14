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
* Copyright (c) 2017 De Bortoli Wines Pty Limited (Australia). All Rights Reserved.
*/

package org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.parser;

import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Class to read Records
 *
 * @author Pieter van der Merwe and Ben Letchford
 */
public class FieldReadHandler extends AbstractXmlReadHandler {
  
  public FieldReadHandler() {
  }
  
  private String fieldName;
  private String fieldType;
  private String format;
  private int start;
  private int end;

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws SAXException if there is a parsing error.
   */
  protected void startParsing( final Attributes attrs ) throws SAXException {
    super.startParsing( attrs );
    
    fieldName = attrs.getValue( getUri(), "fieldName" );
    fieldType = attrs.getValue( getUri(), "fieldType" );
    format = attrs.getValue( getUri(), "format" );
    try{
      start = Integer.parseInt(attrs.getValue( getUri(), "start" ));
    }
    catch (Exception e){
      start = 0;
    }
    
    try{
      end = Integer.parseInt(attrs.getValue( getUri(), "end" ));
    }
    catch (Exception e){
      end = 0;
    }
  }

  /**
   * Returns the object for this element or null, if this element does not create an object.
   *
   * @return the object.
   * @throws SAXException if there is a parsing error.
   */
  public Object getObject() throws SAXException {
    return null;
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getFieldType() {
    return fieldType;
  }

  public String getFormat() {
    return format;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }
}
