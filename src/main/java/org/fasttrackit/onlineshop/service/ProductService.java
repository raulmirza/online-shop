package org.fasttrackit.onlineshop.service;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persistance.ProductRepository;

import org.fasttrackit.onlineshop.transfer.GetProductsRequest;
import org.fasttrackit.onlineshop.transfer.SaveProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    //IoC (Inversion of Control)
    private final ProductRepository productRepository;

    // Dependency Injection
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(SaveProductRequest request){

        LOGGER.info("Creating product {}", request);
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImageUrl(request.getImageUrl());

         return productRepository.save(product);
    }

    public Product getProduct(long id){
        LOGGER.info("Retrieving product {}, id");

//        final Optional<Product> productOptional = productRepository.findById(id);
//
//        if (productOptional.isPresent()) {
//            return  productOptional.get();
//        } else {
//            throw new ResourceNotFoundException("Product " + id + " Not Found..");
//        }
        return productRepository.findById(id)
                // Lambda expression
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " Not Found.."));
    }

    public Page<Product> getProducts(GetProductsRequest request, Pageable pageable) {
        if (request.getPartialName() != null && request.getMinimumQuantity() != null) {

         return  productRepository.findByNameContainingAndQuantityGreaterThanEqual(
                    request.getPartialName(), request.getMinimumQuantity(), pageable);

        } else if (request.getPartialName() != null) {

            return productRepository.findByNameContaining(request.getPartialName(), pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    public Product updateProduct(long id, SaveProductRequest request){
        LOGGER.info("Updating product {} {}", id, request);

        final Product product = getProduct(id);

        BeanUtils.copyProperties(request, product);

        return productRepository.save(product);
    }

    public void deleteProduct(long id){
        LOGGER.info("Deleting product {}, id");
        productRepository.deleteById(id);
    }
}
