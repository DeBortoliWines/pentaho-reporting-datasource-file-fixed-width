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
* Copyright (c) 2011-2012 De Bortoli Wines Pty Limited (Australia). All Rights Reserved.
*/

package org.pentaho.reporting.ui.datasources.filefixedwidth;

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
 * Copyright (c) 2011-2012 De Bortoli Wines Pty Limited (Australia). All Rights Reserved.
 */

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.designtime.DataFactoryChangeRecorder;
import org.pentaho.reporting.engine.classic.core.designtime.DataSourcePlugin;
import org.pentaho.reporting.engine.classic.core.designtime.DesignTimeContext;
import org.pentaho.reporting.engine.classic.core.metadata.DataFactoryMetaData;
import org.pentaho.reporting.engine.classic.core.metadata.DataFactoryRegistry;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthDataFactory;

import javax.swing.*;
import java.awt.*;

/**
 * @author Pieter van der Merwe and Ben Letchford
 */
public class FileFixedWidthDataSourcePlugin implements DataSourcePlugin {
	public FileFixedWidthDataSourcePlugin() {
	}

	public DataFactory performEdit(final DesignTimeContext context, final DataFactory input, final String queryName,
			final DataFactoryChangeRecorder changeRecorder) {
		final FileFixedWidthDataSourceEditor editor;
		final Window window = context.getParentWindow();
		if (window instanceof JDialog) {
			editor = new FileFixedWidthDataSourceEditor(context, (JDialog) window);
		} else if (window instanceof JFrame) {
			editor = new FileFixedWidthDataSourceEditor(context, (JFrame) window);
		} else {
			editor = new FileFixedWidthDataSourceEditor(context);
		}
		return editor.performConfiguration((FileFixedWidthDataFactory) input);
	}

	public boolean canHandle(final DataFactory dataFactory) {
		return dataFactory instanceof FileFixedWidthDataFactory;
	}

	public DataFactoryMetaData getMetaData() {
		return DataFactoryRegistry.getInstance().getMetaData(FileFixedWidthDataFactory.class.getName());
	}
}
