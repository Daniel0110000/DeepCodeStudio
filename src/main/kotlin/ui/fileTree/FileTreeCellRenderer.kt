package ui.fileTree

import com.dr10.common.ui.AppIcons
import com.dr10.common.ui.ThemeApp
import com.dr10.common.utilities.ColorUtils.toAWTColor
import java.awt.Component
import javax.swing.JLabel
import javax.swing.JTree
import javax.swing.tree.DefaultTreeCellRenderer


class FileTreeCellRenderer: DefaultTreeCellRenderer() {

    override fun getTreeCellRendererComponent(
        tree: JTree?,
        value: Any?,
        sel: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ): Component {
        val label = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus) as JLabel
        val treeFile = value as? FileSystemModel.TreeFile

        treeFile?.let { file ->
            label.icon = when {
                file.isDirectory -> AppIcons.folderIconFT
                (file.name.endsWith(".asm") || file.name.endsWith(".s")) -> AppIcons.asmIcon
                file.name.equals("Makefile")  -> AppIcons.makefileIcon
                else -> AppIcons.unknownFile
            }

            label.foreground = when {
                file.isDirectory && expanded -> ThemeApp.colors.folderOpenTextColor.toAWTColor()
                file.isDirectory -> ThemeApp.colors.folderCloseTextColor.toAWTColor()
                else -> ThemeApp.colors.textColor.toAWTColor()
            }

        }

        openIcon = AppIcons.folderIconFT

        return label
    }

}