package revend.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "email")
    private String email;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image_data", columnDefinition = "BLOB")
    private byte[] imageData;

    @NotNull
    @Column(name = "password")
    private String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<UserRoleEntity> userRoles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ListingEntity> listings;

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ConversationEntity> conversationsAsUser1;

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ConversationEntity> conversationsAsUser2;

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MessageEntity> messages;
}
