package fr.polytech.caspapicoserver.database.repositories;

import fr.polytech.caspapicoserver.database.documents.RawData;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawDataRepository extends ReactiveMongoRepository<RawData, ObjectId> {

}
