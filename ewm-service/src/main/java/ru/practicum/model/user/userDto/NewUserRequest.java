package ru.practicum.model.user.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {

    @Email
    @NotNull
    @NotBlank
    @Size(min = 6)
    @Size(max = 254)
    String email;

    @NotNull
    @NotBlank
    @Size(min = 2)
    @Size(max = 250)
    String name;
}
