package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  private String userString;

  @Override
  public void start(Future<Void> startFuture) {
    server().setHandler(ar ->{
      if(ar.succeeded()){
        startFuture.complete();
      }
      else
        startFuture.fail(ar.cause());
    });
  }

  private Future<Void> server(){
    Future<Void> serverFuture = Future.future();
    HttpServer httpServer = vertx.createHttpServer();

    Router router = Router.router(vertx);
    router.get("/").handler(this::doNothing);
    router.get("/getString").handler(this::getStringHandler);
    router.post().handler(BodyHandler.create());
    router.post("/update").handler(this::updateString);

    httpServer
      .requestHandler(router::accept)
      .listen(8080, ar ->{
        if (ar.succeeded()){
          System.out.println("Started successfully on port 8080");
          serverFuture.complete();
        }
        else
        {
          System.out.println("Failed due to "+ar.cause());
          serverFuture.fail(ar.cause());
        }
      });
    return serverFuture;
  }

  private void doNothing(RoutingContext routingContext) {
    routingContext.response().setStatusCode(200);
    routingContext.response().putHeader("Content-Type", "application/json");
    routingContext.response().end(new JsonObject().encodePrettily());
  }

  private void updateString(RoutingContext routingContext) {
    JsonObject getObject = routingContext.getBodyAsJson();
    userString = getObject.getString("string");
    routingContext.response().setStatusCode(201);
    routingContext.response().putHeader("Content-Type", "application/json");
    routingContext.response().end(new JsonObject().put("success", true).encode());
  }

  private void getStringHandler(RoutingContext routingContext) {
    JsonObject jsonObject = new JsonObject().put("string", userString);
    routingContext.response().setStatusCode(200);
    routingContext.response().putHeader("Content-Type", "application/json");
    routingContext.response().end(jsonObject.encodePrettily());
  }

}
