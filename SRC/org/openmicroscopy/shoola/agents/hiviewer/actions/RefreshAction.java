/*
 * org.openmicroscopy.shoola.agents.hiviewer.actions.RefreshAction
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

package org.openmicroscopy.shoola.agents.hiviewer.actions;


//Java imports
import java.awt.event.ActionEvent;
import javax.swing.Action;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.hiviewer.IconManager;
import org.openmicroscopy.shoola.agents.hiviewer.view.HiViewer;
import org.openmicroscopy.shoola.agents.hiviewer.view.HiViewerFactory;
import org.openmicroscopy.shoola.util.ui.UIUtilities;

/** 
 * Reloads the Model of its viewer.
 * For the time being we just reinstantiate the viewer and discard the 
 * existing one.
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
public class RefreshAction
    extends HiViewerAction
{

    /** The name of the action. */
    private static final String NAME = "Refresh";
    
    /** The description of the action. */
    private static final String DESCRIPTION = "Refresh this window.";
    
    /**
     * Creates a new instance.
     * 
     * @param model Reference to the Model. Mustn't be <code>null</code>.
     */
    public RefreshAction(HiViewer model)
    {
        super(model);
        setEnabled(true);
        putValue(Action.NAME, NAME);
        putValue(Action.SHORT_DESCRIPTION, 
                UIUtilities.formatToolTipText(DESCRIPTION));
        IconManager icons = IconManager.getInstance();
        putValue(Action.SMALL_ICON, icons.getIcon(IconManager.REFRESH));
    }

    /**
     * Reloads the viewer. 
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) 
    { 
        HiViewer newOne = HiViewerFactory.reinstantiate(model);
        model.discard();
        newOne.activate(newOne.getUI().getBounds());
    }
    
}
