package com.dr10.settings.ui.screens.colorScheme

import com.dr10.common.models.ColorSchemeModel
import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import com.dr10.common.utilities.ColorUtils
import com.dr10.common.utilities.DocumentsManager
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.JavaUtils
import com.dr10.common.utilities.TextUtils.deleteWhiteSpaces
import com.dr10.common.utilities.setState
import com.dr10.settings.di.Inject
import com.dr10.settings.ui.screens.colorScheme.components.CollapsibleColorSchemePanel
import com.dr10.settings.ui.viewModels.ColorSchemesViewModel
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.border.EmptyBorder

class ColorSchemeSettingsScreen: JPanel() {

    private val viewModel: ColorSchemesViewModel = Inject().colorSchemesViewModel
    private val colorSchemeState = FlowStateHandler().run {
        viewModel.state.collectAsState(ColorSchemesViewModel.ColorSchemesState())
    }

    private lateinit var optionsContainer: JPanel
    private lateinit var optionTitleSelected: JLabel

    private val currentOptions = mutableMapOf<String, String>()
    private val textFieldColorTests = RSyntaxTextArea()

    init { onCreate() }

    private fun onCreate() {
        val colorSchemeSettingsLayout = GroupLayout(this)
        layout = colorSchemeSettingsLayout
        background = ThemeApp.awtColors.primaryColor
        border = EmptyBorder(10, 10, 10, 10)

        optionsContainer = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            background = ThemeApp.awtColors.primaryColor
            border = EmptyBorder(5, 5, 5, 5)

            setState(colorSchemeState, ColorSchemesViewModel.ColorSchemesState::allColorSchemes) { colorSchemes ->
                updateColorSchemes(colorSchemes)
            }
        }

        val optionsScrollPane = JScrollPane(optionsContainer).apply {
            background = ThemeApp.awtColors.primaryColor
            border = BorderFactory.createLineBorder(ThemeApp.awtColors.hoverColor)
            verticalScrollBar.setUI(CustomScrollBar())
            horizontalScrollBar.setUI(CustomScrollBar())
        }

        optionTitleSelected = JLabel().apply {
            foreground = ThemeApp.awtColors.textColor
            font = ThemeApp.text.fontInterRegular(13f)
        }

        setState(colorSchemeState, ColorSchemesViewModel.ColorSchemesState::selectedOption) { selectedOption  ->
            updateSelectedOption(selectedOption)
        }

        textFieldColorTests.apply {
            text = AssemblyCodeExamples.getCodeExample()
            isCodeFoldingEnabled = false
            background = ThemeApp.awtColors.secondaryColor
            foreground = ThemeApp.awtColors.textColor
            font = ThemeApp.text.fontJetBrains
            caretPosition = 0
            border = EmptyBorder(5, 5, 5, 5)
            font = ThemeApp.text.fontJetBrains
            currentLineHighlightColor = ThemeApp.awtColors.hoverColor
            caretColor = ThemeApp.awtColors.complementaryColor
            selectionColor = ThemeApp.awtColors.complementaryColor
        }

        val scrollPane = JScrollPane(textFieldColorTests).apply {
            background = ThemeApp.awtColors.secondaryColor
            border = BorderFactory.createLineBorder(ThemeApp.awtColors.hoverColor)
            verticalScrollBar.setUI(CustomScrollBar(ThemeApp.awtColors.secondaryColor))
            horizontalScrollBar.setUI(CustomScrollBar(ThemeApp.awtColors.secondaryColor))
        }

        colorSchemeSettingsLayout.setHorizontalGroup(
            colorSchemeSettingsLayout.createParallelGroup()
                .addComponent(optionsScrollPane, 0, 0, Short.MAX_VALUE.toInt())
                .addComponent(optionTitleSelected)
                .addComponent(scrollPane, 0, 0, Short.MAX_VALUE.toInt())
        )

        colorSchemeSettingsLayout.setVerticalGroup(
            colorSchemeSettingsLayout.createSequentialGroup()
                .addComponent(optionsScrollPane, 0, 0, Short.MAX_VALUE.toInt())
                .addGap(10)
                .addComponent(optionTitleSelected)
                .addGap(10)
                .addComponent(scrollPane, 0, 0, Short.MAX_VALUE.toInt())
        )

    }

    private fun updateColorSchemes(colorSchemes: List<ColorSchemeModel>) {
        val existingSchemeIds = colorSchemes.map { it.uniqueId }.toSet()
        currentOptions.keys.retainAll(existingSchemeIds)

        optionsContainer.components
            .filterIsInstance<CollapsibleColorSchemePanel>()
            .filter { panel -> panel.model.uniqueId !in existingSchemeIds }
            .forEach { panel -> optionsContainer.remove(panel) }

        colorSchemes.forEach { model ->
            if (!currentOptions.containsKey(model.uniqueId)) {
                currentOptions[model.uniqueId] = model.jsonPath
                if (DocumentsManager.existsFile(model.jsonPath)) {
                    val instance = CollapsibleColorSchemePanel(
                        colorSchemesViewModel = viewModel,
                        model = model,
                        colorSchemesState = colorSchemeState
                    )
                    optionsContainer.add(instance)
                }
            }
        }
    }

    private fun updateSelectedOption(selectedOption: ColorSchemeModel?) {
        if (selectedOption != null) {
            optionTitleSelected.text = selectedOption.optionName
            val syntaxKey = "syntax/${selectedOption.optionName}".deleteWhiteSpaces()
            val tokenMarker: AbstractTokenMakerFactory = TokenMakerFactory.getDefaultInstance() as AbstractTokenMakerFactory
            val customClassLoader: ClassLoader = JavaUtils.createCustomClasLoader()
            tokenMarker.putMapping(syntaxKey, selectedOption.className, customClassLoader)
            textFieldColorTests.syntaxEditingStyle = syntaxKey
            textFieldColorTests.syntaxScheme.setSyntaxColorScheme(selectedOption)
            textFieldColorTests.foreground = ColorUtils.stringToColor(selectedOption.simpleColor)
        } else { optionTitleSelected.text = "..." }
    }

}