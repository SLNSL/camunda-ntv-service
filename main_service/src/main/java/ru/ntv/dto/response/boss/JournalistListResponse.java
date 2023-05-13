package ru.ntv.dto.response.boss;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalistListResponse implements Serializable {
    private List<JournalistResponse> journalists;
}