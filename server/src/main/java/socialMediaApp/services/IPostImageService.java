package socialMediaApp.services;

import org.springframework.web.multipart.MultipartFile;
import socialMediaApp.dto.responses.postImage.PostImageResponse;


import java.io.IOException;


public interface IPostImageService {

     PostImageResponse upload(MultipartFile file, int postId) throws IOException;

     byte[] download(int id);
}