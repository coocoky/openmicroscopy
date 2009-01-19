/*
 * org.openmicroscopy.shoola.agents.editor.model.Field
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
 *	author Will Moore will@lifesci.dundee.ac.uk
 */

package org.openmicroscopy.shoola.agents.editor.model;

// Java imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.TableModel;

//Third-party libraries

//Application-internal dependencies

import org.openmicroscopy.shoola.agents.editor.model.params.BooleanParam;
import org.openmicroscopy.shoola.agents.editor.model.params.EnumParam;
import org.openmicroscopy.shoola.agents.editor.model.params.IParam;
import org.openmicroscopy.shoola.agents.editor.model.params.NumberParam;
import org.openmicroscopy.shoola.agents.editor.model.params.TextParam;
import org.openmicroscopy.shoola.agents.editor.model.tables.TableModelFactory;

/**
 * This is the data object that occupies a node of the tree hierarchy. 
 * It has name, description, url etc, stored in an AttributeMap, and may
 * have 0, 1 or more Parameter objects {@link IParam} to store 
 * experimental variables, or parameters. 
 * 
 * @author  William Moore &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:will@lifesci.dundee.ac.uk">will@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME3.0
 */
public class Field 
	implements IField,
	Cloneable {
	
	/**
	 * A property of this field. The attribute for an (optional) Name.
	 */
	public static final String 			FIELD_NAME = "fieldName";
	
	/**
	 * A property of this field. 
	 * Stores a color as a string in the form "r:g:b";
	 */
	public static final String 			BACKGROUND_COLOUR = "backgroundColour";
	
	/**  The name of the attribute used to store a step type, 
	 * Possible values are SINGLE_STEP, STEP_GROUP, SPLIT_STEP.
	 */
	public static final String 			STEP_TYPE = "step-type";
	
	/**
	 * A display property of this field.
	 * getDisplayAttribute(TOOL_TIP_TEXT) should return a string composed
	 * of field description and parameter values etc. 
	 */
	public static final String 			TOOL_TIP_TEXT = "toolTipText";
	
	/**
	 * The list of Parameters, representing experimental variables for this 
	 * field.
	 */
	private List<IFieldContent> 		fieldParams;

	/**
	 * A map of the template attributes for this Field. 
	 * eg Name, Description etc. 
	 */
	private HashMap<String, String> 	templateAttributesMap;
	
	/**
	 * If the parameters of this field have multiple values, they can be 
	 * represented as a {@link TableModel} here. 
	 */
	private TableModel 					tableData;
	
	/**
	 * A list of notes that can be added to a Field/Step when a protocol is
	 * performed, to become an experiment. 
	 */
	private	List<Note>					stepNotes;
	
	/**
	 * Default constructor.
	 */
	public Field() 
	{
		templateAttributesMap = new HashMap<String, String>();
		fieldParams = new ArrayList<IFieldContent>();
		stepNotes = new ArrayList<Note>();
	}
	
	/**
	 * Returns a copy of this object.
	 * This is implemented manually, rather than calling super.clone()
	 * Therefore, any subclasses should also manually override this method to 
	 * copy any additional attributes they have.  
	 */
	public IField clone() 
	{
		Field newField = new Field();
		
		// duplicate attributes
		HashMap<String,String> newAtt = new HashMap<String,String>(getAllAttributes());
		newField.setAllAttributes(newAtt);
		
		IFieldContent newContent;
		// duplicate parameters 
		for (int i=0; i<getContentCount(); i++) {
			IFieldContent content = getContentAt(i);
			if (content instanceof IParam) {
				IParam param = (IParam)content;
				newContent = param.cloneParam();
				newField.addContent(newContent);
			} else if (content instanceof TextContent) {
				TextContent text = (TextContent)content;
				newContent = new TextContent(text);	// clone content
				newField.addContent(newContent);
			}
		}
		
		// if tableModelAdaptor exists for this field, add one to new field
		if (getTableData() != null) {
			TableModel tm = TableModelFactory.getFieldTable(newField);
			newField.setTableData(tm);
		}
		
		return newField;
	}
	
	/**
	 * A constructor used to set the name of the field.
	 * This constructor is called by the others, in order to initialise
	 * the attributesMap and parameters list. 
	 * 
	 * @param name		A name given to this field. 
	 */
	public Field(String name) 
	{	
		this();
		
		setAttribute(FIELD_NAME, name);
	}
	
	/**
	 * gets an attribute in the templateAttributesMap
	 * 
	 * Implemented as specified by the {@link IAttributes} interface
	 * 
	 * @see IAttributes#getAttribute(String)
	 */
	public String getAttribute(String name) 
	{
		return templateAttributesMap.get(name);
	}
	
	/**
	 * gets all attributes in the templateAttributesMap
	 */
	public Map getAllAttributes() {
		return templateAttributesMap;
	}
	
	/**
	 * sets the attribute map.
	 * 
	 * @param newAtt	The new attribute map
	 */
	public void setAllAttributes(HashMap<String,String> newAtt) {
		templateAttributesMap = newAtt;
	}
	
	/**
	 * sets an attribute in the attributesMap
	 * Implemented as specified by the {@link IAttributes} interface
	 * 
	 * @see IAttributes#setAttribute(String, String)
	 */
	public void setAttribute(String name, String value) {
		
		templateAttributesMap.put(name, value);
	}
	
	/**
	 * For display etc. Simply returns the name...
	 */
	public String toString() {
		String name = getAttribute(FIELD_NAME);
		
		return (name == null ? "Field Name" : name);
	}

	/**
	 * Implemented as specified by the {@link IField} interface.
	 * Convenience method for querying the attributes map for 
	 * boolean attributes.
	 */
	public boolean isAttributeTrue(String attributeName) {
		String value = getAttribute(attributeName);
		return DataFieldConstants.TRUE.equals(value);
	}
	

	
	/**
	 * Implemented as specified by the {@link IField} interface.
	 * This method tests to see whether the field has been filled out. 
	 * ie, Has the user entered a "valid" value into the Form. 
	 * This will return false if any of the parameters for this field are
	 * not filled. 
	 * 
	 * @return	True if the all the parameters have been filled out by user.  
	 */
	public boolean isFieldFilled() {
		
		for (IFieldContent content : fieldParams) {
			if (content instanceof IParam) {
				IParam param = (IParam)content;
				if (! param.isParamFilled()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Implemented as specified by the {@link IField} interface.
	 * Returns the number of IParam parameters for this field.
	 */
	public int getContentCount() {
		return fieldParams.size();
	}

	/**
	 * Implemented as specified by the {@link IField} interface.
	 * Returns the content of this field at the given index.
	 */
	public IFieldContent getContentAt(int index) {
		return fieldParams.get(index);
	}

	/**
	 * Implemented as specified by the {@link IField} interface.
	 * Adds a parameter to the list for this field
	 */
	public void addContent(IFieldContent param) {
		if (param != null)
			fieldParams.add(param);
	}
	
	/**
	 * Implemented as specified by the {@link IField} interface.
	 * Adds a parameter to the list for this field
	 */
	public void addContent(int index, IFieldContent param) 
	{
		if (param != null)
			fieldParams.add(index, param);
	}

	/**
	 * Implemented as specified by the {@link IField} interface.
	 * Removes the specified content from the list. 
	 */
	public int removeContent(IFieldContent param) 
	{
		int index = fieldParams.indexOf(param);
		
		fieldParams.remove(param);
		return index;
	}
	
	/**
	 * Implemented as specified by the {@link IField} interface.
	 * Removes the specified content from the list. 
	 */
	public void removeContent(int index) 
	{
		fieldParams.remove(index);
	}
	
	/**
	 * Implemented as specified by the {@link IField} interface.
	 * 
	 * @see IField#getAtomicParams()
	 */
	public List<IParam> getAtomicParams() 
	{
		List<IParam> params = new ArrayList<IParam>();
		
		// check Boolean, Enum, Text, Number
		for (IFieldContent content : fieldParams) {
			if ((content instanceof BooleanParam) ||
					(content instanceof EnumParam) ||
					(content instanceof TextParam) || 
					(content instanceof NumberParam)){
				params.add((IParam)content);
			}
		}
		
		return params;
	}
	
	/**
	 * Implemented as specified by the {@link IField} interface.
	 * @see {@link IField#addNote(Note)}
	 */
	public void addNote(Note note) { stepNotes.add(note); }
	
	/**
	 * Implemented as specified by the {@link IField} interface.
	 * @see {@link IField#getNoteCount()}
	 */
	public int getNoteCount() { return stepNotes.size(); }
	
	/**
	 * Implemented as specified by the {@link IField} interface.
	 * @see {@link IField#getNoteAt(int)}
	 */
	public Note getNoteAt(int index) { return stepNotes.get(index); }
	
	/**
	 * Returns a String containing the field description, plus the 
	 * tool-tip-text from it's parameters. 
	 * 
	 * @return		see above.
	 */
	public String getToolTipText() 
	{
		String toolTipText = "";
		
		String paramText;
		for (int i=0; i<getContentCount(); i++) {
			paramText = getContentAt(i).toString();
			if ((paramText != null) && (paramText.length() > 0))
			{
				if (toolTipText.length() > 0) 
					toolTipText = toolTipText + ", ";
				toolTipText = toolTipText + paramText;
			}
		}
		
		return toolTipText;
	}
	
	/**
	 * Returns an html representation of the field. 
	 * 
	 * @return
	 */
	public String getDescription() 
	{
		String description = "";
		String contentText;
		String contentString;
		IFieldContent content;
		
		// flag used to insert space between parameter objects, so user can
		// start typing and insert text between parameters. 
		boolean includeSpacer = false;	
		
		// html for the field contents. 
		for (int i=0; i<getContentCount(); i++) {
			content = getContentAt(i);
			if (content instanceof IParam) {
				contentString = content.toString();
				if (contentString.length() == 0)
					contentString = "param";
				
				contentString = "[" + contentString + "]";
				// id attribute allows parameters to be linked to model
				// eg for editing parameters. 
				contentText = (includeSpacer ? " " : "") + // space before param
				 		contentString;
				includeSpacer = true;
			} else {
				contentText = content.toString();
				if (content.toString().length() > 0)
					includeSpacer = false;		// don't need a space after text
			}
			// add each component. 
			description = description + contentText;
		}
		
		return description;
		
	}
	
	/**
	 * Implemented as specified by the {@link IField} interface. 
	 * Sets {@link #tableData}
	 * 
	 * @see IField#setTableData(TableModel)
	 */
	public void setTableData(TableModel tableModel)
	{
		tableData = tableModel;
	}

	/**
	 * Implemented as specified by the {@link IField} interface. 
	 * Returns {@link #tableData}
	 * 
	 * @see IField#getTableData()
	 */
	public TableModel getTableData() { return tableData; }
}
