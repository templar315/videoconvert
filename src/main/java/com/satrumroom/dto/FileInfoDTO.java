package com.satrumroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoDTO implements Serializable {

    private static final long serialVersionUID = 107331531219791159L;

    private long id;
    private String name;
    private String path;
    private String lastChange;
    private String videoFormat;
    private String audioFormat;
    private boolean convertible;
    private long user;

}
