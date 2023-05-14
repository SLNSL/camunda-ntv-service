package ru.ntv.dto.request.theme;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeToThemesRequest {
    private List<String> themes;
}