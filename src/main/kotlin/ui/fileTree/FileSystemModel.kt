package ui.fileTree

import java.io.File
import java.util.Vector
import javax.swing.JTree
import javax.swing.event.TreeModelEvent
import javax.swing.event.TreeModelListener
import javax.swing.tree.TreeModel
import javax.swing.tree.TreePath

class FileSystemModel(
    private val root: File,
    private val jTree: JTree,
    private val isLoading: (Boolean) -> Unit,
    private val onDeleteFile: (File) -> Unit
): TreeModel {

    private val listeners = Vector<TreeModelListener>()

    init {
        val fileObserver = FileObserver(root.path){ isLoading(it) }
        fileObserver.registerCallbacksForChanges(
            onCreate = { file -> notifyFileSystemChange(file) },
            onDelete = { file ->
                onDeleteFile(file)
                notifyFileSystemChange(file)
            }
        )
    }

    override fun getRoot(): Any = root

    override fun getChild(parent: Any, index: Int): Any {
        val directory = parent as File
        val children = directory.list()
        return TreeFile(directory, children?.get(index) ?: "")
    }

    override fun getChildCount(parent: Any): Int {
        val file = parent as File
        if (file.isDirectory) {
            val fileList = file.list()
            if (fileList != null) {
                return file.list()?.size ?: 0
            }
        }
        return 0
    }

    override fun isLeaf(node: Any?): Boolean {
        val file = node as File
        return file.isFile
    }

    override fun valueForPathChanged(path: TreePath, newValue: Any) {
        val oldFile = path.lastPathComponent as File
        val fileParentPath = oldFile.parent
        val newFileName = newValue as String
        val targetFile = File(fileParentPath, newFileName)
        oldFile.renameTo(targetFile)
        val parent = File(fileParentPath)
        val changedChildrenIndices = IntArray(getIndexOfChild(parent, targetFile))
        val changedChildren: Array<Any> = arrayOf(targetFile)
        fireTreeNodesChanged(path.parentPath, changedChildrenIndices, changedChildren)
    }

    private fun fireTreeNodesChanged(parentPath: TreePath, indices: IntArray, children: Array<Any>) {
        val event = TreeModelEvent(this, parentPath, indices, children)
        listeners.forEach { listener -> listener.treeNodesChanged(event) }
    }


    override fun getIndexOfChild(parent: Any, child: Any): Int {
        val directory = parent as File
        val file = child as File
        val children = directory.list()
        children?.forEachIndexed { index, s ->
            if(file.name.equals(s)) return index
        }
        return -1
    }

    override fun addTreeModelListener(l: TreeModelListener) {
        listeners.add(l)
    }

    override fun removeTreeModelListener(l: TreeModelListener) {
        listeners.remove(l)
    }

    private fun fireTreeStructureChanged(parentPath: TreePath) {
        val event = TreeModelEvent(this, parentPath)
        listeners.forEach { listener -> listener.treeStructureChanged(event) }
    }

    private fun notifyFileSystemChange(changedFile: File) {
        val parentFile = changedFile.parentFile
        val parentPath = TreePath(parentFile.parent.split(File.separator))

        val expandedPaths = getExpandedPaths()

        fireTreeStructureChanged(parentPath)

        restoreExpandedPaths(expandedPaths)
    }

    private fun getExpandedPaths(): List<TreePath> =
        jTree.getExpandedDescendants(TreePath(root))?.toList() ?: emptyList()

    private fun restoreExpandedPaths(paths: List<TreePath>) {
        paths.forEach { path ->
            jTree.expandPath(path)
        }
    }

    class TreeFile(parent: File, child: String) : File(parent, child) {
        override fun toString(): String {
            return name
        }

    }

}