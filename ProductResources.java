package com.productManagement;

import com.productManagement.Product;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import com.productManagement.ProductRepository;

import java.util.List;

/**
 * Resource file to perform CRUD operations.Can be tested via postman.
 */
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResources {

	@Inject
	ProductRepository productRepository;

	// To add a new product
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public Response createProduct(Product product) {
		System.out.println("ADDING PRODUCT: " + product);
		productRepository.persist(product);
		return Response.status(Response.Status.CREATED).entity(product).build();
	}

	// Get all products
	@GET
	public List<Product> getAllProducts() {
		List<Product> products = productRepository.listAll();
		System.out.println("FETHING ALL PRODUCTS");
		for (Product product : products) {
			System.out.println(product);
		}
		return products;
	}

	// Get a product by its id
	@GET
	@Path("/{id}")
	public Response getProductById(@PathParam("id") Integer id) {
		Product product = productRepository.findById(id.longValue());
		System.out.println("FETCHING PRODUCT BASED ON ID : " + product);
		if (product == null) {
			System.out.println("No product found");
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(product).build();
	}

	// Update an existing product details
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public Response updateProduct(@PathParam("id") Integer id, Product product) {
		Product existingProduct = productRepository.findById(id.longValue());
		if (existingProduct == null) {
			System.out.println("No product found for id " + id);
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		existingProduct.setName(product.getName());
		existingProduct.setDescription(product.getDescription());
		existingProduct.setPrice(product.getPrice());
		existingProduct.setQuantity(product.getQuantity());
		System.out.println("UPDATING PRODUCT DETAILS BASED ON ID: " + existingProduct);
		return Response.ok(existingProduct).build();
	}

	// Delete a product by its id
	@DELETE
	@Path("/{id}")
	@Transactional
	public Response deleteProduct(@PathParam("id") Integer id) {
		Product existingProduct = productRepository.findById(id.longValue());
		if (existingProduct == null) {
			System.out.println("No product found for id " + id);
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		productRepository.delete(existingProduct);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	// Check stock availability based on id
	@GET
	@Path("/{id}/stock")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response checkStock(@PathParam("id") Integer id, @QueryParam("count") Integer count) {
		Product product = productRepository.findById(id.longValue());
		if (product == null) {
			System.out.println("No product found for id " + id);
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		boolean available = product.getQuantity() >= count;
		return Response.ok(available).build();
	}

	// Get products sorted by price
	@GET
	@Path("/sorted")
	public List<Product> getProductsSortedByPrice() {
		return productRepository.find("SELECT p FROM Product p ORDER BY p.price ASC").list();
	}
}
