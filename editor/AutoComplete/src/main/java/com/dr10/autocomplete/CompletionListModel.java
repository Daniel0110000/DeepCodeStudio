/*
 * 12/22/2008
 *
 * CompletionListModel.java - A model that allows bulk addition of elements.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE.md file for details.
 */
package com.dr10.autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;


/**
 * A list model implementation that allows the bulk addition of elements.
 * This is the only feature missing from <code>DefaultListModel</code> that
 * we need.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class CompletionListModel extends AbstractListModel<Completion> {

	/**
	 * Container for items in this model.
	 */
	private final List<Completion> delegate;


	/**
	 * Constructor.
	 */
	CompletionListModel() {
		delegate = new ArrayList<>();
	}


	/**
	 * Removes all the elements from this list.  The list will
	 * be empty after this call returns (unless it throws an exception).
	 *
	 * @see #setContents(Collection)
	 */
	public void clear() {
		int end = delegate.size()-1;
		delegate.clear();
		if (end >= 0) {
			fireIntervalRemoved(this, 0, end);
		}
	}


	@Override
	public Completion getElementAt(int index) {
		return delegate.get(index);
	}


	@Override
	public int getSize() {
		return delegate.size();
	}


	/**
	 * Sets the contents of this model.  All previous contents are removed.
	 *
	 * @param contents The new contents of this model.
	 */
	public void setContents(Collection<Completion> contents) {
		clear();
		int count = contents.size();
		if (count>0) {
			delegate.addAll(contents);
			fireIntervalAdded(this, 0, count-1); // endpoints included (!)
		}
	}


}
