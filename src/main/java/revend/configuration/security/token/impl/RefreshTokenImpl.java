package revend.configuration.security.token.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import revend.configuration.security.token.RefreshToken;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@EqualsAndHashCode
@Getter
public class RefreshTokenImpl implements RefreshToken {
    private final String subject;
    private final Long userId;
    private final Set<String> roles;

    public RefreshTokenImpl(String subject, Long userId, Collection<String> roles) {
        this.subject = subject;
        this.userId = userId;
        this.roles = roles != null ? Set.copyOf(roles) : Collections.emptySet();
    }

    @Override
    public boolean hasRole(String roleName) {
        return this.roles.contains(roleName);
    }
}
