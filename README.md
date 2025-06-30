<div align="center">

<img src="https://github.com/Daniel0110000/DeepCodeStudio/blob/master/ic_launcher.png" width="100px"/>  

# DeepCode Studio
DeepCode Studio (DCS) is a code editor for assembly languages of any architecture, with support for Linux and Windows, where you have full control over syntax highlighting and autocompletion. The main goal of DCS is to become the best IDE for assembly languages, helping developers work more efficiently and easily.

![dcs_screenshot](https://github.com/user-attachments/assets/e0feddec-9a00-4c13-924f-0da4c4d98175)


![GitHub](https://img.shields.io/github/license/daniel0110000/deepcodestudio?style=for-the-badge&labelColor=282C34&color=1F6FEB) ![GitHub](https://img.shields.io/github/v/release/daniel0110000/deepcodestudio?style=for-the-badge&labelColor=282C34&color=1F6FEB)

<a href="https://github.com/Daniel0110000/DeepCodeStudio/releases/tag/v1.0.0-alpha.2">
  <img src="https://img.shields.io/badge/Linux-282C34?style=for-the-badge&logo=linux&logoColor=white"/>
</a>

<a href="https://github.com/Daniel0110000/DeepCodeStudio/releases/tag/v0.0.1-rc1">
  <img src="https://img.shields.io/badge/Windows-282C34?style=for-the-badge&logo=window&logoColor=white"/>
</a>
</div>

# Installation
Linux binary, Windows installer, JAR file, and configuration JSON file are available on the [releases](https://github.com/Daniel0110000/DeepCodeStudio/releases) page.

## Linux
1. Download the `AppImage`
2. Execute `chmod +x DCS-*AppImage`
3. Run the program with `./DCS-*AppImage`

## Windows
Download and run the Windows installer.

## Jar
1. Download the editor version in `.jar`
2. Run the program with `java -Xss2m -jar DCS-*.jar`

> [!IMPORTANT]  
> For the editor to work properly, you need Java 17 installed on your system and must add the VM option `"-Xss2m"`, as shown in the section above.

# Configuration
**DeeCode Studio** use `JSON` files to configure `syntax highlighting` and `autocomplete suggestions`. This `JSON` file must have the following format:

```json
{
  "data": {
    "instructions": [ "mov", "..." ],
    "variables": [ "db", "..." ],
    "constants": [ "equ", "..." ],
    "segments": [ ".data", "..." ],
    "registers": [ "rax", "..." ],
    "systemCall": [ "int 0x80", "..." ],
    "arithmeticInstructions": [ "inc", "..." ],
    "logicalInstructions": [ "and", "..." ],
    "conditions": [ "jnz", "..." ],
    "loops": [ "loop", "..." ],
    "memoryManagement": [ "malloc", "..." ],
  }
}
```

You can also download the example configuration JSON file from the [releases](https://github.com/Daniel0110000/DeepCodeStudio/releases) page.

## New Configuration
Now, to add a new configuration for syntax highlighting and autocomplete suggestions to the editor, follow these steps:

![Syntax_Suggestions_Settings](https://github.com/user-attachments/assets/6ef18998-0e50-4fe1-9043-e15f796e58d9)


1. Open the settings and go to the `"Syntax & Suggestions"` option.
2. Add the name you want for the configuration option.
3. Click on the folder icon; this will open a file selector. Select the `.json` file you want to use.
4. Click on the button to add the option, located next to the folder icon.
5. A notification will appear; wait for it to disappear, then wait for the next one with the text `"Option added successfully!"`
6. Perfect! You now have the configuration added.
7. Now, every time you open a `.s` or `.asm` file, a panel will open on the right side of the editor. In this panel, you can choose the configuration you want for that file.

## Customization of syntax highlighting colors
You can fully customize the colors for syntax highlighting. To customize them, go to: `"Settings > Color Scheme"`.

![Color_Scheme_Settings](https://github.com/user-attachments/assets/3e9766c9-cebb-48c0-b650-11abde45451e)


# Contributing
You can contribute in the following ways:
- Creating configuration JSON files with instructions and reserved words for different assembly languages.
- Proposing ideas for the editor.
- Reporting bugs.
- Adding new features to the editor.

# Roadmap
- [ ] Automatic execution and compilation using Makefile and CMake files.
- [ ] Autocomplete suggestions with data extracted from the code, such as variable names, labels, macros, etc.
- [ ] Editor customization system.
- [ ] Improvement of the extension system.
- [ ] Improvement of the autocomplete suggestion system.
- [ ] Improvement of the file and folder selection system.
- [ ] Fix bugs that have arisen.
- [ ] Optimizations
- [ ] 🚀 New release

# 📜 License
[Apache-2.0](/LICENSE)
