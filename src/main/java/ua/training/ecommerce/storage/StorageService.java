package ua.training.ecommerce.storage;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void init();

    String store(MultipartFile file, String path);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String path);

    void deleteAll();

}
