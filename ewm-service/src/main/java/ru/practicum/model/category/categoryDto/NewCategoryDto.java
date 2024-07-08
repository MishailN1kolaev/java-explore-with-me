package ru.practicum.model.category.categoryDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {

    @NotNull
    @NotBlank
    @Size(min = 1)
    @Size(max = 50)
    private String name;

    public void setName(String value) {
        this.name = value.trim();
    }

    public String getName() {
        return this.name;
    }
}
