package revend.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
