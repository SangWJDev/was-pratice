package com.example;

import com.example.caculation.domain.Calculator;
import com.example.caculation.domain.PositiveNumber;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomWebApplicationServer {

  private final int port;

  private static final Logger logger = LoggerFactory.getLogger(CustomWebApplicationServer.class);

  public CustomWebApplicationServer(int port) {
    this.port = port;
  }

  public void start() throws IOException {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      logger.info("[CustomWebApplicationServer] Listening on port {}", port);

      Socket clientSocket;
      logger.info("[CustomWebApplicationServer] Waiting for client connection...");

      while((clientSocket = serverSocket.accept()) != null) {
        logger.info("[CustomWebApplicationServer] Accepted connection from {}", clientSocket.getRemoteSocketAddress());

        /**
         * Step1 - 사용자 요청용 메인 Thread가 처리되도록 한다.
         *
         */

        try (InputStream in = clientSocket.getInputStream(); OutputStream out = clientSocket.getOutputStream()){
          BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
          DataOutputStream dos = new DataOutputStream(out);

          HttpRequest httpRequest = new HttpRequest(br);

          if (httpRequest.isGetRequest() && httpRequest.matchPath("/calculate")) {
            QueryStrings queryStrings = httpRequest.getQueryStrings();

            int operand1 = Integer.parseInt(queryStrings.getValue("operand1"));
            String operator = queryStrings.getValue("operator");
            int operand2 = Integer.parseInt(queryStrings.getValue("operand2"));

            int result = Calculator.calculate(new PositiveNumber(operand1), operator, new PositiveNumber(operand2));
            byte[] body = String.valueOf(result).getBytes();

            HttpResponse response = new HttpResponse(dos);
            response.response200Header("application/json", body.length);
            response.responseBody(body);
          }

        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }

  }

}
