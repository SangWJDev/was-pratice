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

      while ((clientSocket = serverSocket.accept()) != null) {
        logger.info("[CustomWebApplicationServer] Accepted connection from {}",
            clientSocket.getRemoteSocketAddress());

        /**
         * Step2 - 사용자 요청이 들어올 때마다 Thread를 새로 생성해서 사용자 요청을 처리하도록 한다.
         * 문제점: 요청이 들어올 때마다 스레드를 생성하면 스레드가 많아지고 -> cpu contextSwitching이 빈번해진다.
         * cpu 사용량, 메모리 사용량 증가
         * 해결방법: 사용자 요청이 들어올 때마다 생성하는 게 아니라 스레드 pool을 적용해 안정적인 서비스가 가능하도록 개선.
         */
        new Thread(new ClientRequestHandler(clientSocket)).start();
      }
    }
  }
}
