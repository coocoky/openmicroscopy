/*
 * org.openmicroscopy.shoola.agents.dataBrowser.view.DatasetsModel 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2008 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */
package org.openmicroscopy.shoola.agents.dataBrowser.view;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openmicroscopy.shoola.agents.dataBrowser.DataBrowserAgent;
import org.openmicroscopy.shoola.agents.dataBrowser.DataBrowserLoader;
import org.openmicroscopy.shoola.agents.dataBrowser.DataBrowserTranslator;
import org.openmicroscopy.shoola.agents.dataBrowser.ThumbnailLoader;
import org.openmicroscopy.shoola.agents.dataBrowser.browser.BrowserFactory;

import pojos.DatasetData;
import pojos.ImageData;

//Java imports

//Third-party libraries

//Application-internal dependencies

/** 
 * 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME3.0
 */
class DatasetsModel 
	extends DataBrowserModel
{

	private Set<DatasetData> datasets;
	

	/**
	 * Creates a new instance.
	 * 
	 * @param datasets The collection to datasets the model is for.
	 */
	DatasetsModel(Set<DatasetData> datasets)
	{
		super();
		if (datasets  == null) 
			throw new IllegalArgumentException("No images.");
		this.datasets = datasets;
		long userID = DataBrowserAgent.getUserDetails().getId();
		Set visTrees = DataBrowserTranslator.transformHierarchy(datasets, userID, 0);
        browser = BrowserFactory.createBrowser(visTrees);
        layoutBrowser();
	}
	
	protected DataBrowserLoader createDataLoader(boolean refresh)
	{
		Iterator<DatasetData> i = datasets.iterator();
		Set<ImageData> images = new HashSet<ImageData>();
		DatasetData data;
		while (i.hasNext()) {
			data = i.next();
			images.addAll(data.getImages());
		}
		return new ThumbnailLoader(component, images);
	}
}
