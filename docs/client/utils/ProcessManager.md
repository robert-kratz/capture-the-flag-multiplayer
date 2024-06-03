## ProcessManager Class Documentation

The `ProcessManager` class in the `de.cfp1.client.utils` package is designed to manage and control AI processes for a client application. It facilitates the starting, managing, and stopping of AI instances running from JAR files stored in a designated resources directory. This utility class significantly simplifies the interaction with external AI processes by abstracting complex operations into straightforward method calls.

### Constructor

- **ProcessManager()**: Initializes a new instance of the `ProcessManager`. It sets up the necessary environment for managing AI processes.

### Methods

#### Starting New Processes

- **startNewProcess(AiType aiType, boolean showGui, String args)**: Initiates a new AI process based on the specified AI type and additional arguments. The method handles the extraction of the AI executable from a JAR file, the setup of the process with or without a GUI, and the execution in a separate thread. It catches and handles `IOException` if the process cannot be started.

#### Stopping Processes

- **stopProcess(int index)**: Stops a specific AI process identified by its index in the list of currently managed threads. It safely interrupts the thread associated with the process.

- **stopAllProcesses()**: Terminates all running AI processes managed by the `ProcessManager`. This method iterates through all active threads and interrupts each one to ensure a clean shutdown of all resources.

#### Utility Methods

- **getProcesses()**: Returns a list of all threads currently managed by the `ProcessManager`. This allows external entities to monitor or interact with running processes.

- **getAiTypeString(AiType aiType)**: Converts an `AiType` enum value into a corresponding string that can be used in process arguments or for display purposes.

#### Private Helper Methods

- **extractJar(String resourcePath)**: Extracts a JAR file from a specified resource path within the project structure to a temporary file. This method is crucial for running isolated AI instances from JAR files that are bundled with the application or downloaded dynamically.

### Usage Scenario

This class is typically used in environments where multiple AI agents are required to operate concurrently, such as in testing environments or multiplayer gaming sessions where AI opponents are needed. It allows for dynamic management of AI resources without manual intervention in starting or stopping processes.

### Exception Handling

- The methods of `ProcessManager` handle various exceptions internally, particularly `IOException` during file operations and `InterruptedException` during process waiting. These exceptions are caught and logged, ensuring that the application remains stable even if individual AI processes encounter issues.

### Example

Here's an example of how to use the `ProcessManager` to start an AI process:

```java
ProcessManager manager = new ProcessManager();
try {
    manager.startNewProcess(AiType.EASY, false, "--exampleArg");
} catch (IOException e) {
    System.out.println("Error starting AI process: " + e.getMessage());
}
```

robert.kratz May 2, 2024
