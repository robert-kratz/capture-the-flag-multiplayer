# Capture The Flag

This project is a Capture The Flag (CTF) game developed for the course "Software Engineering" at the University of Mannheim. The game is divided into three components: the server, the client and the ai client. The server is responsible for managing the game, the client is responsible for the user interface and the ai client is responsible for the artificial intelligence of the game.

## Authors and acknowledgment

- Virgil Baclanov [https://itsgil.com](https://itsgil.com) (virgil.baclanov@students.uni-mannheim.de)
- Robert J. Kratz [https://rjks.us](https://rjks.us) (robert.kratz@students.uni-mannheim.de)
- Benjamin Sander [Linkedin](https://www.linkedin.com/in/benjamin-sander-17b398289/) (benjamin.sander@students.uni-mannheim.de)
- Joel Bakirel (joel.bakirel@students.uni-mannheim.de)
- Juan Steppacher (juan.steppacher@students.uni-mannheim.de)
- Gabriel Himmelein [Website](https://gabrielhimmelein.com) (gabriel.victor.arthur.himmelein@students.uni-mannheim.de)

## Index

- [Documentation](#documentation)
- [Installation](#installation)
  - [macOS installation](#macos-installation)
  - [Linux installation](#linux-installation)
  - [Windows installation](#windows-installation)
- [Process Arguments](#process-arguments)
- [Authors and acknowledgment](#authors-and-acknowledgment)

## Documentation

To learn more about the project, please refer to the [documentation](/docs/README.md).

**Important:** All client tests are disabled by default. The reason for this is that the client module relies on the server module to be running. To enable the tests, you need to start the server module first. For this reason, the client tests are disabled for pipeline runs.

## Installation

### macOS installation:

1. Start the server:

```bash
$ java -jar server-1.0-SNAPSHOT-exec.jar
```

2. Start a client:

```bash
$  java -jar client-1.0-SNAPSHOT.jar
```

3. Start the AI client:

```bash
$ java -jar ai-1.0-SNAPSHOT.jar
```

### Linux installation:

1. Download the Linux JavaFX module from our repository [here](https://swt-praktikum.informatik.uni-mannheim.de/cfp/cfp1/-/blob/main/utilities/openjfx-22.0.1_linux-x64_bin-sdk.zip?ref_type=heads).

2. Unzip the file.

3. Start the server:

```bash
$ java -jar server-1.0-SNAPSHOT-exec.jar
```

4. Start a client by importing the JavaFX module present in the .zip
   archive (downloaded in step 1):

```bash
$  java --module-path <path to JavaFX module lib> --add-modules javafx.controls,javafx.fxml -jar client-1.0-SNAPSHOT.jar
```

**Example**

```bash
$ /home/peitscha/.jdks/openjdk-21.0.2/bin/java --module-path/home/peitscha/Downloads/openjfx-22.0.1_linux-x64_bin-sdk/javafx-sdk-22.0.1/lib --add-modules javafx.controls,javafx.fxml -jar client-1.0-SNAPSHOT.jar
```

5. Start the AI client by importing the JavaFX module present in the
   .zip archive (downloaded in step 1):

```bash
$ java --module-path <path to JavaFX module lib> --add-modules javafx.controls,javafx.fxml -jar ai-1.0-SNAPSHOT.jar
```

**Example**

```bash
$ /home/peitscha/.jdks/openjdk-21.0.2/bin/java --module-path /home/peitscha/Downloads/openjfx-22.0.1_linux-x64_bin-sdk/javafx-sdk-22.0.1/lib --add-modules javafx.controls,javafx.fxml -jar ai-1.0-SNAPSHOT.jar
```

### Windows installation:

1. Download the Linux JavaFX module from our repository [here](https://swt-praktikum.informatik.uni-mannheim.de/cfp/cfp1/-/blob/main/utilities/openjfx-21.0.2_windows-x64_bin-sdk.zip?ref_type=heads).

2. Unzip the file.

3. Start the server:

```bash
$ java -jar server-1.0-SNAPSHOT-exec.jar
```

4. Start a client by importing the JavaFX module present in the .zip
   archive (downloaded in step 1):

```bash
$  java --module-path <path to JavaFX module lib> --add-modules javafx.controls,javafx.fxml -jar client-1.0-SNAPSHOT.jar
```

**Example**

```bash
$ java --module-path C:\Users\virgi\Downloads\openjfx-21.0.2_windows-x64_bin-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.fxml -jar client-1.0-SNAPSHOT.jar
```

5. Start the AI client by importing the JavaFX module present in the
   .zip archive (downloaded in step 1):

```bash
$ java --module-path <path to JavaFX module lib> --add-modules javafx.controls,javafx.fxml -jar ai-1.0-SNAPSHOT.jar
```

**Example**

```bash
$  java --module-path C:\Users\virgi\Downloads\openjfx-21.0.2_windows-x64_bin-sdk\javafx-sdk-21.0.2\lib --add-modules javafx.controls,javafx.fxml -jar ai-1.0-SNAPSHOT.jar
```

## Process Arguments:

To connect the client and the ai to a different server, you can pass the server address as an argument. The default server address is `localhost`, the default port is `:8888`.

Start the Client client with a different server address:

```bash
$ java -jar client-1.0-SNAPSHOT.jar --url http://cfp1.rjks.us
```

Start the AI client with a different server address:

```bash
$ java -jar ai-1.0-SNAPSHOT.jar --url http://cfp1.rjks.us
```
