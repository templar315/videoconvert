package com.satrumroom.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_info")
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo implements Serializable {

    private static final long serialVersionUID = -8758503860116870553L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path", nullable = false, unique = true)
    private String path;

    @Column(name = "last_change", nullable = false)
    private LocalDateTime lastChange;

    @Column(name = "video_format")
    private String videoFormat;

    @Column(name = "audio_format")
    private String audioFormat;

    @Column(name = "convertible", nullable = false)
    private boolean convertible;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "file_info_user", nullable = false)
    private User user;

}
