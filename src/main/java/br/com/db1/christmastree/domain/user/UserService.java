package br.com.db1.christmastree.domain.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private static final Integer THREE_SECONDS = 9000;

    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private UserRepository userRepository;

    public List<UserAd> getAllUsersActive() {
        LdapQuery query = LdapQueryBuilder.query()
                .searchScope(SearchScope.SUBTREE)
                .attributes("cn", "ou", "uid", "mail")
                .base(LdapUtils.emptyLdapName())
                .where("objectclass").is("person");
        List<UserAd> usersAd = ldapTemplate.search(query, new UserAdAttributesMapper());
        usersAd.stream().filter(user -> user.isGrupoAtivo() && !StringUtils.hasText(user.getMail())).forEach(user -> LOGGER.info("User sem e-mail: {}", user));
        return usersAd.stream().filter(UserAd::isValid).collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void findAllUsersAdAndSave() {
        LOGGER.info("Inciou processamento de usuário ativos do AD para serem salvos no banco de dados local");
        final List<UserAd> allUsersAdActive = this.getAllUsersActive();
        final List<User> allUsers = userRepository.findAll();
        allUsersAdActive.stream()
                .map(userAd -> new User(userAd.getName(), userAd.getMail()))
                .filter(user -> !allUsers.contains(user))
                .peek(user -> System.out.println("Salvar usuário " + user))
                .forEach(userRepository::save);
        LOGGER.info("Terminou processamento de usuário ativos do AD para serem salvos no banco de dados local");
    }

    public List<User> findByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    public User findByMail(String mail) {
        return userRepository.findByMail(mail).orElseThrow(() -> new RuntimeException("Usuário de e-mail " + mail + " não encontrado na base de dados"));
    }
}
