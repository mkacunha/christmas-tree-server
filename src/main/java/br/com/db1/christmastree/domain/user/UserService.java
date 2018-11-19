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
                .timeLimit(THREE_SECONDS)
                .attributes("cn", "ou", "uid", "mail")
                .base(LdapUtils.emptyLdapName())
                .where("objectclass").is("person");
        List<UserAd> usersAd = ldapTemplate.search(query, new UserAdAttributesMapper());
        return usersAd.stream().filter(UserAd::isGrupoAtivo).collect(Collectors.toList());
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
}
