# LoginController Documentation

## LoginController.java

This class handles the GUI logic for the login process of the game. It includes methods for user login, guest sign up, navigation to the sign up scene, and error handling.

## Key Methods

### Initialization

`initialize(URL location, ResourceBundle resources)`: This method is called after the scene is loaded. It comes from the Initializable interface.

### User Login

`logInAsUser()`: This method is used to log in a user. It takes the user's input from the login fields, sends a login request to the server, and handles any exceptions that may occur during the process.

### Guest Sign Up

`signUpAsGuest()`: This method is used to sign up a guest user. It sends a sign up request to the server, updates the user profile text, and handles any exceptions that may occur during the process.

### Navigation

`navigateToSignUp()`: This method is used to navigate to the sign up scene when the "Sign Up" button is clicked.

### Error Handling

`clearAllErrorMessages()`: This method is used to clear all error messages from the login scene.

## Attributes

`stage`: This attribute represents the stage that the login scene is displayed on.

`sceneController`: This attribute is an instance of the SceneController class, which is used to control the behavior of the scenes.

`userLogInField`, `userSignUpField`, `passwordLogInField`: These attributes represent the text fields where the user enters their login credentials.

`logInButton`: This attribute represents the button that the user clicks to log in.

`passExceptionLabel`, `userExceptionLabel`: These attributes represent the labels where error messages are displayed.

## Usage in Game Flow

The LoginController is used at the start of the game flow. When the game is launched, the user is presented with the login scene. The user can choose to log in with their credentials, sign up as a guest, or navigate to the sign up scene to create a new account. If any errors occur during the login or sign up process, they are displayed on the login scene.

robert.kratz May 2. 2024
