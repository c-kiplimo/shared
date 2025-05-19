package com.collicode.shared.domain.router;

public class Router<E, R> {

    public static <E, R> RouteBuilder<E, R> route(String routeId) {
        return RouteBuilder.newRoute(routeId);
    }
}
