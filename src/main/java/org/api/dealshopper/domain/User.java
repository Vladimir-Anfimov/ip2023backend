package org.api.dealshopper.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    @NotNull
    private String username;

    @NotNull
    @Column(name = "firstname")
    private String firstName;

    @NotNull
    @Column(name = "lastname")
    private String lastName;

    @NotNull
    private String password;

    private String phone;

    @Column(unique = true)
    @NotNull
    private String email;

    private String address;

    @NotNull
    private String role;


    @ManyToMany
    @JoinTable(name = "favourites_products",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName ="id" ),
            inverseJoinColumns = @JoinColumn(name = "products_id",referencedColumnName ="id"))
    private List<Ingredient> favouriteProducts;


    @ManyToMany
    @JoinTable(name = "favourites_restaurants",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName ="id" ),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id",referencedColumnName ="id"))
    private List<Ingredient> favouriteRestaurants;

    public User(String username, String firstName, String lastName, String email, String password, String phone, String address)
    {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}