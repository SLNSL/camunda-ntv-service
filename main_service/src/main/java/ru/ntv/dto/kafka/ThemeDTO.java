package ru.ntv.dto.kafka;

import lombok.Data;

import java.io.Serializable;

@Data
public class ThemeDTO implements Serializable {

    private Integer id;
    private String name;
}
