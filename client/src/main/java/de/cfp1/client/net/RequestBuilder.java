package de.cfp1.client.net;

import de.cfp1.server.exception.RequestErrorException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author robert.kratz
 */

public class RequestBuilder {

  private static final String USER_AGENT = "Mozilla/5.0";

  private String url;
  private String method;
  private String payload;
  private List<String> headers = new ArrayList<>();

  private String authToken;

  private int responseCode;

  /**
   * Constructor for RequestBuilder
   *
   * @param url The URL to send the request to
   * @author robert.kratz
   */
  public RequestBuilder(String url) {
    this.url = url;
  }

  /**
   * Set the request method (e.g., GET, POST)
   *
   * @param method The request method to use (e.g., GET, POST)
   * @return The RequestBuilder object for method chaining
   * @author robert.kratz
   */
  public RequestBuilder method(String method) {
    this.method = method;
    return this;
  }

  /**
   * Set the payload for the request
   *
   * @param payload The payload to send with the request
   * @return The RequestBuilder object for method chaining
   * @author robert.kratz
   */
  public RequestBuilder payload(String payload) {
    this.payload = payload;
    return this;
  }

  /**
   * Use the given token for authentication
   *
   * @param token The token to use for authentication
   * @return The RequestBuilder object for method chaining
   * @author robert.kratz
   */
  public RequestBuilder useAuth(String token) {
    this.authToken = token;
    return this;
  }

  /**
   * Add a header to the request
   *
   * @param key   key value
   * @param value value
   * @return The RequestBuilder object for method chaining
   * @author robert.kratz
   */
  public RequestBuilder addHeader(String key, String value) {
    headers.add(key + ":" + value);
    return this;
  }

  /**
   * Add the content type header to the request
   *
   * @return The RequestBuilder object for method chaining
   * @author robert.kratz
   */
  public RequestBuilder contentTypeJson() {
    this.headers.add("Content-Type:application/json");
    return this;
  }

  public int getResponseCode() {
    return responseCode;
  }

  /**
   * Send the request and return the response
   *
   * @return The response as a string
   * @throws IOException                                    if an I/O exception occurs
   * @throws de.cfp1.server.exception.TokenInvalidException if the token is invalid
   * @throws de.cfp1.server.exception.RequestErrorException if the request fails
   * @author robert.kratz
   */
  public String send() throws IOException, RequestErrorException {
    HttpURLConnection con = getHttpURLConnection();

    int responseCode = con.getResponseCode();
    System.out.println(
        "Sending '" + method + "' request to URL : " + url + " with payload: " + payload
            + " and response code: " + responseCode + " and response message: "
            + con.getResponseMessage());

    this.responseCode = responseCode;

    //get request body
    String response = getResponseBody(
        con.getErrorStream() != null ? con.getErrorStream() : con.getInputStream());

    if (responseCode != 200 && responseCode != 201) {
      throw new RequestErrorException(response, responseCode);
    }
    ;

    return response;
  }

  /**
   * Get the HttpURLConnection object with all the necessary settings
   *
   * @return HttpURLConnection object
   * @throws IOException if an I/O exception occurs
   * @author robert.kratz
   */
  private HttpURLConnection getHttpURLConnection() throws IOException {
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    // Set request method (e.g., GET, POST)
    con.setRequestMethod(method);

    // Add request header
    con.setRequestProperty("User-Agent", USER_AGENT);
    if (authToken != null) {
      // Add auth token to request header
      con.setRequestProperty("Authorization", "Bearer " + authToken);
    }

    // Apply all additional headers
    for (String header : headers) {
      String[] headerParts = header.split(":", 2);
      if (headerParts.length == 2) {
        con.setRequestProperty(headerParts[0].trim(), headerParts[1].trim());
      }
    }

    if (payload != null) {
      // Enable output for request body
      con.setDoOutput(true);
      try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
        wr.writeBytes(payload);
        wr.flush();
      }
    }
    return con;
  }

  /**
   * Get the response body from the input stream
   *
   * @param stream The input stream to read the response from
   * @return The response body as a string
   * @throws IOException if an I/O exception occurs
   * @author robert.kratz
   */
  private String getResponseBody(InputStream stream) throws IOException {
    StringBuilder response;
    try (BufferedReader in = new BufferedReader(new InputStreamReader(stream))) {
      String inputLine;
      response = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
    }
    return response.toString();
  }
}
