## Class: DatabaseManager

**Package:** `de.cfp1.server.sql`

**Imports:**

- Gson (Google's JSON library)
- Various server entities and data structures
- Java SQL classes
- SLF4J logging framework

### Description

`DatabaseManager` extends `DatabaseHandler` and manages all database operations related to user and map data within the application. It provides methods for creating, updating, deleting, and querying user and map information stored in a SQL database.

### Constructor

#### DatabaseManager(String file)

Initializes a new `DatabaseManager` with a connection to the specified database file.

- **Parameters:**
  - `file`: Path to the database file.
- **Behavior:**
  - Connects to the database and ensures necessary tables are created.

### Methods

#### public synchronized PreparedStatement getPreparedStatement(String query)

Generates a `PreparedStatement` for the specified SQL query.

- **Parameters:**
  - `query`: SQL query string.
- **Returns:**
  - A `PreparedStatement` object ready for execution.

#### public synchronized User getUserById(String id)

Fetches a user from the database by their unique identifier.

- **Parameters:**
  - `id`: User's unique identifier.
- **Returns:**
  - A `User` object if found; otherwise `null`.

#### public synchronized User getUserByName(String name)

Retrieves a user by their username.

- **Parameters:**
  - `name`: Username of the user.
- **Returns:**
  - A `User` object if found; otherwise `null`.

#### public User getUserByEmailAddress(String email)

Fetches a user by their email address.

- **Parameters:**
  - `email`: Email address of the user.
- **Returns:**
  - A `User` object if found; otherwise `null`.

#### public synchronized User createUser(String name, String email, String password, boolean guest, boolean admin)

Creates a new user in the database.

- **Parameters:**
  - `name`: Username of the new user.
  - `email`: Email address of the new user.
  - `password`: Password for the new user.
  - `guest`: Whether the user is a guest.
  - `admin`: Whether the user has admin privileges.
- **Returns:**
  - A new `User` object if creation is successful; otherwise `null`.

#### public synchronized boolean deleteUser(String id)

Deletes a user from the database.

- **Parameters:**
  - `id`: Unique identifier of the user to be deleted.
- **Returns:**
  - `true` if the deletion was successful; otherwise `false`.

#### public synchronized boolean updateUser(String id, String username, String email, String password, boolean guest, boolean admin)

Updates an existing user's information.

- **Parameters:**
  - `id`: Unique identifier of the user.
  - `username`: New username.
  - `email`: New email address.
  - `password`: New password.
  - `guest`: Guest status.
  - `admin`: Admin status.
- **Returns:**
  - `true` if the update was successful; otherwise `false`.

#### public ArrayList<User> getUsers()

Retrieves all users from the database.

- **Returns:**
  - An `ArrayList` of `User` objects representing all users in the database.

#### public Map createMapTemplate(String userId, String name, String id, boolean isPublic)

Creates a new map template in the database.

- **Parameters:**
  - `userId`: ID of the user creating the map.
  - `name`: Name of the new map.
  - `id`: Unique identifier for the map.
  - `isPublic`: Whether the map is publicly accessible.
- **Returns:**
  - A `Map` object representing the newly created map template.

### Example Usage

```java
DatabaseManager dbManager = new DatabaseManager("path/to/database.db");
User user = dbManager.getUserById("123456");
```

robert.kratz May 2. 2024
