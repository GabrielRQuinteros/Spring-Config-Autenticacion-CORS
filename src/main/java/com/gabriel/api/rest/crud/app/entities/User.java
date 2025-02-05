package com.gabriel.api.rest.crud.app.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table( name = "users")
public class User {


    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Id
    private Long id;

    @NotBlank
    @Size(min = 4, max = 40)
    @Column( unique = true )
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Size(min = 4, max = 100)
    private String password;

    @Email
    @Column( unique = true )
    private String email;

    private boolean enabled;

    @Transient
    private boolean admin;

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    public User(String username, String password, String email, boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
    }

    public void updateFromUser(User user) {
        this.username = user.username;
        this.password = user.password;
        this.email = user.email;
        this.enabled = user.enabled;

    }

}
