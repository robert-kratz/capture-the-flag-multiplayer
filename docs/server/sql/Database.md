## Database Schema Documentation

This documentation describes the schema for three key tables in the database: `users`, `maps`, and `stats`. Each table serves distinct purposes in storing data about users, their maps, and gameplay statistics.

### Table: users

Stores data about each user, including their identification, credentials, and roles.

- **userid** (`VARCHAR(36)`): A unique identifier for the user. This is the primary key.
- **username** (`VARCHAR(16)`): The user's username, used for login and display. This field is expected to be unique.
- **password** (`VARCHAR(64)`): The hashed password for the user's account.
- **email** (`VARCHAR(64)`): The user's email address. This field is also expected to be unique.
- **guest** (`BOOLEAN`): Indicates whether the user is a guest (true) or a registered member (false).
- **admin** (`BOOLEAN`): Indicates whether the user has administrative privileges.

**Relationships:**

- One user can have multiple maps.
- One user has one stats record.

### Table: maps

Stores information about maps created by users.

- **mapid** (`VARCHAR(36)`): A unique identifier for the map. This is the primary key.
- **maptemplate** (`VARCHAR(64)`): A JSON string or reference to a template used for the map configuration.
- **ispublic** (`BOOLEAN`): Flag to determine whether the map is available publicly.
- **name** (`VARCHAR(64)`): The name of the map, as defined by the user.
- **userid** (`VARCHAR(36)`): The identifier of the user who created the map. This is a foreign key linking to the `users` table.
- **created** (`TIMESTAMP`): The timestamp when the map was created.
- **lastmodified** (`TIMESTAMP`): The timestamp when the map was last modified.

**Relationships:**

- Each map is associated with exactly one user.

### Table: stats

Stores statistics related to users' gameplay.

- **userid** (`VARCHAR(36)`): The identifier of the user whose statistics are being recorded. This is the primary key and a foreign key linking to the `users` table.
- **wins** (`INT`): The number of games the user has won.
- **losses** (`INT`): The number of games the user has lost.
- **draws** (`INT`): The number of games that have ended in a draw.
- **elo** (`INT`): The Elo rating of the user, used for ranking and matchmaking purposes.

**Relationships:**

- Each stats record is associated with exactly one user.

### Relationships Overview

- **1 user - n maps**: One user can create multiple maps, but each map is created by only one user.
- **1 map - 1 user**: Each map is uniquely associated with one user.
- **1 user - 1 stats**: Each user has exactly one stats record corresponding to their gameplay.
- **1 stats - 1 user**: Each stats record is uniquely associated with one user.

This schema forms the foundational data structure for the application, facilitating user management, map sharing, and tracking gameplay statistics. Adjustments and extensions can be made based on further requirements or changes in application functionality.

robert.kratz May 2. 2024
