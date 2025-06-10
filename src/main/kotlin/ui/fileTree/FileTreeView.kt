package ui.fileTree

import com.dr10.common.ui.ThemeApp
import com.dr10.common.ui.components.CustomScrollBar
import com.dr10.common.utilities.ColorUtils.toAWTColor
import com.dr10.common.utilities.FlowStateHandler
import com.dr10.common.utilities.setState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.viewModels.FileTreeViewModel
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import javax.swing.*
import javax.swing.border.EmptyBorder

/**
 * JPanel representing the file tree view in the code editor
 *
 * @property window The main application window [JFrame]
 * @property fileTreeState Current [ui.fileTree.FileTreeView] state
 * @property onOpenTab Callback function invoked when a tab need to be opened
 * @property onDeleteSelectedConfig Callback function invoked when a [.s] or [.asm] file is deleted from the [ui.fileTree.FileTreeView] to remove the configuration related to the file
 */
class FileTreeView(
    private val window: JFrame,
    private val fileTreeState: FlowStateHandler.StateWrapper<FileTreeViewModel.FileTreeState>,
    private val onOpenTab: (File) -> Unit,
    private val onDeleteSelectedConfig: (File) -> Unit
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
                            when {
                                file.name.endsWith(".s") || file.name.endsWith(".asm") -> onOpenTab(file)
                                file.name.equals("Makefile") -> { onOpenTab(file) }
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

        setState(fileTreeState, FileTreeViewModel.FileTreeState::currentPath) { path ->
            CoroutineScope(Dispatchers.Default).launch {
                fileTree.model = FileSystemModel(
                    root = File(path),
                    jTree = fileTree,
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
                    onDeleteFile = { onDeleteSelectedConfig(it) }
                )
            }
        }

    }
}