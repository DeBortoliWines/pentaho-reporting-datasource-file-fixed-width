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

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Class to read Records
 *
 * @author Pieter van der Merwe
 */
public class RecordReadHandler extends AbstractXmlReadHandler {
  
  private ArrayList<FieldReadHandler> fieldReadHandlers = new ArrayList<FieldReadHandler>();
  @SuppressWarnings("unused")
  private static final Log logger = LogFactory.getLog( RecordReadHandler.class );
  
  public RecordReadHandler() {
  }
  
  private String description;
  private String identifier;

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws SAXException if there is a parsing error.
   */
  protected void startParsing( final Attributes attrs ) throws SAXException {
    super.startParsing( attrs );
    
    description = attrs.getValue( getUri(), "description" );
    identifier = attrs.getValue( getUri(), "identifier" );
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

    if ( "field".equals( tagName ) ) {
      final FieldReadHandler fieldReadHandler = new FieldReadHandler();
      fieldReadHandlers.add(fieldReadHandler);
      return fieldReadHandler;
    }
    
    return null;
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
  
  public ArrayList<FieldReadHandler> getFieldHandles(){
    return fieldReadHandlers;
  }

  public String getDescription() {
    return description;
  }
  
  public String getIdentifier(){
    return identifier;
  }
}
