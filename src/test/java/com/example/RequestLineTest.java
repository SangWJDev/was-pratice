package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class RequestLineTest {

  @Test
  void create() {
    RequestLine requestLine = new RequestLine("GET /calculate?operand1=11&operator=*&operand=55 HTTP/1.1");

    assertThat(requestLine).isNotNull();
    assertThat(requestLine).isEqualTo(new RequestLine("GET", "/calculate", "operand1=11&operator=*&operand=55"));
  }

}
