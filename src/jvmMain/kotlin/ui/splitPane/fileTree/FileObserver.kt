package ui.splitPane.fileTree

import org.apache.commons.io.monitor.FileAlterationListener
import org.apache.commons.io.monitor.FileAlterationMonitor
import org.apache.commons.io.monitor.FileAlterationObserver
import java.io.File

class FileObserver(path: String){
    private val observer = FileAlterationObserver(path)
    private val monitor = FileAlterationMonitor(1000)

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
        observer.addListener(object : FileAlterationListener{
            override fun onDirectoryChange(directory: File?) {}

            override fun onDirectoryCreate(directory: File) { onCreate(directory) }

            override fun onDirectoryDelete(directory: File) { onDelete(directory) }

            override fun onFileChange(file: File?) {}

            override fun onFileCreate(file: File) { onCreate(file) }

            override fun onFileDelete(file: File) { onDelete(file) }

            override fun onStart(observer: FileAlterationObserver?) {}

            override fun onStop(observer: FileAlterationObserver?) {}
        })

        monitor.addObserver(observer)
        monitor.start()
    }
}