package br.com.db1.christmastree.domain.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.Objects;

public class UserAdAttributesMapper implements ContextMapper<UserAd> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAdAttributesMapper.class);

    @Override
    public UserAd mapFromContext(Object obj) {
        try {
            UserAd userAD = new UserAd();
            if (obj instanceof DirContextAdapter) {
                DirContextAdapter dirContextAdapter = (DirContextAdapter) obj;
                userAD.setName(getValueByAttributeIfExists(dirContextAdapter.getAttributes(), "cn"));
                userAD.setMail(getValueByAttributeIfExists(dirContextAdapter.getAttributes(), "mail"));
            }
            userAD.setGrupoAtivo(obj.toString().contains("OU=Ativos"));
            return userAD;
        } catch (Exception e) {
            LOGGER.error(obj.toString(), e);
            return new UserAd();
        }
    }

    private String getValueByAttributeIfExists(Attributes attributes, String key) throws NamingException {
        if (Objects.nonNull(attributes.get(key))) {
            return attributes.get(key).get().toString();
        }
        return "";
    }

}
