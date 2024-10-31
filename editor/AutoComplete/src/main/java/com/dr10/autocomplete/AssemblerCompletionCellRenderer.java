package com.dr10.autocomplete;

import java.awt.Component;

import javax.swing.JList;

public class AssemblerCompletionCellRenderer extends CompletionCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean hasFocus) {
        return new AssemblerSuggestionItem(value.toString(), selected);
    }
}
