/*
 * 05/26/2012
 *
 * ParameterizedCompletionInsertionInfo.java - Used internally to track the
 * changes required for a specific parameterized completion.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE.md file for details.
 */
package com.dr10.autocomplete;

import org.fife.ui.rsyntaxtextarea.DocumentRange;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.Position;


/**
 * Describes a parameterized completion - what's being inserted, where the
 * parameters are in the inserted text, etc.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class ParameterizedCompletionInsertionInfo {

	private int minOffs;
	private Position maxOffs;
	private int defaultEnd;
	private int selStart;
	private int selEnd;
	private String textToInsert;
	private List<DocumentRange> replacementLocations;
	private List<ReplacementCopy> replacementCopies;


	public ParameterizedCompletionInsertionInfo() {
		defaultEnd = -1;
	}


	/**
	 * Adds a copy of a replacement.
	 *
	 * @param id The ID of the replacement copy, e.g. the text itself.
	 * @param start The start offset.
	 * @param end The end offset.
	 */
	public void addReplacementCopy(String id, int start, int end) {
		if (replacementCopies==null) {
			replacementCopies = new ArrayList<>(1);
		}
		replacementCopies.add(new ReplacementCopy(id, start, end));
	}


	/**
	 * Marks a region of the replacement text as representing a variable name
	 * or some other construct that the user should replace.
	 *
	 * @param start The start offset.
	 * @param end The end offset.
	 * @see #getReplacementCount()
	 * @see #getReplacementLocation(int)
	 */
	public void addReplacementLocation(int start, int end) {
		if (replacementLocations==null) {
			replacementLocations = new ArrayList<>(1);
		}
		replacementLocations.add(new DocumentRange(start, end));
	}


	public int getDefaultEndOffs() {
		return defaultEnd>-1 ? defaultEnd : getMaxOffset().getOffset();
	}


	/**
	 * Returns the maximum offset the caret can move to before being outside
	 * the text inserted for this completion.
	 *
	 * @return The maximum offset.
	 * @see #getMinOffset()
	 */
	public Position getMaxOffset() {
		return maxOffs;
	}


	/**
	 * Returns the minimum offset the caret can move to before being outside
	 * the text inserted for this completion.
	 *
	 * @return The minimum offset.
	 * @see #getMaxOffset()
	 */
	public int getMinOffset() {
		return minOffs;
	}


	public int getReplacementCopyCount() {
		return replacementCopies==null ? 0 : replacementCopies.size();
	}


	/**
	 * Returns the number of replacements in the completion.
	 *
	 * @return The number of replacements in the completion.
	 */
	public int getReplacementCount() {
		return replacementLocations==null ? 0 : replacementLocations.size();
	}


	/**
	 * Returns the specified replacement copy.
	 *
	 * @param index The index of the replacement to retrieve.
	 * @return The replacement.
	 * @see #getReplacementCopyCount()
	 * @see #addReplacementCopy(String, int, int)
	 */
	public ReplacementCopy getReplacementCopy(int index) {
		return replacementCopies.get(index);
	}


	/**
	 * Returns the starting- and ending-offsets of the replacement regions
	 * in the completion.
	 *
	 * @param index The replacement region.
	 * @return The range in the document of that replacement region.
	 * @see #getReplacementCount()
	 */
	public DocumentRange getReplacementLocation(int index) {
		return replacementLocations.get(index);
	}


	/**
	 * Returns the offset that should be the end of the initially selected
	 * text when the completion is inserted (i.e., the end offset of the first
	 * replacement region).
	 *
	 * @return The end offset for the initial selection.
	 * @see #getSelectionStart()
	 */
	public int getSelectionEnd() {
		return selEnd;
	}


	/**
	 * Returns the offset that should be the start of the initially selected
	 * text when the completion is inserted (i.e., the start offset of the
	 * first replacement region).
	 *
	 * @return The start offset for the initial selection.
	 * @see #getSelectionEnd()
	 */
	public int getSelectionStart() {
		return selStart;
	}


	/**
	 * Returns the actual text to insert when the completion is selected.
	 *
	 * @return The text to insert.
	 * @see #setTextToInsert(String)
	 */
	public String getTextToInsert() {
		return textToInsert;
	}


	/**
	 * Returns whether there is an initial selected region for the
	 * completion (i.e., whether the completion actually has any parameters).
	 *
	 * @return Whether there is a region to initially select for the completion.
	 */
	public boolean hasSelection() {
		return selEnd!=selStart;
	}


	/**
	 * Sets the initially selected region for the completion.
	 *
	 * @param selStart The selection start.
	 * @param selEnd The selection end.
	 * @see #getSelectionEnd()
	 * @see #getSelectionStart()
	 */
	public void setInitialSelection(int selStart, int selEnd) {
		this.selStart = selStart;
		this.selEnd = selEnd;
	}


	/**
	 * Sets the document range the caret can move around in before being
	 * outside the text inserted for the completion.
	 *
	 * @param minOffs The minimum offset.
	 * @param maxOffs The maximum offset, that will track its location as the
	 *        document is modified.
	 * @see #getMinOffset()
	 * @see #getMaxOffset()
	 */
	public void setCaretRange(int minOffs, Position maxOffs) {
		this.minOffs = minOffs;
		this.maxOffs = maxOffs;
	}


	public void setDefaultEndOffs(int end) {
		defaultEnd = end;
	}


	/**
	 * Sets the text to insert for the completion.
	 *
	 * @param text The text to insert.
	 * @see #getTextToInsert()
	 */
	public void setTextToInsert(String text) {
		this.textToInsert = text;
	}


	/**
	 * Information about a replacement.
	 */
	public static class ReplacementCopy {

		private String id;
		private int start;
		private int end;

		ReplacementCopy(String id, int start, int end) {
			this.id = id;
			this.start = start;
			this.end = end;
		}

		public int getEnd() {
			return end;
		}

		public String getId() {
			return id;
		}

		public int getStart() {
			return start;
		}

	}


}
