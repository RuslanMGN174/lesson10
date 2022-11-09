package ru.knyazev.lesson10.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knyazev.lesson10.persist.Product;
import ru.knyazev.lesson10.persist.ProductRepository;
import ru.knyazev.lesson10.persist.ProductSpecification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findWithFilter(Optional<String> nameFilter,
                                        Optional<BigDecimal> minPrice,
                                        Optional<BigDecimal> maxPrice,
                                        Optional<Integer> size,
                                        Optional<String> sortField,
                                        Optional<String> sortOrder) {
        Specification<Product> spec = Specification.where(null);
        if (nameFilter.isPresent() && !nameFilter.get().isBlank()) {
            logger.info("Adding {} to filter", nameFilter.get());
            spec = spec.and(ProductSpecification.nameLike(nameFilter.get()));
        }
        if (minPrice.isPresent()) {
            logger.info("Adding {} to filter", minPrice.get());
            spec = spec.and(ProductSpecification.minPriceFilter(minPrice.get()));
        }
        if (maxPrice.isPresent()) {
            logger.info("Adding {} to filter", maxPrice.get());
            spec = spec.and(ProductSpecification.maxPriceFilter(maxPrice.get()));
        }
        if (sortField.isPresent() && sortOrder.isPresent()) {
            return productRepository.findAll(spec);
        }
        return productRepository.findAll(spec);
    }

    @Override
    public List<Product> findAll(Specification<Product> spec) {
        return productRepository.findAll(spec);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
