package renatius.syazhkapro;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Data {

    @NotBlank(message = "Номер телефона обязателен")
    @Pattern(
            regexp = "^\\+375(25|29|33|44)\\d{7}$",
            message = "Телефон должен быть в формате: +375XXYYYYYYY (например, +375291234567)"
    )
    private String phoneNumber;

    @NotNull
    private String contentMessage;

    @NotNull
    @PositiveOrZero(message = "Количество не может быть отрицательной")
    @Min(value = 1, message = "Количество должно быть больше 0")
    private int square;

}
