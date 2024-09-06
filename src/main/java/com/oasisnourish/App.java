package com.oasisnourish;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        var app = Javalin.create(/* config */);
        app.start(7070);
        app.get("/", ctx -> ctx.result("Hello World"));
    }
}