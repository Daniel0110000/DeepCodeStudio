/*
 * 12/21/2008
 *
 * ParameterizedCompletionDescriptionToolTip.java - A "tool tip" displaying
 * information on the function or method currently being entered.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE.md file for details.
 */
package com.dr10.autocomplete;

import org.fife.ui.rsyntaxtextarea.HtmlUtil;
import org.fife.ui.rsyntaxtextarea.PopupWindowDecorator;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;


/**
 * A "tool tip" that displays information on the function or method currently
 * being entered.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class ParameterizedCompletionDescriptionToolTip {

	/**
	 * The backing AutoCompletion.
	 */
	private AutoCompletion ac;

	/**
	 * The actual tool tip.
	 */
	private JWindow tooltip;

	/**
	 * The label that holds the description.
	 */
	private JLabel descLabel;

	/**
	 * The completion being described.
	 */
	private ParameterizedCompletion pc;

	private boolean overflow;

	/**
	 * Constructor.
	 *
	 * @param owner The parent window.
	 * @param ac The parent auto-completion.
	 * @param pc The completion being described.
	 */
	ParameterizedCompletionDescriptionToolTip(Window owner,
			ParameterizedCompletionContext context,
			AutoCompletion ac, ParameterizedCompletion pc) {

		tooltip = new JWindow(owner);

		this.ac = ac;
		this.pc = pc;

		descLabel = new JLabel();
		descLabel.setBorder(BorderFactory.createCompoundBorder(
					TipUtil.getToolTipBorder(),
					BorderFactory.createEmptyBorder(2, 5, 2, 5)));
		descLabel.setOpaque(true);
		descLabel.setBackground(TipUtil.getToolTipBackground());
		// It appears that if a JLabel is set as a content pane directly, when
		// using the JDK's opacity APIs, it won't paint its background, even
		// if label.setOpaque(true) is called.  You have to have a container
		// underneath it for it to paint its background.  Thus, we embed our
		// label in a parent JPanel to handle this case.
		//tooltip.setContentPane(descLabel);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(descLabel);
		tooltip.setContentPane(panel);

		// Give apps a chance to decorate us with drop shadows, etc.
		PopupWindowDecorator decorator = PopupWindowDecorator.get();
		if (decorator!=null) {
			decorator.decorate(tooltip);
		}

		updateText(0);

		tooltip.setFocusableWindowState(false);

	}


	/**
	 * Returns whether this tool tip is visible.
	 *
	 * @return Whether this tool tip is visible.
	 * @see #setVisible(boolean)
	 */
	public boolean isVisible() {
		return tooltip.isVisible();
	}


	/**
	 * Sets the location of this tool tip relative to the given rectangle.
	 *
	 * @param r The visual position of the caret (in screen coordinates).
	 */
	public void setLocationRelativeTo(Rectangle r) {

		// Multi-monitor support - make sure the completion window (and
		// description window, if applicable) both fit in the same window in
		// a multi-monitor environment.  To do this, we decide which monitor
		// the rectangle "r" is in, and use that one (just pick top-left corner
		// as the defining point).
		Rectangle screenBounds = Util.getScreenBoundsForPoint(r.x, r.y);
		//Dimension screenSize = tooltip.getToolkit().getScreenSize();

		// Try putting our stuff "above" the caret first.
		int y = r.y - 5 - tooltip.getHeight();
		if (y<0) {
			y = r.y + r.height + 5;
		}

		// Get x-coordinate of completions.  Try to align left edge with the
		// caret first.
		int x = r.x;
		if (x<screenBounds.x) {
			x = screenBounds.x;
		}
		else if (x+tooltip.getWidth()>screenBounds.x+screenBounds.width) { // completions don't fit
			x = screenBounds.x + screenBounds.width - tooltip.getWidth();
		}

		tooltip.setLocation(x, y);
		EventQueue.invokeLater(tooltip::pack);
	}


	/**
	 * Toggles the visibility of this tool tip.
	 *
	 * @param visible Whether this tool tip should be visible.
	 * @see #isVisible()
	 */
	public void setVisible(boolean visible) {
		tooltip.setVisible(visible);
	}


	/**
	 * Updates the text in the tool tip to have the current parameter
	 * displayed in bold.
	 *
	 * @param selectedParam The index of the selected parameter.
	 * @return Whether the text needed to be updated.
	 */
	public boolean updateText(int selectedParam) {

		StringBuilder sb = new StringBuilder("<html>");
		int paramCount = pc.getParamCount();

		if (overflow) {
			if (selectedParam < paramCount) { // Not end-of-function parameter
				String temp = pc.getParam(Math.min(paramCount - 1, selectedParam)).toString();
				sb.append("...<b>")
					.append(HtmlUtil.escapeForHtml(temp, "<br>", false))
					.append("</b>...");
				// Hacky calls to hide tool tip if "trailing" parameter is focused, and we are displaying only
				// one argument at a time, then re-show it if they tab back into a parameter.  Otherwise, we
				// end up showing an empty tool tip here
				if (!isVisible()) {
					setVisible(true);
				}
			}
			else {
				setVisible(false);
			}
		}
		else {

			for (int i=0; i<paramCount; i++) {

				if (i==selectedParam) {
					sb.append("<b>");
				}

				// Some parameter types may have chars in them unfriendly to HTML
				// (such as type parameters in Java).  We need to take care to
				// escape these.
				String temp = pc.getParam(i).toString();
				sb.append(HtmlUtil.escapeForHtml(temp, "<br>", false));

				if (i==selectedParam) {
					sb.append("</b>");
				}
				if (i<paramCount-1) {
					sb.append(pc.getProvider().getParameterListSeparator());
				}

			}
		}

		if (selectedParam>=0 && selectedParam<paramCount) {
			ParameterizedCompletion.Parameter param =
							pc.getParam(selectedParam);
			String desc = param.getDescription();
			if (desc!=null) {
				sb.append("<br>");
				sb.append(desc);
			}
		}

		descLabel.setText(sb.toString());
		if (!overflow && sb.length() > ac.getParameterDescriptionTruncateThreshold()) {
			overflow = true;
			updateText(selectedParam);
		}
		else {
			overflow = false;
			tooltip.pack();
		}

		return true;

	}


	/**
	 * Updates the {@code LookAndFeel} of this window and the description
	 * window.
	 */
	public void updateUI() {
		SwingUtilities.updateComponentTreeUI(tooltip);
	}


}
