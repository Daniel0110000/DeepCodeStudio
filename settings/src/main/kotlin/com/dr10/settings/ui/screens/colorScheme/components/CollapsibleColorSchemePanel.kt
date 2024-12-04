package com.dr10.settings.ui.screens.colorScheme.components

import com.dr10.common.models.ColorSchemeModel
import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils
import com.dr10.common.utilities.Constants
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.settings.ui.viewModels.ColorSchemesViewModel
import javax.swing.BoxLayout
import javax.swing.JPanel

class CollapsibleColorSchemePanel(
    private val colorSchemesViewModel: ColorSchemesViewModel,
    val model: ColorSchemeModel,
    private val colorSchemesState: FlowStateHandler. StateWrapper<ColorSchemesViewModel. ColorSchemesState>
): JPanel() {

    private val colorSchemeHeader = ColorSchemeHeader(colorSchemesViewModel, model)

    private val colorSchemes: JPanel by lazy {
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            background = ThemeApp.awtColors.primaryColor

            colorSchemeOptions().forEach { option ->
                add(ColorScheme(option) {
                    if (ColorUtils.stringToColor(option.color) != it) {
                        colorSchemesViewModel.updateColor(
                            option = option,
                            newColorValue = ColorUtils.colorToString(it),
                            uniqueId = model.uniqueId
                        )
                    }
                })
            }
        }
    }

    init { onCreate() }

    private fun onCreate() {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        background = ThemeApp.awtColors.primaryColor

        setState(colorSchemesState, ColorSchemesViewModel.ColorSchemesState::isExpandColorsList) { expandList ->
            updateExpandState(expandList)
        }

        setState(colorSchemesState, ColorSchemesViewModel.ColorSchemesState::selectedOptionIndex) { selectedIndex ->
            updateSelectedState(selectedIndex)
        }

        add(colorSchemeHeader)
        add(colorSchemes)

    }

    private fun updateExpandState(expandList: List<Boolean>) {
        val index = colorSchemesViewModel.getIndex(model)
        if (index >= 0 && expandList.size > index && expandList[index]) {
            colorSchemeHeader.apply {
                indicationIcon.icon = AppIcons.arrowDownIcon
                colorSchemes.isVisible = true
            }
        } else {
            colorSchemeHeader.apply {
                indicationIcon.icon = AppIcons.arrowRightIcon
                colorSchemes.isVisible = false
            }
        }
    }

    private fun updateSelectedState(selectedIndex: Int) {
        val index = colorSchemesViewModel.getIndex(model)
        colorSchemeHeader.background = if (selectedIndex == index) ThemeApp.awtColors.complementaryColor
        else ThemeApp.awtColors.primaryColor
    }

    private fun colorSchemeOptions(): List<ColorSchemeOption> = listOf(
        ColorSchemeOption(Constants.SIMPLE, model.simpleColor),
        ColorSchemeOption(Constants.COMMENT, model.commentColor),
        ColorSchemeOption(Constants.RESERVED_WORD, model.reservedWordColor),
        ColorSchemeOption(Constants.RESERVED_WORD_2, model.reservedWord2Color),
        ColorSchemeOption(Constants.HEXADECIMAL, model.hexadecimalColor),
        ColorSchemeOption(Constants.NUMBER, model.numberColor),
        ColorSchemeOption(Constants.FUNCTION, model.functionColor),
        ColorSchemeOption(Constants.STRING, model.stringColor),
        ColorSchemeOption(Constants.DATA_TYPE, model.dataTypeColor),
        ColorSchemeOption(Constants.VARIABLE, model.variableColor),
        ColorSchemeOption(Constants.OPERATOR, model.operatorColor),
        ColorSchemeOption(Constants.PROCESSOR, model.processorColor)
    )

}