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
* Copyright (c) 2011 - 2012 De Bortoli Wines Pty Limited (Australia). All Rights Reserved.
*/

package org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.writer;

import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration.Field;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration.Record;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthDataFactory;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthModule;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.xmlns.common.AttributeList;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriter;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriterSupport;

import java.io.IOException;

/**
 * Helper class to serialise OpenERPConfiguration to XML
 *
 * @author Pieter van der Merwe and Ben Letchford
 */
public class FileFixedWidthDataFactoryHelper {

  public static void writeXML( FileFixedWidthDataFactory dataFactory, XmlWriter xmlWriter ) throws IOException {

    final AttributeList rootAttrs = new AttributeList();
    rootAttrs.addNamespaceDeclaration( "data", FileFixedWidthModule.NAMESPACE );

    xmlWriter.writeTag( FileFixedWidthModule.NAMESPACE, "file-fixed-width-datasource", rootAttrs, XmlWriter.OPEN );

    final AttributeList configAttrs = new AttributeList();

    if ( StringUtils.isEmpty( dataFactory.getQueryName() ) == false ) {
      configAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "queryName", dataFactory.getQueryName() );
    }

    FileFixedWidthConfiguration config = dataFactory.getConfig();

    if ( StringUtils.isEmpty( config.getFileLocation() ) == false ) {
      configAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "fileLocation", config.getFileLocation() );
    }

    xmlWriter.writeTag( FileFixedWidthModule.NAMESPACE, "config", configAttrs, XmlWriterSupport.CLOSE );

    for ( Record rec : config.getRecords() ) {
      final AttributeList recordAttributes = new AttributeList();
      recordAttributes.setAttribute( FileFixedWidthModule.NAMESPACE, "description", rec.getDescription() );
      recordAttributes.setAttribute( FileFixedWidthModule.NAMESPACE, "identifier", rec.getIdentifier() );
      
      xmlWriter.writeTag( FileFixedWidthModule.NAMESPACE, "record", recordAttributes, XmlWriterSupport.OPEN );

      for (Field fld : rec.getFields()){
        final AttributeList fieldAttributes = new AttributeList();
        
        fieldAttributes.setAttribute( FileFixedWidthModule.NAMESPACE, "fieldName", fld.getFieldName() );
        fieldAttributes.setAttribute( FileFixedWidthModule.NAMESPACE, "fieldType", fld.getFieldType() );
        fieldAttributes.setAttribute( FileFixedWidthModule.NAMESPACE, "format", fld.getFormat() );
        fieldAttributes.setAttribute( FileFixedWidthModule.NAMESPACE, "start", Integer.toString(fld.getStart()) );
        fieldAttributes.setAttribute( FileFixedWidthModule.NAMESPACE, "end", Integer.toString(fld.getEnd()) );
        
        xmlWriter.writeTag( FileFixedWidthModule.NAMESPACE, "field", fieldAttributes, XmlWriterSupport.CLOSE );
      }
      
      // Close record tag
      xmlWriter.writeCloseTag();
    }

    xmlWriter.writeCloseTag();
    xmlWriter.close();

  }
}
