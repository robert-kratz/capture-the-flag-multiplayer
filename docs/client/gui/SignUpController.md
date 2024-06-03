# SignUpController Documentation

## SignUpController.java

This class handles the GUI logic for the sign-up process in the application. It includes methods for:

## Key Methods

### Initialization

`initialize(URL location, ResourceBundle resources)`: This method is called after the scene is loaded. It comes from the Initializable interface.

### User Registration

`signUp()`: This method is responsible for the sign-up process. It validates the user input and communicates with the server to create a new user account.

### Error Handling

`clearAllErrorMessages()`: This method clears all error messages displayed on the sign-up form.

### Navigation

`cancelSignUp()`: This method is used to navigate back to the login scene.

## Attributes

`stage`: This is the current stage where the sign-up form is displayed.

`sceneController`: This is an instance of the SceneController class used to control scene transitions and play audio.

`passwordSignUpField`, `passwordReconfirmSignUpField`, `password_field_popUp`: These are the password fields in the sign-up form.

`userSignUpField`, `emailSignUpField`: These are the username and email fields in the sign-up form.

`usernameExceptionLabel`, `passExceptionLabel`, `reconfirmPassExceptionLabel`: These are the labels used to display error messages related to the username and password fields.

## Usage in Game Flow

The SignUpController is used when a new user wants to create an account. After the user fills in the sign-up form and clicks the sign-up button, the `signUp()` method is called. If the sign-up process is successful, the user is redirected to the login scene. If there are any errors during the sign-up process, appropriate error messages are displayed. The user can also choose to cancel the sign-up process and navigate back to the login scene by clicking the cancel button.

robert.kratz May 2. 2024
