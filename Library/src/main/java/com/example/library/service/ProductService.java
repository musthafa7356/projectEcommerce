package com.example.library.service;

import com.example.library.dto.ProductDto;
import com.example.library.model.Category;
import com.example.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<Product> findAll();
    List<ProductDto> products();
    List<ProductDto>allProduct();
    List<ProductDto> getAllProduct();
    List<ProductDto> getAllProductCatAct();
    Product save(List <MultipartFile> imageProduct, ProductDto product, List<Long> sizes, List<Long> colors);


    Product update(List<MultipartFile> imageProduct, ProductDto productDto, List<Long> size_id, List<Long> color_id);
    void enableById(Long id);
    void deleteById(Long id);
    Product findById(Long id);
    ProductDto getById(Long id);

    List<ProductDto> searchProduct(String name);

//    Page<ProductDto> searchProducts(int pageNo, String keyword);
//    Page<ProductDto> getAllproducts(int pageNo);
//    Page<ProductDto> getAllProductsForCustomer(int pageNo);

    Page<ProductDto> findAllByActivated(long id, int pageNo);
    Page<ProductDto> findAllByActivated(int pageNo, String sort);

    List<Product> getProductsByCategory(Category category);
    List<Product> findProductsByCategory(long id);
}
