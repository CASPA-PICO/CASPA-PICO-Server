package fr.polytech.caspapicoserver.database.repositories;

import fr.polytech.caspapicoserver.database.documents.Device;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DeviceRepository extends ReactiveMongoRepository<Device, ObjectId> {
	Mono<Device> findByKey(String key);
	Flux<Device> findByOwner(ObjectId owner, Pageable pageable);
	Mono<Device> findByActivationKey(String key);
	Flux<Device> findByPublicDevice(Boolean publicDevice, Pageable pageable);
}
