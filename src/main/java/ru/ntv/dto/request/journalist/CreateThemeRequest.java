package ru.ntv.dto.request.journalist;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateThemeRequest {

    @Size(min = 2, max = 100)
    private String name;
}