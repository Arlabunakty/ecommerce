package ua.training.ecommerce.storage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Random;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileSystemStorageService implements StorageService {
    private static final Random RANDOM = new Random();
    private final StorageProperties properties;

    @Override
    public String store(MultipartFile file, String path) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Path location = Paths.get(properties.getLocation() + "/" + path);
            if (!Files.isDirectory(location)) {
                Files.createDirectories(location);
            }

            String filename = "%d-%d-%s".formatted(new Date().getTime(), 10000 + RANDOM.nextInt(100000), file.getOriginalFilename());

            Files.copy(file.getInputStream(), location.resolve(filename));

            return filename;

        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            Path rootLocation = getRootLocation();
            return Files.walk(rootLocation, 1)
                    .filter(path -> !path.equals(rootLocation))
                    .map(rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    private Path getRootLocation() {
        return Paths.get(properties.getLocation());
    }

    @Override
    public Path load(String filename) {
        return getRootLocation().resolve(filename);
    }

    @Override
    public Resource loadAsResource(String path) {
        try {
            Path file = load(path);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + path);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + path, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(getRootLocation().toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(getRootLocation());
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
