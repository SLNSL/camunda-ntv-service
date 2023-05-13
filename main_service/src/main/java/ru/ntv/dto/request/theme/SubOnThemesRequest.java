package ru.ntv.dto.request.theme;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ntv.entity.articles.Theme;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubOnThemesRequest {


    private List<String> themes;
}