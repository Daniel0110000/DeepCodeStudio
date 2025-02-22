package com.dr10.common.ui

import com.dr10.common.utilities.ImageResourceUtils
import javax.swing.plaf.IconUIResource

object AppIcons {

    val appIcon = ImageResourceUtils.loadResourceImage("images/ic_app.svg", 300, 300)

    val folderIconFT = ImageResourceUtils.loadResourceImage("images/ic_folder_ft.svg", 20, 20)
    val asmIcon = ImageResourceUtils.loadResourceImage("images/ic_asm.svg", 20, 20)
    val makefileIcon = ImageResourceUtils.loadResourceImage("images/ic_makefile.svg", 17, 17)
    val asmBottomIcon = ImageResourceUtils.loadResourceImage("images/ic_asm.svg", 17, 17)
    val analyzerIcon = ImageResourceUtils.loadResourceImage("images/ic_analyzing.svg", 19, 19)
    val unknownFile = ImageResourceUtils.loadResourceImage("images/ic_unknown.svg", 20, 20)

    val expandedIcon = IconUIResource(ImageResourceUtils.loadResourceImage("images/ic_expanded.svg", 20, 20))
    val collapseIcon = IconUIResource(ImageResourceUtils.loadResourceImage("images/ic_collapse_file_tree.svg", 20, 20))

    val fileIcon = ImageResourceUtils.loadResourceImage("images/ic_new_file.svg", 18, 19)
    val folderIcon = ImageResourceUtils.loadResourceImage("images/ic_new_folder.svg", 17, 17)
    val renameIcon = ImageResourceUtils.loadResourceImage("images/ic_rename.svg", 17, 17)
    val deleteIcon = ImageResourceUtils.loadResourceImage("images/ic_delete.svg", 17, 17)

    val closeIcon = ImageResourceUtils.loadResourceImage("images/ic_close.svg", 15, 15)
    val closeTerminalIcon = ImageResourceUtils.loadResourceImage("images/ic_close.svg", 18, 18)
    val newTerminalIcon = ImageResourceUtils.loadResourceImage("images/ic_new_terminal.svg", 18, 18)
    val arrowDownIcon = ImageResourceUtils.loadResourceImage("images/ic_arrow.svg", 20, 20)
    val arrowRightIcon = ImageResourceUtils.loadResourceImage("images/ic_arrow_right.svg", 20, 20)

    val defaultShellLinux = ImageResourceUtils.loadResourceImage("images/ic_linux.svg", 15, 15)
    val defaultShellWindows = ImageResourceUtils.loadResourceImage("images/ic_windows.svg", 15, 15)
    val bashIcon = ImageResourceUtils.loadResourceImage("images/ic_bash.svg", 15, 15)
    val zshIcon = ImageResourceUtils.loadResourceImage("images/ic_zsh_cmd.svg", 15, 15)
    val fishIcon = ImageResourceUtils.loadResourceImage("images/ic_fish.svg", 15, 15)
    val powershellIcon = ImageResourceUtils.loadResourceImage("images/ic_powershell.svg", 15, 15)
    val cmdIcon = ImageResourceUtils.loadResourceImage("images/ic_zsh_cmd.svg", 15, 15)

    val readOnlyIcon = ImageResourceUtils.loadResourceImage("images/ic_lock.svg", 15, 15)
    val writeEnableIcon = ImageResourceUtils.loadResourceImage("images/ic_lock_open.svg", 15, 15)

    val suggestionWordIcon = ImageResourceUtils.loadResourceImage("images/ic_suggestion_word.svg", 15, 15)
    val suggestionWordSelectedIcon = ImageResourceUtils.loadResourceImage("images/ic_suggestion_word_selected.svg", 15, 15)

    val jsonIcon = ImageResourceUtils.loadResourceImage("images/ic_json.svg", 18, 18)
    val deleteOpIcon = ImageResourceUtils.loadResourceImage("images/ic_delete_op.svg", 18, 18)
    val addIcon = ImageResourceUtils.loadResourceImage("images/ic_add.svg", 18, 18)
    val simpleFolderIcon = ImageResourceUtils.loadResourceImage("images/ic_folder.svg", 18, 18)

    val successIcon = ImageResourceUtils.loadResourceImage("images/ic_success.svg", 18, 18)
    val errorIcon = ImageResourceUtils.loadResourceImage("images/ic_error.svg", 18, 18)
    val infoIcon = ImageResourceUtils.loadResourceImage("images/ic_info.svg", 18, 18)
    val warningIcon = ImageResourceUtils.loadResourceImage("images/ic_warning.svg", 18, 18)

    val executeIcon = ImageResourceUtils.loadResourceImage("images/ic_execute.svg", 18, 18)
    val regexIcon = ImageResourceUtils.loadResourceImage("images/ic_regex.svg", 18, 18)
}