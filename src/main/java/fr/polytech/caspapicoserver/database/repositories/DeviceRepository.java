package fr.polytech.caspapicoserver.database.repositories;

import fr.polytech.caspapicoserver.database.documents.Device;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;

@Repository
public interface DeviceRepository extends ReactiveMongoRepository<Device, ObjectId> {
	Mono<Device> findByKey(String key);
	Flux<Device> findByIdNotNull(Pageable pageable);
	Mono<Device> findByActivationKey(String key);
}
