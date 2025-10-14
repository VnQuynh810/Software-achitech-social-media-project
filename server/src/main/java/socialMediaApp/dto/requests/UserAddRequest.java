package socialMediaApp.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddRequest {
    @NotNull(message = "name ko duoc de trong")
    private String name;
    @NotNull(message = "lastname ko duoc de trong")
    private String lastName;
    @NotNull(message = "email ko duoc de trong")
    private String email;
    @NotNull(message = "password ko duoc de trong")
    private String password;
}
