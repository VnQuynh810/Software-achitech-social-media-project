package socialMediaApp.services;

import org.springframework.web.multipart.MultipartFile;
import socialMediaApp.dto.responses.userImage.UserImageResponse;
import java.io.IOException;


public interface IUserImageService {

     UserImageResponse upload(MultipartFile file, int userId) throws IOException;

     byte[] download(int id);
}
