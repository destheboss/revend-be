package revend.configuration.security.token;

import java.util.Set;

public interface RefreshToken {
    String getSubject();
    Long getUserId();
    Set<String> getRoles();
    boolean hasRole(String roleName);
}
