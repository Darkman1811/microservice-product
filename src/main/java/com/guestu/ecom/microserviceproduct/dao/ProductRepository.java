package com.guestu.ecom.microserviceproduct.dao;

import com.guestu.ecom.microserviceproduct.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "Product", path = "product")
@CrossOrigin("*")
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
}
