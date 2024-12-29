package com.webflux.webflux.controller;

import com.webflux.webflux.dto.ProductDTO;
import com.webflux.webflux.entity.Product;
import com.webflux.webflux.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*@RestController
@RequestMapping("/product")
@Slf4j*/
public class ProductController {


    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Flux<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<Product> getProductById(@PathVariable("id") int idProduct) {
        return productService.findProductById(idProduct);
    }

    @PostMapping
    public Mono<Product> postProduct(@RequestBody ProductDTO product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/{id}")
    public Mono<Product> updateProductById(@PathVariable("id") int idProduct, @RequestBody ProductDTO product) {
        return productService.updateProduct(product,idProduct);
    }


    @DeleteMapping("/{id}")
    public Mono<Void> deleteProductById(@PathVariable("id") int idProduct) {
        return productService.deleteProductById(idProduct);
    }


}
