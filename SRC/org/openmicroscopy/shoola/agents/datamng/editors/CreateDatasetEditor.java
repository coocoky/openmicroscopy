/*
 * org.openmicroscopy.shoola.agents.datamng.editors.CreateDatasetEditor
 *
 *------------------------------------------------------------------------------
 *
 *  Copyright (C) 2004 Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.agents.datamng.editors;

//Java imports
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.datamng.DataManager;
import org.openmicroscopy.shoola.agents.datamng.DataManagerCtrl;
import org.openmicroscopy.shoola.agents.datamng.IconManager;
import org.openmicroscopy.shoola.env.config.Registry;
import org.openmicroscopy.shoola.env.data.model.DatasetData;


/** 
 * 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author  <br>Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:a.falconi@dundee.ac.uk">
 * 					a.falconi@dundee.ac.uk</a>
 * @version 2.2 
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
public class CreateDatasetEditor
	extends JDialog
{	
	private Registry 					registry;
	private CreateDatasetPane 			creationPane;
	private CreateDatasetProjectsPane	projectsPane;
	private CreateDatasetImagesPane		imagesPane;
	private CreateDatasetEditorManager	manager;
	
	public CreateDatasetEditor(Registry registry, DataManagerCtrl control,
								DatasetData model, List projects, List images)
	{
		super((JFrame) registry.getTopFrame().getFrame(), true);
		this.registry = registry;
		manager = new CreateDatasetEditorManager(this,control, model, projects,
												images);
		creationPane = new CreateDatasetPane(manager, registry);
		projectsPane = new CreateDatasetProjectsPane(manager);
		imagesPane = new CreateDatasetImagesPane(manager);
		buildGUI();
		manager.initListeners();
		setSize(DataManager.EDITOR_WIDTH+100, DataManager.EDITOR_HEIGHT);
	}
	
	public CreateDatasetEditorManager getManager()
	{ 
		return manager;
	}
	
	/** 
	 * Returns the save button displayed in {@link CreateDatasetPane}.
	 */
	public JButton getSaveButton()
	{
		return creationPane.getSaveButton();
	}

	/** 
	 * Returns the select button displayed in {@link CreateDatasetProjectsPane}.
	 */
	public JButton getSelectButton()
	{
		return projectsPane.getSelectButton();
	}

	/** 
	 * Returns the cancel button displayed in {@link CreateDatasetProjectsPane}.
	 */
	public JButton getCancelButton()
	{
		return projectsPane.getCancelButton();
	}
	
	/** 
	 * Returns the select button displayed in {@link CreateDatasetImagesPane}.
	 */
	public JButton getSelectImageButton()
	{
		return imagesPane.getSelectButton();
	}

	/** 
	 * Returns the cancel button displayed in {@link CreateDatasetImagesPane}.
	 */
	public JButton getCancelImageButton()
	{
		return imagesPane.getCancelButton();
	}
	
	/** Forward event to the pane {@link CreateDatasetProjectsPane}. */
	public void	selectAllProjects()
	{
		projectsPane.setSelection(new Boolean(true));
	}

	/** Forward event to the pane {@link CreateDatasetProjectsPane}. */
	public void	cancelSelectionProject()
	{
		projectsPane.setSelection(new Boolean(false));
	}
	
	/** Forward event to the pane {@link CreateDatasetImagesPane}. */
	public void	selectAllImages()
	{
		imagesPane.setSelection(new Boolean(true));
	}

	/** Forward event to the pane {@link CreateDatasetImagesPane}. */
	public void	cancelSelectionImage()
	{
		imagesPane.setSelection(new Boolean(false));
	}
	
	/** Build and layout the GUI. */
	private void buildGUI()
	{
		//create and initialize the tabs
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, 
										  JTabbedPane.WRAP_TAB_LAYOUT);
		tabs.setAlignmentX(LEFT_ALIGNMENT);
		IconManager IM = IconManager.getInstance(registry);
		//TODO: specify lookup name.
		Font font = (Font) registry.lookup("/resources/fonts/Titles");
		tabs.addTab("New Dataset", IM.getIcon(IconManager.DATASET), 
					creationPane);
		tabs.addTab("Add to Projects", IM.getIcon(IconManager.PROJECT), 
					projectsPane);
		tabs.addTab("Add Images", IM.getIcon(IconManager.IMAGE), 
					imagesPane);			
		tabs.setSelectedComponent(creationPane);
		tabs.setFont(font);
		tabs.setForeground(DataManager.STEELBLUE);
		//set layout and add components
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(tabs, BorderLayout.CENTER);
	}

}
