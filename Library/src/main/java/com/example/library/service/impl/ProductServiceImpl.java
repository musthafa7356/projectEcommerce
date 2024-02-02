package com.example.library.service.impl;

import com.example.library.dto.ProductDto;
import com.example.library.exception.DisabledProductExistsException;
import com.example.library.model.*;
import com.example.library.repository.ColorRepository;
import com.example.library.repository.ImageRepository;
import com.example.library.repository.ProductRepository;
import com.example.library.repository.SizeRepository;
import com.example.library.service.CategoryService;
import com.example.library.service.ProductService;
import com.example.library.utils.ImageUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ImageUpload imageUpload;
    private ImageRepository imageRepository;
    private SizeRepository sizeRepository;
    private ColorRepository colorRepository;
    private CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, ImageUpload imageUpload, ImageRepository imageRepository, SizeRepository sizeRepository, ColorRepository colorRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.imageUpload = imageUpload;
        this.imageRepository = imageRepository;
        this.sizeRepository = sizeRepository;
        this.colorRepository = colorRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductDto> products() {
      return transferData(productRepository.getAllProduct());

    }

    @Override
    public List<ProductDto> allProduct() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = transferData(products);
        return productDtos;
    }


    //for customer
    @Override
    public List<ProductDto> getAllProduct() {
        List<Product> products = productRepository.getAllProduct();
        List<ProductDto> productDtos = transferData(products);
        return productDtos;
    }


    @Override
    public List<ProductDto> getAllProductCatAct() {
//        List<Product> products = productRepository.findAllByCatActivatedTrue();
        List<Product> products = productRepository.getAllProduct();
        List<ProductDto> productDtos = transferData(products);
        return productDtos;
    }


