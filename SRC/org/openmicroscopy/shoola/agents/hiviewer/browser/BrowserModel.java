/*
 * org.openmicroscopy.shoola.agents.hiviewer.browser.BrowserModel
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

package org.openmicroscopy.shoola.agents.hiviewer.browser;


//Java imports
import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

//Third-party libraries

//Application-internal dependencies

/** 
 * Implements {@link Browser} to maintain presentation state, thus acting
 * as the Model in MVC.
 *
 * @see BrowserControl
 * @see RootDisplay
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
class BrowserModel
    implements Browser
{

    /** Change notification table. */
    private PropertyChangeSupport   changeListeners;
    
    /** The currently selected node in the visualization tree. */
    private ImageDisplay    selectedDisplay;
    
    /** 
     * Tells if a thumbnail has been selected in the case the 
     * {@link #selectedDisplay} is an {@link ImageNode}. 
     */
    private boolean         thumbSelected;
    
    /** Position of the last pop-up trigger within the browser. */
    private Point           popupPoint;
    
    /** Contains all visualization trees, our View. */
    private RootDisplay     rootDisplay;
    
    
    /**
     * Supports reporting bound property changes.
     * Listeners will be notified only if <code>newValue</code> is not the
     * same as <code>oldValue</code>.
     * 
     * @param propertyName  The property that has been set.
     * @param oldValue      The previous value of the property.
     * @param newValue      The value that has just been set.
     */
    private void firePropertyChange(String propertyName, 
                                    Object oldValue, Object newValue) 
    {
        if (oldValue == null && newValue == null) return;
        if (oldValue != null && newValue != null && oldValue.equals(newValue))
            return;
        changeListeners.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    /**
     * Creates a new instance.
     * You then need to {@link #setController(BrowserControl) specify}
     * the controller component.
     * 
     * @param view The root display of the visualization trees.  Each child node
     *              is the top node of a visualization tree.
     *              Mustn't be <code>null</code>.
     */
    BrowserModel(RootDisplay view)
    {
        if (view == null) throw new NullPointerException("No view.");
        changeListeners = new PropertyChangeSupport(this);
        rootDisplay = view;
    }
    
    /**
     * Implemented as specified by the {@link Browser} interface.
     * @see Browser#setSelectedDisplay(ImageDisplay)
     */
    public void setSelectedDisplay(ImageDisplay node)
    {
        thumbSelected = false;
        popupPoint = null;
        Object oldValue = selectedDisplay;
        selectedDisplay = node;
        firePropertyChange(SELECTED_DISPLAY_PROPERTY, oldValue, node);
    }
    
    /**
     * Implemented as specified by the {@link Browser} interface.
     * @see Browser#getSelectedDisplay()
     */
    public ImageDisplay getSelectedDisplay() { return selectedDisplay; }

    /**
     * Implemented as specified by the {@link Browser} interface.
     * @see Browser#setThumbSelected(boolean)
     */
    public void setThumbSelected(boolean selected)
    {
        if (!(selectedDisplay instanceof ImageNode) && selected)
            throw new IllegalArgumentException(
                "Can only select a thumbnail on an ImageNode.");
        popupPoint = null;
        Boolean oldVal = thumbSelected ? Boolean.TRUE : Boolean.FALSE,
                newVal = selected ? Boolean.TRUE : Boolean.FALSE;
        thumbSelected = selected;
        firePropertyChange(THUMB_SELECTED_PROPERTY, oldVal, newVal);
    }

    /**
     * Implemented as specified by the {@link Browser} interface.
     * @see Browser#isThumbSelected()
     */
    public boolean isThumbSelected() { return thumbSelected; }

    /**
     * Implemented as specified by the {@link Browser} interface.
     * @see Browser#setPopupPoint(java.awt.Point)
     */
    public void setPopupPoint(Point p)
    {
        thumbSelected = false;
        SwingUtilities.convertPointToScreen(p, selectedDisplay);
        Object oldValue = popupPoint;
        popupPoint = p;
        firePropertyChange(POPUP_POINT_PROPERTY, oldValue, p);
    }

    /**
     * Implemented as specified by the {@link Browser} interface.
     * @see Browser#getPopupPoint()
     */
    public Point getPopupPoint() { return popupPoint; }

    /**
     * Implemented as specified by the {@link Browser} interface.
     * @see Browser#getSelectedDisplay()
     */
    public Set getImages()
    { 
        ImageFinder finder = new ImageFinder();
        accept(finder);
        return finder.getImages(); 
    }
    
    /**
     * Implemented as specified by the {@link Browser} interface.
     * @see Browser#getSelectedDisplay()
     */
    public Set getImageNodes()
    { 
        ImageFinder finder = new ImageFinder();
        accept(finder);
        return finder.getImageNodes(); 
    }
    
    /**
     * Implemented as specified by the {@link Browser} interface.
     * @see oBrowser#accept(ImageDisplayVisitor)
     */
    public void accept(ImageDisplayVisitor visitor) 
    {
        rootDisplay.accept(visitor);
    }

    /**
     * Implemented as specified by the {@link Browser} interface.
     * @see Browser#getUI()
     */
    public JComponent getUI() { return rootDisplay; }

    /**
     * Implemented as specified by the {@link Browser} interface.
     * @see Browser#addPropertyChangeListener(PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener observer)
    {
        if (observer == null) throw new NullPointerException("No observer.");
        changeListeners.addPropertyChangeListener(observer);
    }

    /**
     * Implemented as specified by the {@link Browser} interface.
     * @see Browser#removePropertyChangeListener(PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener observer)
    {
        if (observer == null) throw new NullPointerException("No observer.");
        changeListeners.removePropertyChangeListener(observer);
    }

    /**
     * Implemented as specified by the {@link Browser} interface.
     * @see Browser#addPropertyChangeListener(String, PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, 
                                          PropertyChangeListener observer)
    {
        if (propertyName == null) 
            throw new NullPointerException("No property name.");
        if (observer == null) throw new NullPointerException("No observer.");
        changeListeners.addPropertyChangeListener(propertyName, observer);
    }

    /**
     * Implemented as specified by the {@link Browser} interface.
     * @see Browser#removePropertyChangeListener(String, PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, 
                                             PropertyChangeListener observer)
    {
        if (propertyName == null) 
            throw new NullPointerException("No property name.");
        if (observer == null) throw new NullPointerException("No observer.");
        changeListeners.removePropertyChangeListener(propertyName, observer);
    }

}
