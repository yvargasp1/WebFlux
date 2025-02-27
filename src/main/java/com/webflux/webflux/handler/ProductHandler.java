package com.webflux.webflux.handler;

import com.webflux.webflux.dto.ProductDTO;
import com.webflux.webflux.entity.Product;
import com.webflux.webflux.service.ProductService;

import com.webflux.webflux.validation.ObjectValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductService productService;
    private final ObjectValidation objectValidation;



    public Mono<ServerResponse> getAllProducts(ServerRequest serverRequest) {
        Flux<Product> productsFlux = productService.getAllProducts();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(productsFlux, Product.class);
    }

    public Mono<ServerResponse> getAllProductsPage(ServerRequest serverRequest) {
        int page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(10);

        Flux<Product> productsFlux = productService.getAllProductPage(page,size);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(productsFlux, Product.class);
    }

    public Mono<ServerResponse> getOneProduct(ServerRequest serverRequest) {
        int id = Integer.parseInt(serverRequest.pathVariable("id"));
        Mono<Product> productMono = productService.findProductById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(productMono, Product.class);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Mono<ServerResponse> saveProduct(ServerRequest serverRequest) {
        Mono<ProductDTO> productMono = serverRequest.bodyToMono(ProductDTO.class).doOnNext(objectValidation::validate);
        return productMono.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(productService.saveProduct(p), Product.class));
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {
        int id = Integer.parseInt(serverRequest.pathVariable("id"));
        Mono<ProductDTO> productMono = serverRequest.bodyToMono(ProductDTO.class).doOnNext(objectValidation::validate);
        return productMono.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(productService.updateProduct(p, id), Product.class));
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
        int id = Integer.parseInt(serverRequest.pathVariable("id"));
        Mono<Product> productMono = productService.findProductById(id);
        return productMono.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(productService.deleteProductById(id), Product.class));
    }

}
