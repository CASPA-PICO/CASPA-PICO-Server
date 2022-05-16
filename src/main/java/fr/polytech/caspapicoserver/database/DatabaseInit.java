package fr.polytech.caspapicoserver.database;

import fr.polytech.caspapicoserver.database.documents.Account;
import fr.polytech.caspapicoserver.database.repositories.AccountRepository;
import fr.polytech.caspapicoserver.security.UserRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInit {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	final AccountRepository accountRepository;

	public DatabaseInit(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@PostConstruct
	void initDatabase(){
		logger.info("Initialisation de la base de donnée");

		accountRepository.count().doOnError(throwable -> {logger.error("Impossible d'obtenir le nombre d'utilisateurs : "+throwable.getLocalizedMessage());})
				.subscribe(accountCount -> {
			if(accountCount == 0){
				Account adminAccount = new Account("admin",
						new BCryptPasswordEncoder().encode("admin123"),
						"Administrateur",
						"",
						true,
						UserRoles.ADMINISTRATEUR);
				accountRepository.save(adminAccount).subscribe(account -> {
					logger.info("Aucun compte : ajout du compte Administateur par défaut");
				});
			}
		});
	}
}
