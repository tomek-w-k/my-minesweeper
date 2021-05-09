# MyMinesweeper

![Custom badge](https://img.shields.io/static/v1.svg?label=Java&message=11&color=cadetblue)
![Custom badge](https://img.shields.io/static/v1.svg?label=Qt+Jambi&message=4.8.7&color=apple)

A clone of popular puzzle game. It allows to play in four pre - defined difficulty levels and in a custom mode - game area size and number of mines are defined by user. The game is also available in three languages: english, polish and german. 

<p align="center">
    <img src="https://user-images.githubusercontent.com/59183133/115570324-50527800-a2be-11eb-8fc2-ae6ba76dbe30.png"> 
</p>


## Detailed description
### Game modes
| Mode | Area size [field x field] | Mines |
| --- | --- | --- |
| Basic | 15 x 15 | 25 |
| Medium | 20 x 20 | 60 |
| Advanced | 30 x 30 | 130 |
| Expert | 30 x 40 | 200 |

In the **Custom** mode a game area cannot be smaller than 10 x 10 and bigger than 30 x 50 fields. Number of mines must be greather than one and smaller than a number of rows and columns product. 

### Preferences
**Preferences** window can be opened by clicking **Options -> Preferences**: 
<p align="center">
    <img width="33%"  src="https://user-images.githubusercontent.com/59183133/115422506-41f05780-a1fd-11eb-8c79-ec49043c9d1a.png">    
    <img width="33%" src="https://user-images.githubusercontent.com/59183133/115422573-53d1fa80-a1fd-11eb-9491-55dd914b5a9b.png">
    <img width="33%" src="https://user-images.githubusercontent.com/59183133/115422607-5af90880-a1fd-11eb-8ef9-73c99be8d1c8.png">
</p>

- **Startup**: Allows to specify which pre - defined mode should be chosen at game startup.
- **Language**: The user can choose a current language of the application GUI. Restart is not required. 
- **Other**: 
    - **Field size**: Specifies the size of fields in pixels. It can be useful while playing with higher difficulty levels (where the game area is bigger) on a small screens. For example, running an **Advanced** mode on a laptop screen (15.6'', 1366 x 768) with default field size (30 x 30 pixels) causes that the game window does not fit within the desktop. Changing field size to 20 x 20 pixels solves the problem.
    - **Time counting**: Enables a game time counting. If it's enabled - allows to specify whether the user shall be asked for save his result (if the best result will be achieved).

Clicking **Apply** or **Ok** causes the current game restart. 
All settings are stored and are available after application restart. 

### Best results
Best results can be viewed by clicking **Help -> Best results...** option:

<p align="center">
    <img src="https://user-images.githubusercontent.com/59183133/115571273-23529500-a2bf-11eb-9f0c-e42842cc3a16.png"> 
</p>

## Requirements
- JDK 11
- Maven 3.6

## Compilation & Lanuch
Although the game can be run both on Linux and Windows systems, the executable package has to be build separately for each operating system. 
<p align="center">
    <img src="https://user-images.githubusercontent.com/59183133/115407539-65f96c00-a1f0-11eb-8041-a4e87545e480.png">    
    <img src="https://user-images.githubusercontent.com/59183133/115407346-3c404500-a1f0-11eb-9f31-d8d1b959d775.png">
</p>

### Linux - Debian/Ubuntu
1. Download Qt Jambi archive `qtjambi-linux64-community-4.8.7.tar.gz` from https://sourceforge.net/projects/qtjambi/files/4.8.7.
2. In a directory where the archive is located, unpack it with command `tar -zxvf qtjambi-linux64-community-4.8.7.tar.gz` and then move to the directory `qtjambi-linux64-community-4.8.7`.
3. It is necessary to install `.jar` files with Qt Jambi library in a local Maven repository. There are two `.jar` files to install - do it with the following commands:
    ```
    mvn install:install-file -Dfile=qtjambi-4.8.7.jar -DgroupId=com.trolltech.qt -DartifactId=qtjambi -Dversion=4.8.7 -Dpackaging=jar      
    mvn install:install-file -Dfile=qtjambi-linux64-gcc-4.8.7.jar -DgroupId=com.trolltech.qt -DartifactId=qtjambi-linux64-gcc -Dversion=4.8.7 -Dpackaging=jar
    ```
4. Go to the MyMinesweeper project directory and build the project with `mvn package` command.
5. Run the application with `java -jar target/my-minesweeper-1.0-jar-with-dependencies.jar` command.

### Windows
1. Download Qt Jambi archive `qtjambi-community-4.8.7-win64-msvc2013.zip` from https://sourceforge.net/projects/qtjambi/files/4.8.7.
2. Unpack the archive, open the terminal and go to the unpacked directory - there should be two files, among other things: `qtjambi-4.8.7.jar` and `qtjambi-native-win64-msvc2013x64-4.8.7.jar`.
3. It is necessary to install `.jar` files with Qt Jambi library in a local Maven repository. There are two `.jar` files to install - do it with the following commands:
    ```
    mvn install:install-file -Dfile=qtjambi-4.8.7.jar -DgroupId=com.trolltech.qt -DartifactId=qtjambi -Dversion=4.8.7 -Dpackaging=jar      
    mvn install:install-file -Dfile=qtjambi-native-win64-msvc2013x64-4.8.7.jar -DgroupId=com.trolltech.qt -DartifactId=qtjambi-native-win64-msvc2013x64 -Dversion=4.8.7 -Dpackaging=jar
    ```   
4. Go to the MyMinesweeper project directory and build the project with `mvn -Denv=win64 package` command.
5. Run the application with `java -jar target/my-minesweeper-1.0-jar-with-dependencies.jar` command.
