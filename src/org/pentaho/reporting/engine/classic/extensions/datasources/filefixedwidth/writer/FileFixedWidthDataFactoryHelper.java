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

import com.debortoliwines.openerp.reporting.di.OpenERPConfiguration;
import com.debortoliwines.openerp.reporting.di.OpenERPFieldInfo;
import com.debortoliwines.openerp.reporting.di.OpenERPFilterInfo;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.PasswordEncryptionService;
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
 * @author Pieter van der Merwe
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

    OpenERPConfiguration config = dataFactory.getConfig();

    if ( StringUtils.isEmpty( config.getHostName() ) == false ) {
      configAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "hostName", config.getHostName() );
    }

    configAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "portNumber", Integer.toString( config.getPortNumber() ) );

    if ( StringUtils.isEmpty( config.getDatabaseName() ) == false ) {
      configAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "databaseName", config.getDatabaseName() );
    }
    if ( StringUtils.isEmpty( config.getUserName() ) == false ) {
      configAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "userName", config.getUserName() );
    }
    if ( StringUtils.isEmpty( config.getPassword() ) == false ) {
      configAttrs.setAttribute( FileFixedWidthModule.NAMESPACE,
        "password", PasswordEncryptionService.getInstance().encrypt( config.getPassword() ) );
    }
    if ( StringUtils.isEmpty( config.getModelName() ) == false ) {
      configAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "modelName", config.getModelName() );
    }
    if ( config.getDataSource() != null ) {
      configAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "dataSource", config.getDataSource().name() );
    }

    if ( StringUtils.isEmpty( config.getCustomFunctionName() ) == false ) {
      configAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "customFunctionName", config.getCustomFunctionName() );
    }

    xmlWriter.writeTag( FileFixedWidthModule.NAMESPACE, "config", configAttrs, XmlWriterSupport.CLOSE );

    for ( OpenERPFilterInfo filter : config.getFilters() ) {
      final AttributeList filterAttrs = new AttributeList();
      filterAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "modelPath", filter.getModelPath() );
      filterAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "instanceNum", Integer.toString( filter.getInstanceNum() ) );
      filterAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "operator", filter.getOperator() );
      filterAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "fieldName", filter.getFieldName() );
      filterAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "comparator", filter.getComparator() );
      filterAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "value", filter.getValue().toString() );
      xmlWriter.writeTag( FileFixedWidthModule.NAMESPACE, "filter", filterAttrs, XmlWriterSupport.CLOSE );
    }

    for ( OpenERPFieldInfo field : config.getSelectedFields() ) {
      writeFieldInfo( xmlWriter, field );
    }

    xmlWriter.writeCloseTag();
    xmlWriter.close();

  }

  private static void writeFieldInfo( XmlWriter xmlWriter, OpenERPFieldInfo field ) throws IOException {
    final AttributeList selectedFieldAttrs = new AttributeList();
    selectedFieldAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "modelName", field.getModelName() );
    selectedFieldAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "fieldName", field.getFieldName() );
    selectedFieldAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "fieldType", field.getFieldType().name() );
    selectedFieldAttrs
      .setAttribute( FileFixedWidthModule.NAMESPACE, "instanceNum", Integer.toString( field.getInstanceNum() ) );
    selectedFieldAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "sortIndex", Integer.toString( field.getSortIndex() ) );
    selectedFieldAttrs
      .setAttribute( FileFixedWidthModule.NAMESPACE, "sortDirection", Integer.toString( field.getSortDirection() ) );
    selectedFieldAttrs.setAttribute( FileFixedWidthModule.NAMESPACE, "renamedFieldName", field.getRenamedFieldName() );
    selectedFieldAttrs
      .setAttribute( FileFixedWidthModule.NAMESPACE, "relatedChildModelName", field.getRelatedChildModelName() );

    xmlWriter.writeTag( FileFixedWidthModule.NAMESPACE, "selectedField", selectedFieldAttrs, XmlWriterSupport.OPEN );
    if ( field.getParentField() != null ) {
      writeFieldInfo( xmlWriter, field.getParentField() );
    }
    xmlWriter.writeCloseTag();
  }
}
