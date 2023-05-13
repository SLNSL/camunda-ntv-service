package ru.ntv.dto.request.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetByArticleIdRequest implements Serializable {

    private int id;
}
