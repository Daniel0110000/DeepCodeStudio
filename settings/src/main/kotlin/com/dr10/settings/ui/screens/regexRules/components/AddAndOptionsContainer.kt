package com.dr10.settings.ui.screens.regexRules.components

import com.dr10.common.models.RegexRuleModel
import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.settings.ui.components.IconButton
import com.dr10.settings.ui.components.TextField
import com.dr10.settings.ui.viewModels.RegexRulesViewModel
import javax.swing.BorderFactory
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.border.EmptyBorder

class AddAndOptionsContainer(
    private val regexRulesViewModel: RegexRulesViewModel,
    private val state: FlowStateHandler.StateWrapper<RegexRulesViewModel.RegexRulesState>,
    private val onExecuteRegex: (String) -> Unit
): JPanel() {

    private val addAndOptionsContainerLayout = GroupLayout(this)

    private lateinit var patternTitle: JLabel
    private lateinit var patternTextField: TextField
    private lateinit var regexNameTitle: JLabel
    private lateinit var regexNameTextField: TextField

    private lateinit var addButton: IconButton
    private lateinit var executeRegexButton: IconButton

    private lateinit var regexRulesTitle: JLabel
    private lateinit var regexRules: RegexRulesList<RegexRuleModel>
    private lateinit var regexRulesScrollPanel: JScrollPane

    init {
        layout = addAndOptionsContainerLayout
        background = ThemeApp.awtColors.primaryColor
        border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeApp.awtColors.hoverColor),
            EmptyBorder(0, 5, 8, 5)
        )

        initializeComponents()
        setComponentsStructure()

        setState(state, RegexRulesViewModel.RegexRulesState::clearFields) { clear ->
            if (clear) {
                regexNameTextField.setText("")
                patternTextField.setText("")
                regexRulesViewModel.clearFields(false)
            }
        }
    }

    private fun initializeComponents() {
        patternTitle = createTitle("Regex")
        patternTextField = TextField()

        regexNameTitle = createTitle("Regex Name")
        regexNameTextField = TextField()

        addButton = IconButton(AppIcons.addIcon) { regexRulesViewModel.addRegexRule(
            name = regexNameTextField.getText(),
            pattern = patternTextField.getText()
        ) }
        executeRegexButton = IconButton(AppIcons.executeIcon) { onExecuteRegex(patternTextField.getText()) }

        regexRulesTitle = createTitle("Regex Rules")
        regexRules = RegexRulesList<RegexRuleModel>(
            onClickListener = { onExecuteRegex(it.regexPattern) }
        ).apply {
            setItemUI(RegexRuleItem::class.java)
            itemListener = object: ItemListener<RegexRuleModel> {
                override fun onItemDeleted(item: RegexRuleModel) {
                    regexRulesViewModel.deleteRegexRule(item) }
            }
            setState(state, RegexRulesViewModel.RegexRulesState::regexRules) { rules ->
                setListData(rules)
            }
        }

        regexRulesScrollPanel = JScrollPane(regexRules).apply {
            verticalScrollBar.unitIncrement = 25
            viewport.background = ThemeApp.awtColors.primaryColor
            border = EmptyBorder(0, 0, 0, 0)
            verticalScrollBar.setUI(CustomScrollBar())
            horizontalScrollBar.setUI(CustomScrollBar())
        }
    }

    private fun setComponentsStructure() {
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