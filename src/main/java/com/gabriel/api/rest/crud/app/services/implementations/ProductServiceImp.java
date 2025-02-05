package com.gabriel.api.rest.crud.app.services.implementations;

import java.util.List;
import java.util.Optional;

import com.gabriel.api.rest.crud.app.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabriel.api.rest.crud.app.dtos.products.ProductDTO;
import com.gabriel.api.rest.crud.app.entities.Product;
import com.gabriel.api.rest.crud.app.repositories.ProductRepository;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired()
    private ProductRepository productRepository;


    @Override
    @Transactional( readOnly = true)
    public Optional<Product> findById( Long id ) {
        return this.productRepository.findById(id);
    }

    @Override
    @Transactional( readOnly = true)
    public List<Product> findAll() {
        return (List<Product>) this.productRepository.findAll();
    }

    @Override
    @Transactional()
    public Product save(ProductDTO product) {
        Product newProduct = new Product();
        newProduct.loadFromDTO(product);
        return this.productRepository.save(newProduct);
    }

    @Override
    @Transactional()
    public void delete(Product product ) {
        this.productRepository.delete(product);
    }

    @Override
    @Transactional()
    public Optional<Product> deleteById(Long id) {
        Optional<Product> productOpt = this.productRepository.findById(id);
        this.productRepository.deleteById(id);
        return productOpt;
    }

    @Override
    public Optional<Product> update(Long id, ProductDTO productDTO) {
        return this.productRepository.findById(id)
                                    .map(product -> {
                                        product.loadFromDTO(productDTO);
                                        return this.productRepository.save(product);
                                    });
    }
}
