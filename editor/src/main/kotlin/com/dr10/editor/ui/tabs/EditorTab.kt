package com.dr10.editor.ui.tabs

import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomSplitPaneDivider
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import com.dr10.editor.di.Inject
import com.dr10.editor.ui.viewModels.EditorTabViewModel
import java.awt.Dimension
import javax.swing.GroupLayout
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.SwingConstants

/**
 * Represents an editor tab that contains the code editor logic
 *
 * @property tab The model containing the tab's information
 */
class EditorTab(private val tab: TabModel): JPanel() {

    private val viewModel: EditorTabViewModel = EditorTabViewModel(
        Inject().syntaxAndSuggestionsRepository,
        Inject().editorRepository
    )

    private val editorTabState = FlowStateHandler().run {
        viewModel.state.collectAsState(EditorTabViewModel.EditorTabState())
    }

    private var dividerLocation = 0

    init { onCreate() }

    private fun onCreate() {
        viewModel.setCurrentFilePath(tab.filePath)
        viewModel.getSelectedConfig()
        val editorTabLayout = GroupLayout(this)
        layout = editorTabLayout
        background = ThemeApp.colors.background.toAWTColor()

        val editorSplitPane = JSplitPane(
            SwingConstants.VERTICAL,
            CodeEditor(tab, viewModel, editorTabState),
            AutoCompleteOptions(viewModel, editorTabState) { model -> viewModel.insertOrUpdateSelectedConfig(model) }
        ).apply {
            setUI(CustomSplitPaneDivider())
            resizeWeight = 1.0
            isContinuousLayout = true
        }

        dividerLocation = editorSplitPane.width - 200

        setState(editorTabState, EditorTabViewModel.EditorTabState::isCollapseAutocompleteOptions) { isCollapse ->
            if (isCollapse) {
                dividerLocation = editorSplitPane.dividerLocation
                editorSplitPane.setDividerLocation(Short.MAX_VALUE.toInt())
                editorSplitPane.dividerSize = 0
                editorSplitPane.rightComponent.minimumSize = Dimension()
            } else {
                editorSplitPane.setDividerLocation(dividerLocation)
                editorSplitPane.dividerSize = 3
                editorSplitPane.rightComponent.minimumSize = Dimension(200,  Short.MAX_VALUE.toInt())
            }
        }

        editorTabLayout.setHorizontalGroup(
            editorTabLayout.createParallelGroup()
                .addComponent(editorSplitPane, 0, 0, Short.MAX_VALUE.toInt())
        )

        editorTabLayout.setVerticalGroup(
            editorTabLayout.createSequentialGroup()
                .addComponent(editorSplitPane, 0, 0, Short.MAX_VALUE.toInt())
        )

    }

}