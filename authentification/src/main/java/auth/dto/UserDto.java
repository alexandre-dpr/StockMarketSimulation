package auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
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
