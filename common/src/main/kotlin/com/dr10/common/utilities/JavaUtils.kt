package com.dr10.common.utilities

import com.dr10.common.utilities.TextUtils.deleteWhiteSpaces
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import java.io.File
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Paths
import javax.tools.JavaCompiler
import javax.tools.ToolProvider

object JavaUtils {

    /**
     * Deletes the duplicate functions "zzRefill" and "yyreset" from the java file
     *
     * @param optionName The name to reference the class
     */
    fun deleteUnnecessaryFunctionsFromJavaFile(optionName: String) {
        val javaFilePath = getJavaFilePath(optionName)
        if (javaFilePath.toFile().exists()) {
            val compilationUnit: CompilationUnit = StaticJavaParser.parse(javaFilePath)

            val allMethods = compilationUnit.findAll(MethodDeclaration::class.java)

            allMethods
                .filter { it.nameAsString == "zzRefill" }
                .maxByOrNull { it.toString().length }
                ?.remove()

            allMethods
                .filter { it.nameAsString == "yyreset" }
                .maxByOrNull { it.toString().length }
                ?.remove()

            Files.writeString(javaFilePath, compilationUnit.toString())
        }
    }

    /**
     * Compiles the java file and save the classes in the [DocumentsManager.localDirectory]/[Constants.CLASSES_DIRECTORY_NAME] path
     *
     * @param optionName The name to reference the class
     * @return True if the compilation was successful, false otherwise
     */
    fun compileJavaFile(optionName: String): Boolean {
        val compiler: JavaCompiler = ToolProvider.getSystemJavaCompiler()
        return compiler.getStandardFileManager(null, null, null).use {
            val options = listOf("-d", "${DocumentsManager.localDirectory}/${Constants.CLASSES_DIRECTORY_NAME}")
            val compileUnits = it.getJavaFileObjectsFromFiles(listOf(getJavaFilePath(optionName).toFile()))
            val task = compiler.getTask(null, it, null, options, null, compileUnits)
            task.call()

        }
    }

    /**
     * Gets the path of the java file
     *
     * @param optionName The name to reference the class
     */
    private fun getJavaFilePath(optionName: String) =
        Paths.get("${DocumentsManager.javaFilesDirectory}/${optionName.deleteWhiteSpaces()}${Constants.CLASS_EXTENSION}${Constants.JAVA_EXTENSION}")

    /**
     * Creates a custom class loader to load the classes from the [DocumentsManager.classFilesDirectory] path
     */
    fun createCustomClasLoader(): ClassLoader {
        val urls = arrayOf(File(DocumentsManager.classFilesDirectory.absolutePath).toURI().toURL())
        return URLClassLoader(urls, ClassLoader.getSystemClassLoader())
    }

}