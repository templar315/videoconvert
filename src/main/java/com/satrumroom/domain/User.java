package com.satrumroom.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "user")
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 6471869048313491172L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "login", nullable = false, unique = true)
    @NotEmpty(message = "*Please provide your login")
    private String login;

    @Column(name = "password", nullable = false)
    @NotEmpty(message = "*Please provide your password")
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileInfo> filesInfo;

}
