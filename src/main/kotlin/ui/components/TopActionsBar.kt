package ui.components

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.TextUtils.replaceHomePath
import com.dr10.common.utilities.setState
import ui.viewModels.CodeEditorViewModel
import javax.swing.BorderFactory
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel

class TopActionsBar(
    private val codeEditorState: FlowStateHandler.StateWrapper<CodeEditorViewModel.CodeEditorState>,
    private val openSettings: () -> Unit
): JPanel() {

    private val topActionsBarLayout = GroupLayout(this)

    private lateinit var icApp: JLabel
    private lateinit var appName: JLabel

    private lateinit var currentPath: JLabel

    private lateinit var settingsOption: JPanel
    private lateinit var executeOption: JPanel

    init {
        layout = topActionsBarLayout
        background = ThemeApp.awtColors.secondaryColor
        border = BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeApp.awtColors.primaryColor)

        initializeComponents()
        setComponentsStructure()
    }

    private fun initializeComponents() {
        icApp = JLabel(AppIcons.appTBIIcon)
        appName = JLabel("DCS").apply {
            font = ThemeApp.text.fontInterBold(12f)
            foreground = ThemeApp.awtColors.textColor
        }

        currentPath = JLabel().apply {
            font = ThemeApp.text.fontInterRegular(13f)
            foreground = ThemeApp.awtColors.textColor
            setState(codeEditorState, CodeEditorViewModel.CodeEditorState::currentPathSelected) { currentPath ->
                text = (if (currentPath.isBlank()) "~" else currentPath.replaceHomePath()) + " · DCS"
            }
        }

        settingsOption = TopAction(
            icon = AppIcons.settingsIcon,
            onClickListener = { openSettings() }
        ).apply {
            setState(codeEditorState, CodeEditorViewModel.CodeEditorState::isOpenSettings) { isOpenSettings ->
                isSelected = isOpenSettings
            }
        }

        executeOption = TopAction(AppIcons.runIcon) {}
    }

    private fun setComponentsStructure() {
        topActionsBarLayout.setVerticalGroup(
            topActionsBarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE.toInt())
                .addGroup(
                    topActionsBarLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(icApp)
                        .addComponent(appName)
                        .addComponent(currentPath)
                        .addComponent(executeOption, 30, 30, 30)
                        .addComponent(settingsOption, 30, 30, 30)
                )
                .addGap(0, 0, Short.MAX_VALUE.toInt())
        )

        topActionsBarLayout.setHorizontalGroup(
            topActionsBarLayout.createSequentialGroup()
                .addGap(0, 0, 10)
                .addComponent(icApp)
                .addGap(0, 0, 5)
                .addComponent(appName)
                .addGap(0, 0, Short.MAX_VALUE.toInt())
                .addComponent(currentPath)
                .addGap(0, 0, Short.MAX_VALUE.toInt())
                .addComponent(executeOption, 30, 30, 30)
                .addComponent(settingsOption, 30, 30, 30)
                .addGap(0, 0, 10)
        )
    }


}