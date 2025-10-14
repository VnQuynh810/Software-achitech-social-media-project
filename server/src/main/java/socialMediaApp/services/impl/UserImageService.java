package socialMediaApp.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import socialMediaApp.mappers.UserImageMapper;
import socialMediaApp.models.UserImage;
import socialMediaApp.repositories.UserImageRepository;
import socialMediaApp.dto.responses.userImage.UserImageResponse;
import socialMediaApp.services.IUserImageService;
import socialMediaApp.utils.ImageUtil;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserImageService implements IUserImageService {

    private final UserImageRepository userImageRepository;
    private final UserService userService;
    private final UserImageMapper userImageMapper;

    public UserImageService(UserImageRepository userImageRepository, UserService userService, UserImageMapper userImageMapper) {
        this.userImageRepository = userImageRepository;
        this.userService = userService;
        this.userImageMapper = userImageMapper;
    }

    @Override
    public UserImageResponse upload(MultipartFile file,int userId) throws IOException {
        UserImage userImage = new UserImage();
        userImage.setData(ImageUtil.compressImage(file.getBytes()));
        userImage.setName(file.getOriginalFilename());
        userImage.setType(file.getContentType());
        userImage.setUser(userService.getById(userId));
        userImageRepository.save(userImage);
        return userImageMapper.userImageToResponse(userImage);
    }
    @Override
    public byte[] download(int id){
        Optional<UserImage> userImage = userImageRepository.findByUser_Id(id);
        return ImageUtil.decompressImage(userImage.get().getData());
    }
}
