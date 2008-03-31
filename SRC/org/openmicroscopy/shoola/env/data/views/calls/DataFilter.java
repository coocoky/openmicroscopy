/*
 * org.openmicroscopy.shoola.env.data.views.calls.DataFilter 
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
package org.openmicroscopy.shoola.env.data.views.calls;

import java.util.List;
import java.util.Set;

import org.openmicroscopy.shoola.env.data.OmeroMetadataService;
import org.openmicroscopy.shoola.env.data.views.BatchCall;
import org.openmicroscopy.shoola.env.data.views.BatchCallTree;

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
public class DataFilter 
	extends BatchCallTree
{

    /** The result of the call. */
    private Object		result;
    
    /** Loads the specified experimenter groups. */
    private BatchCall   loadCall;
    

    /**
     * Creates a {@link BatchCall} to load the ratings related to the object
     * identified by the class and the id.
     * 
     * @param annotationType 	The type of the object.
     * @param nodeType			The type of object to filter.
     * @param ids		The collection of id of the object.
     * @param userID	The id of the user who tagged the object or 
     * 					<code>-1</code> if the user is not specified.
     * @return The {@link BatchCall}.
     */
    private BatchCall filterBy(final Class annotationType, final Class nodeType, 
    						final Set<Long> ids, final List<String> terms, 
    						final long userID)
    {
        return new BatchCall("Loading Ratings") {
            public void doCall() throws Exception
            {
            	OmeroMetadataService os = context.getMetadataService();
                result = os.filterByAnnotation(nodeType, ids, annotationType, 
                					terms, userID);
            }
        };
    }
    
    /**
     * Adds the {@link #loadCall} to the computation tree.
     * @see BatchCallTree#buildTree()
     */
    protected void buildTree() { add(loadCall); }

    /**
     * Returns, in a <code>Set</code>, the root nodes of the found trees.
     * @see BatchCallTree#getResult()
     */
    protected Object getResult() { return result; }
    
    /**
     * Creates a new instance. Builds the call corresponding to the passed
     * index, throws an {@link IllegalArgumentException} if the index is not
     * supported.
     * 
     * @param annotationType	The type of annotations to fetch.
     * @param nodeType			The type of objects to filter.	
     * @param nodeIds			The collection of object ids.
     * @param userID			The id of the user or <code>-1</code> if the id 
     * 							is not specified.
     */
    public DataFilter(Class annotationType, Class nodeType, Set<Long> nodeIds,
    				List<String> terms, long userID)
    {
    	loadCall = filterBy(annotationType, nodeType, nodeIds, terms, userID);
    }
    
}
