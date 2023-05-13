package ru.ntv.dto.response.boss;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalistResponse implements Serializable {
    private int id;
    private String username;
}