package com.example;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {

  private final RequestLine requestLine;

  public HttpRequest(BufferedReader br) throws IOException {
    this.requestLine = new RequestLine(br.readLine());
  }

  public boolean matchPath(String requestPath) {
    return requestLine.matchPath(requestPath);
  }

  public boolean isGetRequest() {
    return requestLine.isGetRequest();
  }

  public QueryStrings getQueryStrings() {
    return requestLine.getQueryStrings();
  }

}
