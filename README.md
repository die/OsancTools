# OsancTools

OsancTools is a Java application containing various tools for making security work in the Oryx Sanctuary discord server much easier.

## Installation
> Note: This project is deprecated. Changes made by Deca make the program inoperable, such as the new FlatBuffer spritesheet changes. This repository exists to serve as an archive of the project.

Download the [latest release](https://github.com/Waifu/OsancTools/releases/latest) and run the executable.

## Build from Source

1. Open `build.gradle` with Intellij IDEA.
2. Add a configuration for the main function found in `com.github.waifu.gui.Main`
3. To build, run the shadowjar task.

This project uses Launch4J to build the jar, but to run the artifact without wrapping into an executable, use the command:
```bash
java -jar <artifact_name>
```

## Contributing

Feel free to submit pull requests that handle specific issues, and when in doubt feel free to contact me to see where you can help. While the code is open source, I ask that you please help contribute to the project rather than make a derivative if it is applicable.

## License

This repository is generally under the [Apache License 2.0](https://choosealicense.com/licenses/apache-2.0/), however certain directories will include a standalone license file that applies to the code in the current directory and its sub-directories.
