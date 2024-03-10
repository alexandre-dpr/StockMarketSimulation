package portefeuille.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TransactionActionReqDto {

    @NotBlank
    private String ticker;

    @NotNull
    private Integer quantity;
}
