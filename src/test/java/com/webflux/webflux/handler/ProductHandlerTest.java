package com.webflux.webflux.handler;

import com.webflux.webflux.dto.ProductDTO;
import com.webflux.webflux.entity.Product;
import com.webflux.webflux.repository.ProductRepository;
import com.webflux.webflux.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class ProductHandlerTest {
    @Mock
    private ProductRepository productRepository;
    private ProductService productService;


    @BeforeEach
    void setUp(){
        productService = new ProductService(productRepository);
    }

    @Test
    void testGetProducts(){
        Product product = new Product(1,"Pan",1000);
        Product product2 = new Product(2,"Pan Ajo",1000);

        Mockito.when(productRepository.findAll()).thenReturn(Flux.just(product,product2));
        StepVerifier.create(productService.getAllProducts())
                .expectNext(product)
                .expectNext(product2)
                .verifyComplete();
    }

    @Test
    void testGetProduct(){
        Product product = new Product(1,"Pan",1000);
        Mockito.when(productRepository.findById(1)).thenReturn(Mono.just(product));
        StepVerifier.create(productService.findProductById(1))
                .expectNext(product)
                .verifyComplete();
    }

    @Test
    void testSaveProduct(){
        ProductDTO productDTO =  new ProductDTO("Chocolates",1);
        Product productSave = Product.builder().name(productDTO.getName()).price(productDTO.getPrice()).build();
        Mockito.when(productRepository.findByName(productDTO.getName())).thenReturn(Mono.empty());  // Producto no existe
        Mockito.when(productRepository.save(productSave)).thenReturn(Mono.just(productSave));

        StepVerifier.create(productService.saveProduct(productDTO))
                .expectNext(productSave)
                .verifyComplete()
        ;
    }
}
