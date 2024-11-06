package ui.fileTree

import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.UIStateManager
import com.dr10.editor.ui.viewModels.TabsViewModel
import ui.viewModels.FileTreeViewModel
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.SwingConstants
import javax.swing.SwingUtilities
import javax.swing.border.EmptyBorder

/**
 * JPanel representing the file tree view in the code editor
 *
 * @property window The main application window [JFrame]
 * @property fileTreeViewModel The viewModel that manages the state of the file tree
 */
class FileTreeView(
    private val window: JFrame,
    private val fileTreeViewModel: FileTreeViewModel,
    private val tabsViewModel: TabsViewModel
): JPanel() {

    init { onCreate() }

    private fun onCreate() {
        layout = BorderLayout()
        preferredSize = Dimension(350, Short.MAX_VALUE.toInt())
        background = ThemeApp.colors.background.toAWTColor()

        val title = JLabel("Folders", SwingConstants.CENTER).apply {
            font = ThemeApp.text.fontInterBold()
            foreground = ThemeApp.colors.textColor.toAWTColor()
            border = EmptyBorder(10, 0, 10, 0)
        }

        val fileTree = JTree().apply {
            border = EmptyBorder(0, 10, 0, 10)
            font = ThemeApp.text.fontInterRegular(13f)
            showsRootHandles = true
            background = ThemeApp.colors.background.toAWTColor()
            cellRenderer = FileTreeCellRenderer()
        }

        // Mouse listener to handle double-click and right-click events
        val clickListener: MouseListener = object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                when {
                    e.clickCount == 2 -> {
                        val selectedFile = fileTree.lastSelectedPathComponent as File
                        selectedFile.takeIf { it.isFile }?.let { file ->
                            if (file.name.endsWith(".s") || file.name.endsWith(".asm")) {
                                tabsViewModel.openTab(file)
                            }
                        }
                    }
                    SwingUtilities.isRightMouseButton(e) -> {
                        val filePath = fileTree.getPathForLocation(e.x, e.y)
                        filePath?.lastPathComponent?.let { node ->
                            if (node is File) {
                                FileDropdownMenu(window, node).show(e.component, e.x, e.y)
                            }
                        }
                    }
                }
            }
        }

        fileTree.addMouseListener(clickListener)

        val scrollPanel = JScrollPane(fileTree).apply {
            border = null
            verticalScrollBar.setUI(CustomScrollBar())
            horizontalScrollBar.setUI(CustomScrollBar())
        }

        val labelLoading = JLabel("Loading ...", SwingConstants.CENTER).apply {
            font = ThemeApp.text.fontInterRegular(12f)
            foreground = ThemeApp.colors.textColor.toAWTColor()
        }

        add(title, BorderLayout.NORTH)

        // Manage the UI state based on change in the file tree's state
        UIStateManager(
            stateFlow = fileTreeViewModel.state,
            onStateChanged = { state: FileTreeViewModel.FileTreeState ->
                fileTree.model = FileSystemModel(
                    File(state.currentPath),
                    fileTree,
                    isLoading = {
                        if (it) {
                            // Show the loading label while the file tree is loading
                            remove(scrollPanel)
                            add(labelLoading, BorderLayout.CENTER)
                        } else {
                            // Show the file tree once loading is complete
                            remove(labelLoading)
                            add(scrollPanel, BorderLayout.CENTER)
                        }
                        // Refresh the UI to reflect changes
                        revalidate()
                        repaint()
                    },
                    onDeleteFile = { fileTreeViewModel.deleteSelectedConfig(it) }
                )
            }
        )

    }
}