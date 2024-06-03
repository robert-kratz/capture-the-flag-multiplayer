## RequestBuilder Class Documentation

The `RequestBuilder` class is designed to facilitate HTTP requests by providing a flexible and intuitive API for building and sending requests to a specified URL. It manages request configurations such as HTTP methods, headers, payloads, and authentication tokens, simplifying the process of setting up HTTP connections.

### Overview

The class encapsulates all necessary aspects of setting up a connection, sending a request, and handling the response. It supports method chaining to streamline the process of configuring a request, making the class both powerful and easy to use for developers.

### Key Methods

#### Configuration

- **constructor(String url)**: Initializes a new instance of the `RequestBuilder` with a specific URL. The URL represents the endpoint to which the HTTP request will be sent.

- **method(String method)**: Sets the HTTP method for the request (e.g., GET, POST). This method returns the `RequestBuilder` instance for chaining further configurations.

- **payload(String payload)**: Assigns a payload to the HTTP request, which is essential for methods like POST or PUT that send data to the server. Returns the `RequestBuilder` for further chaining.

- **useAuth(String token)**: Includes an authentication token in the request headers, typically used for securing API access with bearer tokens.

- **addHeader(String key, String value)**: Adds a custom header to the request. This can be used for specifying content types, language preferences, or other custom data.

- **contentTypeJson()**: Adds a header to specify that the content type of the request payload is JSON. This is a convenience method for setting the `Content-Type` header to `application/json`.

#### Execution

- **send()**: Executes the configured HTTP request and returns the response as a string. This method handles the connection setup, sends the request, and processes the response. It can throw exceptions related to I/O errors or if the request results in an error response code.

#### Utility Methods

- **getResponseCode()**: After sending a request, this method can be used to retrieve the HTTP response code of the executed request, providing insight into whether the request was successful (e.g., 200 OK) or resulted in errors (e.g., 404 Not Found).

- **getHttpURLConnection()**: Configures and returns a `HttpURLConnection` object based on the current settings of the `RequestBuilder`. This includes setting the request method, adding headers, and handling the payload.

- **getResponseBody(InputStream stream)**: Reads an input stream from a connection and returns its contents as a string. This method is used internally to extract the response from the server.

### Exception Handling

- **RequestErrorException**: Thrown by the `send()` method if the HTTP response code indicates a failure (any code other than 200 OK or 201 Created). The exception contains the error message and the response code to help diagnose the issue.

### Usage Example

Here is a simple example demonstrating how to use the `RequestBuilder` to send a POST request with JSON data:

```java
String url = "http://example.com/api/data";
String jsonPayload = "{\"name\":\"John\", \"age\":30}";

RequestBuilder builder = new RequestBuilder(url)
    .method("POST")
    .contentTypeJson()
    .payload(jsonPayload)
    .useAuth("your_auth_token");

try {
    String response = builder.send();
    System.out.println("Response: " + response);
} catch (IOException e) {
    System.out.println("Network error: " + e.getMessage());
} catch (RequestErrorException e) {
    System.out.println("Failed request: " + e.getMessage() + " Code: " + e.getResponseCode());
}
```

### Security Considerations

It's crucial to handle the authentication token securely, ensuring it is not exposed or logged inappropriately. Additionally, input validation should be performed on any data used to construct requests to prevent injection attacks or other malicious activities.
