package ui.fileTree

class FileTree(root: File) {

    // Create an expandable root item using the provided 'root' file
    private val expandableRoot = ExpandableFile(root, 0).apply {
        toggleExpanded()
    }

    // Convert the expandableRoot to a list of items
    val items: List<Item> get() = expandableRoot.toItems()

    /**
     * Inner class representing an item
     *
     * @param file The ExpandableFile associated with the item
     */
    inner class Item(
        private val file: ExpandableFile
    ){
        // Property: Name of the item
        val name: String get() = file.file.name
        // Property: Level of the item in the hierarchy
        val level: Int get() = file.level

        // Property: Type of the item (folder ot file)
        val type: ItemType get() = if(file.file.hasChildren) ItemType.Folder(isExpandable = file.children.isEmpty(), canExpand = file.canExpand)
                                   else ItemType.File(ext = file.file.name.substringAfterLast(".").lowercase())

        /**
         * Opens the item
         *
         * Depending on the item's type, this function either toggles the expansion s tate of a folder
         * or open the file with the code editor
         */
        fun open() = when(type){
            is ItemType.Folder -> file.toggleExpanded()
            is ItemType.File -> println("Open in the editor")
        }

    }

    /**
     * Sealed class that represents different types of items
     */
    sealed class ItemType{
        /**
         * Represents a folder item
         *
         * @param isExpandable Indicates whether the folder is expandable
         * @param canExpand Indicates whether the folder can be expanded
         */
        class Folder(val isExpandable: Boolean, val canExpand: Boolean): ItemType()

        /**
         * Represents a file item
         *
         * @param ext The file extension
         */
        class File(val ext: String): ItemType()
    }


    /**
     * Converts an ExpandableFile to a list of Item objects
     *
     * @return A list of items representing the ExpandableFile and its children
     */
    private fun ExpandableFile.toItems(): List<Item>{
        /**
         * Recursive function to add an ExpandableFile and its children to a list
         *
         * @param list The list to which the items are being added
         */
        fun ExpandableFile.addTo(list: MutableList<Item>){
            list.add(Item(this))
            for (child in children) child.addTo(list)
        }

        val list = mutableListOf<Item>()
        addTo(list)
        return list
    }

}