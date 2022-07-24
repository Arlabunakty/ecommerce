package ua.training.ecommerce.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ua.training.ecommerce.controllers.models.ProductResource;
import ua.training.ecommerce.models.Product;
import ua.training.ecommerce.models.ProductImage;
import ua.training.ecommerce.services.EcommerceService;
import ua.training.ecommerce.services.ProductImageService;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final EcommerceService ecommerceService;
    private final ProductImageService productImageService;

    @GetMapping
    public Collection<ProductResource> index() {
        return ecommerceService.getProducts().stream()
                .map(this::mapToResource)
                .toList();
    }

    private ProductResource mapToResource(Product product) {
        return new ProductResource(product);
    }

    @PostMapping
    public ProductResource create(@RequestBody @Valid Product product) {
        return mapToResource(ecommerceService.saveProduct(product));
    }

    @GetMapping("/{productId}")
    public ProductResource view(@PathVariable("productId") long id) {
        return mapToResource(ecommerceService.getProduct(id));
    }

    @PostMapping(value = "/{productId}")
    public ProductResource edit(@PathVariable("productId") long id, @RequestBody @Valid Product product) {
        Product updatedProduct = ecommerceService.getProduct(id);

        if (updatedProduct == null) {
            return null;
        }

        updatedProduct.setName(product.getName());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setDescription(product.getDescription());

        return mapToResource(ecommerceService.saveProduct(updatedProduct));
    }

    @GetMapping("/{productId}/images")
    public List<ProductImage> viewImages(@PathVariable("productId") long productId) {
        return productImageService.getByProductId(productId);
    }

    @GetMapping("/image/{imageId}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable("imageId") long imageId) {
        Resource file = productImageService.getProductImage(imageId);
        String mimeType = "image/png";
        try {
            mimeType = file.getURL().openConnection().getContentType();
        } catch (IOException e) {
            log.error("Can't get file mimeType. {}", e.getMessage(), e);
        }
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .body(file);
    }

    @PostMapping("/{productId}/image")
    public ProductImage handleFileUpload(@PathVariable("productId") long id, @RequestParam("file") MultipartFile file) {
        return productImageService.addProductImage(id, file);
    }

}