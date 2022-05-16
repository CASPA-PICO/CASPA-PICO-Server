package fr.polytech.caspapicoserver.database.repositories;

import fr.polytech.caspapicoserver.database.documents.Account;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, ObjectId> {

	Mono<Account> findById(ObjectId id);
	Mono<Account> findByUsername(String username);
}
