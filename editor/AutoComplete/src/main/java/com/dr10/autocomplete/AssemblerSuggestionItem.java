package com.dr10.autocomplete;

import com.dr10.common.ui.AppIcons;
import com.dr10.common.ui.ThemeApp;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AssemblerSuggestionItem extends JPanel {

    private final String suggestion;
    private final Boolean isSelected;

    public AssemblerSuggestionItem(String suggestion, Boolean isSelected) {
        this.suggestion = suggestion;
        this.isSelected = isSelected;

        onCreate();
    }

    private void onCreate() {
        GroupLayout suggestionItemLayout = new GroupLayout(this);
        setLayout(suggestionItemLayout);

        setBackground(
                isSelected ? new ThemeApp.AwtColors().getPrimaryColor()
                        : new ThemeApp.AwtColors().getSecondaryColor()
        );

        JLabel suggestionIcon = new JLabel(
                isSelected ? AppIcons.INSTANCE.getSuggestionWordSelectedIcon()
                        : AppIcons.INSTANCE.getSuggestionWordIcon()
        );

        JLabel suggestionLabel = new JLabel(suggestion);
        suggestionLabel.setFont(new ThemeApp.Text().fontInterRegular(13f));
        suggestionLabel.setForeground(new ThemeApp.AwtColors().getTextColor());

        suggestionItemLayout.setHorizontalGroup(
                suggestionItemLayout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(suggestionIcon)
                        .addGap(5)
                        .addComponent(suggestionLabel)
        );

        suggestionItemLayout.setVerticalGroup(
                suggestionItemLayout.createSequentialGroup()
                        .addGap(3)
                        .addGroup(
                                suggestionItemLayout.createParallelGroup()
                                        .addComponent(suggestionIcon)
                                        .addComponent(suggestionLabel)
                        )
                        .addGap(3)
        );

    }

}
