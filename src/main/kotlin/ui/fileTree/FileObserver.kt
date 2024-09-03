package ui.fileTree

import org.apache.commons.io.monitor.FileAlterationListener
import org.apache.commons.io.monitor.FileAlterationMonitor
import org.apache.commons.io.monitor.FileAlterationObserver
import java.io.File

/**
 * Class to file system changes in a specified directory
 *
 * @property path The directory path to observe file system changes
 * @property isLoading Callback function to indicate whether the file monitoring is loading
 */
class FileObserver(
    private val path: String,
    private val isLoading: (Boolean) -> Unit
){
    private var observer: FileAlterationObserver? = null
    private var monitor: FileAlterationMonitor? = null

    init {
        // Stop any existing monitoring and remove listeners
        monitor?.stop()
        observer?.removeListener(null)
        monitor?.removeObserver(observer)
    }

    /**
     * Register callback function for file and directory creation
     *
     * @param onCreate Callback function for file or directory creation
     * @param onDelete Callback function for file pr directory deletion
     */
    fun registerCallbacksForChanges(
        onCreate: (File) -> Unit,
        onDelete: (File) -> Unit
    ){
        observer = FileAlterationObserver(path)
        monitor = FileAlterationMonitor(500)
        observer?.addListener(object : FileAlterationListener{
            override fun onDirectoryChange(directory: File?) {}

            override fun onDirectoryCreate(directory: File) { onCreate(directory) }

            override fun onDirectoryDelete(directory: File) { onDelete(directory) }

            override fun onFileChange(file: File?) {}

            override fun onFileCreate(file: File) { onCreate(file) }

            override fun onFileDelete(file: File) { onDelete(file) }

            override fun onStart(observer: FileAlterationObserver?) {}

            override fun onStop(observer: FileAlterationObserver?) {}
        })

        monitor?.addObserver(observer)
        isLoading(true)
        monitor?.start()
        isLoading(false)
    }
}