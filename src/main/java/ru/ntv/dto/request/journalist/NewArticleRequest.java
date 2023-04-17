package ru.ntv.dto.request.journalist;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewArticleRequest {
    private List<Integer> themeIds;

    @Size(min = 2, max = 50)
    private String header;

    @Size(min = 2, max = 100)
    private String subheader;

    @Size(min = 2, max = 65535)
    private String text;

    @Min(1)
    @Max(3)
    private Integer priority;

    @Size(min = 2, max = 255)
    private String photoURL;
}