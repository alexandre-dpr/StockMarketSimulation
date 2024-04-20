package community.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorDTO {
    private final String errorMessage;
    private final Integer status;
    private final String path;

    public ErrorDTO(String errorMessage, HttpStatus status, String path) {
        this.errorMessage = errorMessage;
        this.status = status.value();
        this.path = path;
    }
}
