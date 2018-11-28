package br.com.db1.christmastree.infra;

import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipal;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class UserLogged {

    private final UserPrincipal user;

    private UserLogged(UserPrincipal user) {
        this.user = user;
    }

    public static UserLogged of(PreAuthenticatedAuthenticationToken authToken) {
        return new UserLogged((UserPrincipal) authToken.getPrincipal());
    }

    public String getName() {
        String givenName = user.getClaims().get("given_name").toString();
        String familyName = user.getClaims().get("family_name").toString();
        return givenName + " " + familyName;
    }

    public String getEmail() {
        return user.getClaims().get("unique_name").toString();
    }
}
