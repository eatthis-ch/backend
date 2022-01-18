package ch.eatthis.backend.images;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Objects;

@Service
public class ImageServiceImpl implements ImageService{
    @Override
    public void uploadImage(MultipartFile file) {
        AWSCredentials credentials = new BasicAWSCredentials(
                "1W7O9JENFSLOD1VJ1JXP",
                "K3vR766otTcn7j1mTfz8PFjYwuwXmonHD5PuA63s"
        );

        AmazonS3 s3client = new AmazonS3Client(credentials);
        s3client.setEndpoint("s3.technat.dev");
        List<Bucket> buckets = s3client.listBuckets();

        for (Bucket bucket: buckets) {
            System.out.println(bucket.getName());
        }
//        System.out.println(file.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        PutObjectRequest request = null;
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ImageWriter writer = ImageIO.getImageWritersByMIMEType(Objects.requireNonNull(file.getContentType())).next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(out);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(1);
            }

            writer.write(null, new IIOImage(image, null, null), param);

            out.close();
            ios.close();
            writer.dispose();
            try (InputStream compressedFile = new ByteArrayInputStream(out.toByteArray())) {
                metadata.setContentLength(compressedFile.available());
                request = new PutObjectRequest("image-store", file.getOriginalFilename().replaceAll(".jpg", ""), compressedFile, metadata);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        s3client.putObject(request);
    }
}
