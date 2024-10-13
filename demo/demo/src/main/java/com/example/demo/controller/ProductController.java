package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal; // Thêm dòng này
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/all")
    public Iterable<Product> getAllProducts() {
        return productService.findAll();
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Optional<Product> product = productService.findById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }


    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        try {

            if (product.getProductName() == null || product.getProductName().isEmpty()) {
                return ResponseEntity.badRequest().body("Tên sản phẩm là bắt buộc.");
            }
            if (product.getProductPrice() == null || product.getProductPrice().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body("Giá sản phẩm phải lớn hơn 0.");
            }
            if (product.getCategoryID() == null) {
                return ResponseEntity.badRequest().body("ID danh mục là bắt buộc.");
            }
            // Nếu có URL hình ảnh, lưu vào sản phẩm
            if (product.getProductImage() == null || product.getProductImage().isEmpty()) {
                return ResponseEntity.badRequest().body("URL hình ảnh sản phẩm là bắt buộc.");
            }

            Product savedProduct = productService.save(product);
            return ResponseEntity.ok("Sản phẩm đã được tạo thành công với ID: " + savedProduct.getProductID());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi tạo sản phẩm: " + e.getMessage());
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        if (productService.findById(id).isPresent()) {
            // Kiểm tra các thông tin cần thiết
            if (product.getProductName() == null || product.getProductName().isEmpty()) {
                return ResponseEntity.badRequest().body("Tên sản phẩm là bắt buộc.");
            }
            if (product.getProductPrice() == null || product.getProductPrice().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body("Giá sản phẩm phải lớn hơn 0.");
            }
            if (product.getCategoryID() == null) {
                return ResponseEntity.badRequest().body("ID danh mục là bắt buộc.");
            }

            product.setProductID(id);
            productService.save(product);
            return ResponseEntity.ok("Sản phẩm với ID " + id + " đã được cập nhật thành công.");
        } else {
            return ResponseEntity.status(404).body("Không tìm thấy sản phẩm với ID " + id + ".");
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
        if (productService.findById(id).isPresent()) {
            productService.deleteById(id);
            return ResponseEntity.ok("Sản phẩm với ID " + id + " đã được xóa thành công.");
        } else {
            return ResponseEntity.status(404).body("Không tìm thấy sản phẩm với ID " + id + ".");
        }
    }
}
