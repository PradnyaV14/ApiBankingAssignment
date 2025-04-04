
package com.productManagement;

import com.productManagement.Product;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class
 */
@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
}

