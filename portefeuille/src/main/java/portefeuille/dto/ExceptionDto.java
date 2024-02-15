package portefeuille.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExceptionDto {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String path;
}

