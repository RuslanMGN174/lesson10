package ru.knyazev.lesson10.service;

import org.springframework.data.jpa.domain.Specification;
import ru.knyazev.lesson10.persist.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findWithFilter(Optional<String> nameFilter,
                                 Optional<BigDecimal> minPrice,
                                 Optional<BigDecimal> maxPrice,
                                 Optional<Integer> size,
                                 Optional<String> sortField,
                                 Optional<String> sortOrder);

    List<Product> findAll(Specification<Product> spec);

    Optional<Product> findById(Long id);

    void save(Product product);

    void deleteById(Long id);
}
