package com.productManagement;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Resource file to perform CRUD operations. Can be tested via Postman.
 */
@Path("/products")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResources {

    @Inject
    ProductRepository productRepository;

    /**
     * To create a new product
     * @param product
     * @return
     */
    @POST
    public Uni<Response> createProduct(Product product) {
        System.out.println("ADDING PRODUCT: " + product);
        return product.persistAndFlush()
                .replaceWith(Response.status(Response.Status.CREATED).entity(product).build());
    }

    /**
     * To read the detailed list of all products.
     * @return
     */
    @GET
    public Uni<List<Product>> getAllProducts() {
        return productRepository.listAll()
                .invoke(products -> System.out.println("FETCHING ALL PRODUCTS: " + products));
    }

    /**
     * To read the list of all products and details of a specific product by id.
     * @param id
     * @return
     */
    @GET
    @Path("/{id}")
    public Uni<Response> getProductById(@PathParam("id") Long id) {
        return productRepository.findById(id)
                .onItem().ifNotNull().transform(product -> {
                    System.out.println("FETCHING PRODUCT BASED ON ID: " + product);
                    return Response.ok(product).build();
                })
                .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * To update the details of an existing product by id.
     * @param id
     * @param product
     * @return
     */
    @PUT
    @Path("/{id}")
    public Uni<Response> updateProduct(@PathParam("id") Long id, Product product) {
        return productRepository.findById(id)
                .onItem().ifNotNull().transformToUni(existingProduct -> {
                    existingProduct.name = product.name;
                    existingProduct.description = product.description;
                    existingProduct.price = product.price;
                    existingProduct.quantity = product.quantity;
                    return existingProduct.persistAndFlush();
                })
                .replaceWith(Response.ok(product).build())
                .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * To delete a product by id.
     * @param id
     * @return 
     */
    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteProduct(@PathParam("id") Long id) {
    	return productRepository.deleteById(id)
                .map(deleted -> {
                    if (deleted) {
                        System.out.println("DELETING PPRODUCT WITH ID " + id);
                        return Response.noContent().build();
                    } else {
                        System.out.println("NO PRODUCT FOUND WITH ID " + id);
                        return Response.status(Response.Status.NOT_FOUND).build();
                    }
                });
    	
    }

    /**
     * To check stock availability by id.
     * @param id
     * @param count
     * @return
     */
    @GET
    @Path("/{id}/stock")
    public Uni<Response> checkStock(@PathParam("id") Long id, @QueryParam("count") Integer count) {
        return productRepository.findById(id)
                .onItem().ifNotNull().transform(product -> Response.ok(product.quantity >= count).build())
                .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * To Retrieve all products in ascending order by price.
     * @return
     */
    @GET
    @Path("/sorted")
	public Uni<List<Product>> getProductsSortedByPrice() {
		return productRepository.find("ORDER BY price ASC").list().invoke(products -> {
			System.out.println("FETCHING PRODUCTS SORTED BY PRICE:");
			products.forEach(product -> System.out.println(product));
		});
	}
}
