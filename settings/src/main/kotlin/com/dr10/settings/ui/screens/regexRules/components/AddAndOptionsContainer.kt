package com.dr10.settings.ui.screens.regexRules.components

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import com.dr10.settings.ui.components.IconButton
import com.dr10.settings.ui.components.TextField
import com.dr10.settings.ui.screens.regexRules.RegexRuleModel
import javax.swing.BorderFactory
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.border.EmptyBorder

class AddAndOptionsContainer(
    private val onExecuteRegex: (String) -> Unit
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        val addAndOptionsContainerLayout = GroupLayout(this)
        layout = addAndOptionsContainerLayout
        background = ThemeApp.awtColors.primaryColor
        border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeApp.awtColors.hoverColor),
            EmptyBorder(0, 5, 8, 5)
        )

        val patternTitle = createTitle("Regex")
        val patternTextField = TextField()

        val regexNameTitle = createTitle("Regex Name")
        val regexNameTextField = TextField()
        val addButton = IconButton(AppIcons.addIcon) {}
        val executeRegexButton = IconButton(AppIcons.executeIcon) { onExecuteRegex(patternTextField.getText()) }

        val regexRulesTitle = createTitle("Regex Rules")
        val regexRules: JList<RegexRuleModel> = JList<RegexRuleModel>().apply {
            background = ThemeApp.awtColors.primaryColor
            setCellRenderer(RegexRulesCellRender())
            setListData(emptyArray())
        }

        val regexRulesScrollPanel = JScrollPane(regexRules).apply {
            border = EmptyBorder(0, 0, 0, 0)
            verticalScrollBar.setUI(CustomScrollBar())
            horizontalScrollBar.setUI(CustomScrollBar())
        }

        addAndOptionsContainerLayout.setHorizontalGroup(
            addAndOptionsContainerLayout.createParallelGroup()
                .addComponent(regexNameTitle)
                .addGroup(
                    addAndOptionsContainerLayout.createSequentialGroup()
                        .addComponent(regexNameTextField, 0, 0, Short.MAX_VALUE.toInt())
                        .addGap(8)
                        .addComponent(addButton, 0, 0, 35)
                        .addGap(8)
                        .addComponent(executeRegexButton, 0, 0, 35)
                )
                .addComponent(patternTitle)
                .addComponent(patternTextField, 0, 0, Short.MAX_VALUE.toInt())
                .addComponent(regexRulesTitle)
                .addComponent(regexRulesScrollPanel, 0, 0, Short.MAX_VALUE.toInt())
        )

        addAndOptionsContainerLayout.setVerticalGroup(
            addAndOptionsContainerLayout.createSequentialGroup()
                .addGap(10)
                .addComponent(regexNameTitle)
                .addGap(2)
                .addGroup(
                    addAndOptionsContainerLayout.createParallelGroup()
                        .addComponent(regexNameTextField, 0, 0, 35)
                        .addComponent(addButton, 0, 0, 35)
                        .addComponent(executeRegexButton, 0, 0, 35)
                )
                .addGap(8)
                .addComponent(patternTitle)
                .addGap(2)
                .addComponent(patternTextField, 0, 0, 35)
                .addGap(12)
                .addComponent(regexRulesTitle)
                .addGap(2)
                .addComponent(regexRulesScrollPanel, 0, 0, Short.MAX_VALUE.toInt())
                .addGap(10)
        )
    }

    private fun createTitle(title: String): JLabel = JLabel(title).apply {
        font = ThemeApp.text.fontInterRegular(13f)
        foreground = ThemeApp.awtColors.textColor
    }
}