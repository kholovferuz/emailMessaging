package uz.pdp.app_jwt_real_email.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToMany
    private Set<Role> roles;

    private boolean accountNonexpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled;

    private String emailCode;


    //------------------------USER DETAILS METHODLARI----------------------

    // Bu userning huquqlari
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    // userning username ni qaytaradi
    @Override
    public String getUsername() {
        return email;
    }

    // accountning muddati otganligi
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonexpired;
    }

    // accountning bloklanganligi
    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    // accountning ishonchlilik muddati
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    // verifikatsiyadan otganligi
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
