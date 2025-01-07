package com.webflux.webflux.rooter;

import com.webflux.webflux.handler.ProductHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class ProductRouter {

    private static final String PATH = "product";

    @Bean
    RouterFunction<ServerResponse> routerFunction(ProductHandler productHandler) {
        return RouterFunctions.route()
                .GET(PATH, productHandler::getAllProducts)
                .GET(PATH + "/{id}", productHandler::getOneProduct)
                .POST(PATH, productHandler::saveProduct)
                .PUT(PATH + "/{id}", productHandler::updateProduct)
                .DELETE(PATH + "/{id}", productHandler::deleteProduct).build();


    }

}
