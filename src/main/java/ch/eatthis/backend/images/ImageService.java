package ch.eatthis.backend.images;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void uploadImage(MultipartFile file);
}
