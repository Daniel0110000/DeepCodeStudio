/*
 * 12/11/2010
 *
 * ParameterizedCompletion.java - A completion option.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE.md file for details.
 */
package com.dr10.autocomplete;

import javax.swing.text.JTextComponent;


/**
 * A completion option that takes parameters, such as a function or method.<p>
 *
 * In order to use any instance of a {@code ParameterizedCompletion}, you need
 * to call {@link AutoCompletion#setParameterAssistanceEnabled(boolean)} with
 * a value of {@code true} first.  Otherwise, any completion choices will be
 * visible in the completion list, but won't insert anything when selected.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface ParameterizedCompletion extends Completion {


	/**
	 * Returns the "definition string" for this completion.  For example,
	 * for the C "<code>printf</code>" function, this would return
	 * "<code>int printf(const char *, ...)</code>".
	 *
	 * @return The definition string.
	 */
	String getDefinitionString();


	/**
	 * Returns the specified {@link Parameter}.
	 *
	 * @param index The index of the parameter to retrieve.
	 * @return The parameter.
	 * @see #getParamCount()
	 */
	Parameter getParam(int index);


	/**
	 * Returns the number of parameters this completion takes.
	 *
	 * @return The number of parameters this completion takes.
	 * @see #getParam(int)
	 */
	int getParamCount();


	/**
	 * Returns completion information for this parameterized completion,
	 * given the specified text component.
	 *
	 * @param tc The text component.
	 * @param replaceTabsWithSpaces Whether to replace tabs with spaces.
	 * @return The completion info.
	 */
	ParameterizedCompletionInsertionInfo getInsertionInfo(
			JTextComponent tc, boolean replaceTabsWithSpaces);


	/**
	 * Returns whether a tool tip displaying assistance for each parameter
	 * while it is being edited is appropriate for this completion.
	 *
	 * @return Whether the tool tip is appropriate to display.
	 */
	boolean getShowParameterToolTip();


	/**
	 * A parameter passed to a parameterized {@link Completion}.
	 */
	class Parameter {

		private String name;
		private Object type;
		private String desc;
		private boolean isEndParam;

		/**
		 * Constructor.
		 *
		 * @param type The type of this parameter.  This may be
		 *        <code>null</code> for languages without specific types,
		 *        dynamic typing, etc.  Usually you'll pass a String for this
		 *        value, but you may pass any object representing a type in
		 *        your language, as long as its <code>toString()</code> method
		 *        returns a string representation of the type.
		 * @param name The name of the parameter.
		 */
		public Parameter(Object type, String name) {
			this(type, name, false);
		}

		/**
		 * Constructor.
		 *
		 * @param type The type of this parameter.  This may be
		 *        <code>null</code> for languages without specific types,
		 *        dynamic typing, etc.  Usually you'll pass a String for this
		 *        value, but you may pass any object representing a type in
		 *        your language, as long as its <code>toString()</code> method
		 *        returns a string representation of the type.
		 * @param name The name of the parameter.
		 * @param endParam Whether this parameter is an "ending parameter;"
		 *        that is, whether this parameter is at a logical "ending
		 *        point" in the completion text.  If the user types in a
		 *        parameter that is an ending point, parameter completion mode
		 *        terminates.  Set this to <code>true</code> for a trailing
		 *        parameter after a function call's closing ')', for example.
		 */
		public Parameter(Object type, String name, boolean endParam) {
			this.name = name;
			this.type = type;
			this.isEndParam = endParam;
		}

		public String getDescription() {
			return desc;
		}

		public String getName() {
			return name;
		}

		/**
		 * Returns the type of this parameter, as a string.
		 *
		 * @return The type of the parameter, or <code>null</code> for none.
		 */
		public String getType() {
			return type==null ? null : type.toString();
		}

		/**
		 * Returns the object used to describe the type of this parameter.
		 *
		 * @return The type object, or <code>null</code> for none.
		 */
		public Object getTypeObject() {
			return type;
		}

		/**
		 * @return Whether this parameter is an "ending parameter;"
		 *         that is, whether this parameter is at a logical "ending
		 *         point" in the completion text.  If the user types in a
		 *         parameter that is an ending point, parameter completion mode
		 *         terminates.
		 */
		public boolean isEndParam() {
			return isEndParam;
		}

		public void setDescription(String desc) {
			this.desc = desc;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			if (getType()!=null) {
				sb.append(getType());
			}
			if (getName()!=null) {
				if (getType()!=null) {
					sb.append(' ');
				}
				sb.append(getName());
			}
			return sb.toString();
		}

	}


}
