package auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Email
    @NotEmpty
    private String email;

    @Size(min=4,max=128)
    @NotEmpty
    private String username;

    @Size(min=8,max=128)
    @NotEmpty
    private String password;
}
