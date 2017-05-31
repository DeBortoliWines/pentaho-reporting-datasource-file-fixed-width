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

package org.pentaho.reporting.ui.datasources.filefixedwidth;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.designtime.DesignTimeContext;
import org.pentaho.reporting.engine.classic.core.designtime.datafactory.DataFactoryEditorSupport;
import org.pentaho.reporting.engine.classic.core.modules.gui.commonswing.ExceptionDialog;
import org.pentaho.reporting.engine.classic.core.util.ReportParameterValues;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthConfiguration;
import org.pentaho.reporting.engine.classic.extensions.datasources.filefixedwidth.FileFixedWidthDataFactory;
import org.pentaho.reporting.libraries.designtime.swing.CommonDialog;
import org.pentaho.reporting.libraries.designtime.swing.background.CancelEvent;
import org.pentaho.reporting.libraries.designtime.swing.background.DataPreviewDialog;
import org.pentaho.reporting.libraries.designtime.swing.background.PreviewWorker;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Pieter van der Merwe
 */
public class FileFixedWidthDataSourceEditor extends CommonDialog {
  private static final long serialVersionUID = 6685784298385723490L;

  private DesignTimeContext context;
  private FileFixedWidthPanel mainPanel;

  public FileFixedWidthDataSourceEditor( final DesignTimeContext context ) {
    init( context );
  }

  public FileFixedWidthDataSourceEditor( final DesignTimeContext context, final Frame owner )
    throws HeadlessException {
    super( owner );
    init( context );
  }

  public FileFixedWidthDataSourceEditor( final DesignTimeContext context, final Dialog owner )
    throws HeadlessException {
    super( owner );
    init( context );
  }

  private void init( final DesignTimeContext context ) {
    this.context = context;

    super.init();
  }

  protected String getDialogId() {
    return "FileFixedWidthDataSourceEditor";
  }

  protected Component createContentPane() {
    mainPanel = new FileFixedWidthPanel();
    
    final JPanel cpanel = new JPanel();
    cpanel.setLayout( new BorderLayout() );
    cpanel.add( mainPanel, BorderLayout.CENTER );
    cpanel.add( new JButton( new PreviewAction() ), BorderLayout.SOUTH );

    return cpanel;
  }

  @Override
  protected boolean validateInputs( final boolean onConfirm ) {
    if ( mainPanel.getQueryName().length() == 0 ) {
      ExceptionDialog.showExceptionDialog( this, "Error", "Query Name is mandatory", null );
      return false;
    }

    return true;
  }

  public DataFactory performConfiguration( final FileFixedWidthDataFactory input ) {
    if ( input != null ) {
      mainPanel.setQueryName( input.getQueryName() );
      mainPanel.setConfiguration( input.getConfig() );
    }

    if ( performEdit() == false ) {
      return null;
    }

    return produceDataFactory();
  }

  private FileFixedWidthDataFactory produceDataFactory() {

    final FileFixedWidthDataFactory dataFactory = new FileFixedWidthDataFactory();
    final FileFixedWidthConfiguration config = mainPanel.getConfiguration();
    dataFactory.setQueryName( mainPanel.getQueryName() );
    dataFactory.setConfig( config );
    return dataFactory;
  }

  private class PreviewAction extends AbstractAction {
    private static final long serialVersionUID = 4093248389910254252L;

    private PreviewAction() {
      putValue( Action.NAME, "Preview" );
    }

    public void actionPerformed( final ActionEvent aEvt ) {
      try {
        final FileFixedWidthDataFactory dataFactory = produceDataFactory();
        DataFactoryEditorSupport.configureDataFactoryForPreview( dataFactory, context );

        final DataPreviewDialog previewDialog = new DataPreviewDialog( FileFixedWidthDataSourceEditor.this );

        final FileFixedWidthPreviewWorker worker = new FileFixedWidthPreviewWorker( dataFactory, mainPanel.getQueryName() );
        previewDialog.showData( worker );

        final ReportDataFactoryException factoryException = worker.getException();
        if ( factoryException != null ) {
          ExceptionDialog.showExceptionDialog( FileFixedWidthDataSourceEditor.this, "Error",
            "An Error Occured during preview", factoryException );
        }
      } catch ( Exception e ) {
        ExceptionDialog.showExceptionDialog( FileFixedWidthDataSourceEditor.this, "Error",
          "An Error Occured during preview", e );
      }
    }
  }


  private static class FileFixedWidthPreviewWorker implements PreviewWorker {
    private FileFixedWidthDataFactory dataFactory;
    private TableModel resultTableModel;
    private ReportDataFactoryException exception;
    private String query;

    private FileFixedWidthPreviewWorker( final FileFixedWidthDataFactory dataFactory, final String query ) {
      if ( dataFactory == null ) {
        throw new NullPointerException();
      }
      this.query = query;
      this.dataFactory = dataFactory;
    }

    public ReportDataFactoryException getException() {
      return exception;
    }

    public TableModel getResultTableModel() {
      return resultTableModel;
    }

    public void close() {
    }

    /**
     * Requests that the thread stop processing as soon as possible.
     */
    public void cancelProcessing( final CancelEvent event ) {
      dataFactory.cancelRunningQuery();
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread
     * causes the object's <code>run</code> method to be called in that separately executing thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run() {
      try {
        resultTableModel = dataFactory.queryData( query, new ReportParameterValues() );
        dataFactory.close();
      } catch ( ReportDataFactoryException e ) {
        exception = e;
      }
    }
  }
}
