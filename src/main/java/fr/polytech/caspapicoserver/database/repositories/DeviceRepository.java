package fr.polytech.caspapicoserver.database.repositories;

import fr.polytech.caspapicoserver.database.documents.Device;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

@Document(collection = "CASPA-PICO")
public interface DeviceRepository extends MongoRepository<Device, ObjectId> {
	Device findByKey(String key);
}
