
package com.productManagement;

import com.productManagement.Product;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository class
 */
@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
}

