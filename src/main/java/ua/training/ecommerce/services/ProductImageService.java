package ua.training.ecommerce.services;

import java.nio.file.Paths;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.training.ecommerce.models.ProductImage;
import ua.training.ecommerce.repositories.ProductImageRepository;
import ua.training.ecommerce.storage.StorageService;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final StorageService storageService;
    private final ProductImageRepository productImageRepository;

    public List<ProductImage> getByProductId(long productId) {
        return productImageRepository.findAll()
                .stream()
                .filter(e -> e.getProductId() == productId)
                .toList();
    }

    public Resource getProductImage(long id) {
        ProductImage image = productImageRepository.getReferenceById(id);
        String path = Paths.get("product-images", String.valueOf(image.getProductId()), image.getPath()).toString();
        return storageService.loadAsResource(path);
    }

    @Transactional
    public ProductImage addProductImage(long productId, MultipartFile multipartFile) {
        String path = "/product-images/" + productId;
        String filename = storageService.store(multipartFile, path);

        ProductImage image = new ProductImage();
        image.setProductId(productId);
        image.setPath(filename);
        return productImageRepository.save(image);
    }
}
