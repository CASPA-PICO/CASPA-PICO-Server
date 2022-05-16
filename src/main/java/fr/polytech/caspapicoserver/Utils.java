package fr.polytech.caspapicoserver;

import fr.polytech.caspapicoserver.database.documents.Account;
import fr.polytech.caspapicoserver.database.repositories.AccountRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Utils {

	@Autowired
	private AccountRepository accountRepository;

	public String getFormatedDate(LocalDateTime localDateTime, String format){
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
		return localDateTime.format(dateTimeFormatter);
	}

}