@Override
public Product save(List<MultipartFile> imageProducts, ProductDto productDto, List<Long> sizes_id,
                    List<Long> colors_id) {
    try {
        // Check if a disabled product with the same name already exists
        List<Product> existingProduct = productRepository.findByNameAndIsActivated(productDto.getName());
        if (!existingProduct.isEmpty()) {
            throw new DisabledProductExistsException("A product with the same name already exists.");
        }

        // Continue with saving the new product
        Product product = new Product();
        List<Size> sizes = sizeRepository.findAllById(sizes_id);
        List<Color> colors = colorRepository.findAllById(colors_id);
        product.setSizes(sizes);
        product.setColors(colors);
        product.setName(productDto.getName());
        product.setCurrentQuantity(productDto.getCurrentQuantity());
        product.setCostPrice(productDto.getCostPrice());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        product.set_activated(true);

        // Save the product
        Product savedProduct = productRepository.save(product);

        // Process images
        if (imageProducts != null) {
            List<Image> imagesList = new ArrayList<>();
            for (MultipartFile imageProduct : imageProducts) {
                Image image = new Image();
                String imageName = imageUpload.uploadFile(imageProduct);
                image.setName(imageName);
                image.setProduct(product);
                imageRepository.save(image);
                imagesList.add(image);
            }
            product.setImages(imagesList);
        }

        return savedProduct;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}


    @Override
    public Product update(List<MultipartFile> imageProducts, ProductDto productDto, List<Long> size_id,
                          List<Long> color_id){

        try {
            long id= productDto.getId();
            Product productUpdate = productRepository.getById(id);
            List<Size> sizes = sizeRepository.findAllById(size_id);
            List<Color> colors = colorRepository.findAllById(color_id);

            productUpdate.setSizes(sizes);
            productUpdate.setColors(colors);
            productUpdate.setCategory(productDto.getCategory());
            productUpdate.setName(productDto.getName());
            productUpdate.setCostPrice(productDto.getCostPrice());
            productUpdate.setCurrentQuantity(productDto.getCurrentQuantity());
            productRepository.save(productUpdate);
            if (imageProducts != null && !imageProducts.isEmpty() && imageProducts.size()!=1) {
                List<Image> imagesList = new ArrayList<>();
                List<Image> image = imageRepository.findImageBy(id);
                int i=0;
                for (MultipartFile imageProduct : imageProducts) {
                    String imageName = imageUpload.uploadFile(imageProduct);
                    image.get(i).setName(imageName);
                    image.get(i).setProduct(productUpdate);
                    imageRepository.save(image.get(i));
                    imagesList.add(image.get(i));
                    i++;
                }
                productUpdate.setImages(imagesList);
            }

            return productUpdate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void enableById(Long id) {
        Product product = productRepository.getById(id);
        product.set_activated(true);
        product.set_deleted(false);
        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.getById(id);
        product.set_deleted(true);
        product.set_activated(false);
        productRepository.save(product);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public ProductDto getById(Long id) {
        ProductDto productDto = new ProductDto();
        Product product = productRepository.getById(id);
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCategory(product.getCategory());
        productDto.setImages(product.getImages());
        productDto.setColors(product.getColors());
        productDto.setSizes(product.getSizes());
        return productDto;
    }

    @Override
    public List<ProductDto> searchProduct(String name) {
        List<Product> products = productRepository.findByNameStartingWithIgnoreCase(name);
        return products.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private ProductDto convertEntityToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setImages(product.getImages());
        productDto.setCategory(product.getCategory());
        productDto.setDeleted(product.is_deleted());
        productDto.setActivated(product.is_activated());
        return productDto;
    }


//    @Override
//    public Page<ProductDto> searchProducts(int pageNo, String keyword) {
//        List<Product> products = productRepository.findAllByNameContainingIgnoreCase(keyword);
//        List<ProductDto> productDtoList = transferData(products);
//        Pageable pageable = PageRequest.of(pageNo, 5);
//        Page<ProductDto> dtoPage = toPage(productDtoList, pageable );
//        return dtoPage;
//    }

//    @Override
//    public Page<ProductDto> getAllproducts(int pageNo) {
//        Pageable pageable = PageRequest.of(pageNo, 6);
//        List<ProductDto> productDtoLists = this.allProduct();
//        Page<ProductDto> productDtoPage = toPage(productDtoLists, pageable);
//        return productDtoPage;
//    }

//    @Override
//    public Page<ProductDto> getAllProductsForCustomer(int pageNo) {
//        return null;
//    }

    @Override
    public Page<ProductDto> findAllByActivated(long id, int pageNo) {
        List<Product> products = productRepository.findAllByActivatedTrue(id);
        List<ProductDto> productDtoList = transferData(products);
        int pageSize = 3;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productDtoList.size());
        return new PageImpl<>(productDtoList.subList(start, end), pageable, productDtoList.size());
//        Page<ProductDto> dtoPage = toPage(productDtoList, pageable);
//        return dtoPage;
    }

    @Override
    public Page<ProductDto> findAllByActivated(int pageNo, String sort) {
        List<Product> products;

        if ("lowToHigh".equals(sort)){
            products = productRepository.findAllByActivatedTrueAndSortBy("lowToHigh");
        }
        else if ("highToLow".equals(sort)){
            products = productRepository.findAllByActivatedTrueAndSortBy("highToLow");
        }
        else {
            products = productRepository.findAllByActivatedTrue();
        }

        List<ProductDto> productDtoList = transferData(products);
        int pageSize = 3;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productDtoList.size());
        return new PageImpl<>(productDtoList.subList(start, end), pageable, productDtoList.size());
//        return toPage(productDtoList, pageable);
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> findProductsByCategory(long id) {
        return productRepository.findAllByCategoryId(id);
    }


//    @Override
//    public boolean productById(long id) {
//        return productRepository.existsProductById();
//    }

//    @Override
//    public boolean exitById(long id) {
//        return productRepository.existsById(id);
//    }



    private List<ProductDto> transferData(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setDescription(product.getDescription());
            productDto.setImages(product.getImages());
            productDto.setSizes(product.getSizes());
            productDto.setColors(product.getColors());
            productDto.setCategory(product.getCategory());
            productDto.setActivated(product.is_activated());
            productDto.setDeleted(product.is_deleted());
            productDtos.add(productDto);
            Category category = new Category();
//            if (category.isActivated()) {
//                productDto.setCategory(category);
//            } else {
//                Category category1 = new Category();
//                category1.setName(category.getName() + "  ALERT: CATEGORY IS DISABLED");
//                productDto.setCategory(category1);
//            }
        }
        return productDtos;
    }

    private Page toPage(List list, Pageable pageable){
        if (pageable.getOffset() >= list.size()){
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }


}
