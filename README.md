<div align="center">

<img src="https://github.com/Daniel0110000/DeepCodeStudio/blob/master/ic_launcher.png" width="100px"/>  


# **DeepCode Studio**

Welcome to **DeepCode Studio**, an assembly code editor, compatible with a wide range of assembly languages, designed to provide a seamless experience in both Windows and Linux environments.

![image](https://github.com/user-attachments/assets/7cf1536a-cf39-47ce-8644-8df1ae365059)

![GitHub](https://img.shields.io/github/license/daniel0110000/deepcodestudio?style=for-the-badge&labelColor=282C34&color=1F6FEB) ![GitHub](https://img.shields.io/github/v/release/daniel0110000/deepcodestudio?style=for-the-badge&labelColor=282C34&color=1F6FEB)

<a href="https://github.com/Daniel0110000/DeepCodeStudio/releases/tag/v1.0.0-alpha.2">
  <img src="https://img.shields.io/badge/Linux-282C34?style=for-the-badge&logo=linux&logoColor=white"/>
</a>

<a href="https://github.com/Daniel0110000/DeepCodeStudio/releases/tag/v1.0.0-alpha.2">
  <img src="https://img.shields.io/badge/Windows-282C34?style=for-the-badge&logo=window&logoColor=white"/>
</a>

</div>

> [!IMPORTANT]
> The program is currently in an alpha phase, so it may contain several bugs. Any errors you encounter can be reported in the "issues" section for resolution.

## 🧩 Configuration
In this version of **DeepCode Studio**, a JSON is used for code autocompletion and syntax highlighting. This JSON must have the following structure:

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

<a href="https://github.com/Daniel0110000/DeepCodeStudio/blob/master/src/main/resources/extras/asm_config.json"/>
  <img src="https://img.shields.io/badge/Download%20Example-1F6FEB?style=for-the-badge&logo=google-cloud&logoColor=white"/>
<a/>

<div align="center">

| Open the settings and go to "Autocomplete"              | Click on the directory icon               |
| ---------------------- | ---------------------- |
| ![Open Settings](https://github.com/user-attachments/assets/86dec1aa-047e-4e99-82f9-c1576e940604) | ![Directory Icon](https://github.com/user-attachments/assets/c9bd0a65-3e9a-49b0-b3bf-3118743aa8f3)|

| Select the JSON file               | Enter the configuration name               |
| ---------------------- | ---------------------- |
| ![Select JSON](https://github.com/user-attachments/assets/ef00fcbd-6264-434e-9323-95ae19b26941) | ![Configuration Name](https://github.com/user-attachments/assets/1de4c480-73b6-4420-8402-a4085c3d87e1) |

| Click on the add button               | Perfect, you now have the necessary configuration 🥳               |
| ---------------------- | ---------------------- |
| ![Add Button](https://github.com/user-attachments/assets/e37fe196-93c1-411e-a703-9f2e0d38708b) | ![🥳](https://github.com/user-attachments/assets/c0f375ba-b5d1-4a97-a5e3-c9778eeb175d) |
  
</div>

### 🎨 Customize syntax highlighting colors

You can customize syntax highlighting colors in `Settings -> Syntax Keyword Highlighter`:

<div align="center">
  
  ![image](https://github.com/user-attachments/assets/7780619f-cbb8-42a4-bd7b-14a89cb0f542)
  
</div>

## 🚧 RoadMap
- [x] Complete migration from **Jetpack Compose Desktop** to **Swing**
- [x] Default settings for different assemblers
    - [x] Autocomplete
    - [x] Syntax Highlighting 
- [ ] Program customization
- [x] Smarter autocompletion
- [x] Terminal type selection
- [ ] Automatic compilation and execution ~ 🏗️
- [ ] Optimization
- [ ] 🚀 New release

## 📜 License
[Apache-2.0](/LICENSE)
