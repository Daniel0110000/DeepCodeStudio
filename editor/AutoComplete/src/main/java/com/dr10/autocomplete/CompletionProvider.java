/*
 * 12/21/2008
 *
 * CompletionProvider.java - Provides autocompletion values based on the
 * text currently in a text component.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE.md file for details.
 */
package com.dr10.autocomplete;

import java.awt.Point;
import java.util.List;

import javax.swing.ListCellRenderer;
import javax.swing.text.JTextComponent;


/**
 * Provides autocompletion values to an {@link AutoCompletion}.<p>
 *
 * Completion providers can have an optional parent.  Parents are searched for
 * completions when their children are.  This allows for chaining of completion
 * providers.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface CompletionProvider {


	/**
	 * Clears the values used to identify and insert "parameterized completions"
	 * (e.g. functions or methods).  After this method is called, functions and
	 * methods will not have their parameters auto-completed.
	 *
	 * @see #setParameterizedCompletionParams(char, String, char)
	 */
	void clearParameterizedCompletionParams();


	/**
	 * Returns the text just before the current caret position that could be
	 * the start of something auto-completable.
	 *
	 * @param comp The text component.
	 * @return The text.  A return value of <code>null</code> means nothing
	 *         should be auto-completed; a value of an empty string
	 *         (<code>""</code>) means auto-completion should still be
	 *         considered (i.e., all possible choices are valid).
	 */
	String getAlreadyEnteredText(JTextComponent comp);


	/**
	 * Gets the possible completions for the text component at the current
	 * caret position.
	 *
	 * @param comp The text component.
	 * @return The list of {@link Completion}s.  If no completions are
	 *         available, this method should return an empty list.
	 */
	List<Completion> getCompletions(JTextComponent comp);


	/**
	 * Returns the completions that have been entered at the specified visual
	 * location.  This can be used for tool tips when the user hovers the
	 * mouse over completed text.
	 *
	 * @param comp The text component.
	 * @param p The position, usually from a {@code MouseEvent}.
	 * @return The completions, or an empty list if there are none.
	 */
	List<Completion> getCompletionsAt(JTextComponent comp, Point p);


	/**
	 * Returns the cell renderer for completions returned from this provider.
	 *
	 * @return The cell renderer, or <code>null</code> if the default should
	 *         be used.
	 * @see #setListCellRenderer(ListCellRenderer)
	 */
	ListCellRenderer<Object> getListCellRenderer();


	/**
	 * Returns an object that can return a list of completion choices for
	 * parameters.  This is used when a user code-completes a parameterized
	 * completion, such as a function or method.  For any parameter to the
	 * function/method, this object can return possible completions.
	 *
	 * @return The parameter choices provider, or <code>null</code> if
	 *         none is installed.
	 */
	ParameterChoicesProvider getParameterChoicesProvider();


	/**
	 * Returns a list of parameterized completions that have been entered
	 * at the current caret position of a text component (and thus can have
	 * their completion choices displayed).
	 *
	 * @param tc The text component.
	 * @return The list of {@link ParameterizedCompletion}s.  If no completions
	 *         are available, this may be <code>null</code>.
	 */
	List<ParameterizedCompletion> getParameterizedCompletions(JTextComponent tc);


	/**
	 * Returns the text that marks the end of a list of parameters to a
	 * function or method.
	 *
	 * @return The text for a parameter list end, for example,
	 *         '<code>)</code>', or {@code 0} if none.
	 * @see #getParameterListStart()
	 * @see #getParameterListSeparator()
	 * @see #setParameterizedCompletionParams(char, String, char)
	 */
	char getParameterListEnd();


	/**
	 * Returns the text that separates parameters to a function or method.
	 *
	 * @return The text that separates parameters, for example,
	 *         "<code>, </code>".
	 * @see #getParameterListStart()
	 * @see #getParameterListEnd()
	 * @see #setParameterizedCompletionParams(char, String, char)
	 */
	String getParameterListSeparator();


	/**
	 * Returns the text that marks the start of a list of parameters to a
	 * function or method.
	 *
	 * @return The text for a parameter list start, for example,
	 *         "<code>(</code>".
	 * @see #getParameterListEnd()
	 * @see #getParameterListSeparator()
	 * @see #setParameterizedCompletionParams(char, String, char)
	 */
	char getParameterListStart();


	/**
	 * Returns the parent completion provider.
	 *
	 * @return The parent completion provider.
	 * @see #setParent(CompletionProvider)
	 */
	CompletionProvider getParent();


	/**
	 * This method is called if auto-activation is enabled in the parent
	 * {@link AutoCompletion} after the user types a single character.  This
	 * provider should check the text at the current caret position of the
	 * text component, and decide whether auto-activation would be appropriate
	 * here.  For example, a <code>CompletionProvider</code> for Java might
	 * want to return <code>true</code> for this method only if the last
	 * character typed was a '<code>.</code>'.
	 *
	 * @param tc The text component.
	 * @return Whether auto-activation would be appropriate.
	 */
	boolean isAutoActivateOkay(JTextComponent tc);


	/**
	 * Sets the renderer to use when displaying completion choices.
	 *
	 * @param r The renderer to use.
	 * @see #getListCellRenderer()
	 */
	void setListCellRenderer(ListCellRenderer<Object> r);


	/**
	 * Sets the values used to identify and insert "parameterized completions"
	 * (e.g. functions or methods).  If this method isn't called, functions
	 * and methods will not have their parameters auto-completed.
	 *
	 * @param listStart The character that marks the beginning of a list of
	 *        parameters, such as '{@code (}' in C or Java.
	 * @param separator Text that should separate parameters in a parameter
	 *        list when one is inserted.  For example, "{@code , }".
	 * @param listEnd The character that marks the end of a list of parameters,
	 *        such as '{@code )}' in C or Java.
	 * @throws IllegalArgumentException If either {@code listStart} or
	 *         {@code listEnd} is not printable ASCII, or if
	 *         {@code separator} is <code>null</code> or an empty string.
	 * @see #clearParameterizedCompletionParams()
	 */
	void setParameterizedCompletionParams(char listStart,
										String separator, char listEnd);


	/**
	 * Sets the parent completion provider.
	 *
	 * @param parent The parent provider.  <code>null</code> means there will
	 *        be no parent provider.
	 * @see #getParent()
	 */
	void setParent(CompletionProvider parent);


}
