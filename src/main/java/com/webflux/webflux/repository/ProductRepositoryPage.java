package com.webflux.webflux.repository;

import com.webflux.webflux.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface ProductRepositoryPage extends ReactiveSortingRepository<Product, String> {
    Flux<Product> findAllBy(Pageable pageable);
}
