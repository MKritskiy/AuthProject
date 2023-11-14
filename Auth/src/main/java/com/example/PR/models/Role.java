package com.example.PR.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {
    @Column(name = "name")
    private String name;
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id: " + super.getId() + ", " +
                "name: " + name + "}";
    }
}
