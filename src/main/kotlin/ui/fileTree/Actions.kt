package ui.fileTree

enum class Actions(private val title: String){
    NONE("None"),
    NEW_FOLDER("New Folder"),
    NEW_FILE("New File"),
    RENAME("Rename");

    fun getTitle(): String = title
}