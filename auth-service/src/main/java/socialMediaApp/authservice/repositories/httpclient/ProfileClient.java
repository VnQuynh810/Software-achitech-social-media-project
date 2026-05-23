package socialMediaApp.authservice.repositories.httpclient;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import socialMediaApp.authservice.dto.requests.ProfileCreationRequest;
import socialMediaApp.authservice.dto.responses.ApiResponse;
import socialMediaApp.authservice.dto.responses.UserProfileResponse;


@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {
    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserProfileResponse> createProfile(@RequestBody ProfileCreationRequest request);
}