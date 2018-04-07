package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;

public class MainVerticle extends AbstractVerticle {

  private String userString;

  @Override
  public void start(Future<Void> startFuture) {
    HttpClient httpClient = vertx.createHttpClient();

  }

}
