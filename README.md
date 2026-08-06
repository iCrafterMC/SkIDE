# SkIDE

SkIDE is an integrated development environment (IDE) for the [Skript](https://github.com/SkriptLang/Skript) language. Skript is a plugin for Minecraft multiplayer servers that allows easy scripting of server modifications. The IDE provides auto-completion, syntax highlighting, a web technology based editor and many more things.

This project is a fork of the original [Sk-IDE](https://github.com/liz3/SkIDE) by liz3.

# Requirements
 - Java Development Kit (JDK) version 26

# Building
```cmd
> gradlew shadowJar
```
or
```sh
$ ./gradlew shadowJar
```
This will output the runnable jar file to `build/libs`.

# Running
```cmd
> gradlew run
```

# Libraries, Frameworks and APIs in use
 - [Kotlin](http://kotlinlang.org/) - Programming language
 - [JavaFX](https://openjdk.java.net/projects/openjfx) - GUI framework
 - [ControlsFX](http://fxexperience.com/controlsfx/) - JavaFX addon for advanced components
 - [Monaco](https://microsoft.github.io/monaco-editor/) - Code editor based on web technologies
 - [TerminalFX](https://github.com/javaterminal/TerminalFX) - JavaFX terminal view
 - [Skript Hub](http://skripthub.net/) - Auto-completion provider
 - [skUnity](http://skunity.com/) - Integration with forums.skunity.com
 - [SkriptTools](https://skripttools.net/) - Skript binary meta data
 - [Gradle](https://gradle.org/) - Build system
 - [JSON](https://www.json.org/json-en.html) - JSON implementation for Java
 - [Jsch](https://github.com/mwiede/jsch) - SSH implementation for Java
 - [Apache Commons Net](https://commons.apache.org/proper/commons-net/) - FTP implementation for Java

# License
This project is licensed under the GNU General Public License v2.0.
