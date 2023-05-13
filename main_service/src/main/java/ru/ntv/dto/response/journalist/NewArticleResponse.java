package ru.ntv.dto.response.journalist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewArticleResponse {
    String errorMessage;
    String status;
}