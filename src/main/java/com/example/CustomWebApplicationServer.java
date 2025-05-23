package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomWebApplicationServer {

  private final int port;

  private final ExecutorService executorService = Executors.newFixedThreadPool(10);

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
         * Step3 - Thread Pool을 적용해 안정적인 서비스가 가능하도록 한다.
         */
        executorService.execute(new ClientRequestHandler(clientSocket));
      }
    }
  }
}
