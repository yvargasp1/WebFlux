package com.webflux.webflux.service;

import com.webflux.webflux.dto.ProductDTO;
import com.webflux.webflux.entity.Product;
import com.webflux.webflux.exception.CustomException;
import com.webflux.webflux.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private static final String NOT_FOUND = "Product not found.";
    private static final String FOUND = "Product found, same name.";


    public Flux<Product> getAllProducts() {
        return productRepository.findAll().switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND , NOT_FOUND)));
    }

    public Mono<Product> findProductById(int idProduct) {
        return productRepository.findById(idProduct)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND,NOT_FOUND)));

    }

    public Mono<Product> saveProduct(ProductDTO product) {
        try {
            Product productDTOSave = Product.builder().name(product.getName()).price(product.getPrice()).image(product.getImage()).build();
            return productRepository.findByName(product.getName()).flatMap(
                    product1 -> {
                        if (product1.getName().equals(product.getName())) {
                            return Mono.error(new CustomException(HttpStatus.CONFLICT, FOUND));
                        }
                        return productRepository.save(productDTOSave);
                    }
            ).switchIfEmpty(productRepository.save(productDTOSave));
        } catch (Exception e) {
            return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    public Mono<Product> updateProduct(ProductDTO product, int idProduct) {
        Mono<Boolean> productId = productRepository.findById(idProduct).hasElement();
        Mono<Product> productRepeatedName = productRepository.repeatedName(idProduct, product.getName());
        Product productDTOSave = Product.builder().id(idProduct).name(product.getName()).price(product.getPrice()).build();

        return productId.flatMap(exist -> exist ?
                productRepeatedName.flatMap(
                        existName -> {
                            if(existName.getName().equals(product.getName()) ){
                               return Mono.error(new CustomException(HttpStatus.CONFLICT, FOUND + " Already exist"));
                            }
                                 return   productRepository.save(productDTOSave);
                        }
                ).switchIfEmpty( productRepository.save(productDTOSave))
                :
                Mono.error(new CustomException(HttpStatus.NOT_FOUND, NOT_FOUND))
        )
                ;
    }

    public Mono<Void> deleteProductById(int idProduct) {
        Mono<Boolean> productId = productRepository.findById(idProduct).hasElement();

        return productId.flatMap(product -> product ? productRepository.deleteById(idProduct) :
                Mono.error(new CustomException(HttpStatus.NOT_FOUND, NOT_FOUND))
        );

    }


}
