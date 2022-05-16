package fr.polytech.caspapicoserver.database.repositories;

import fr.polytech.caspapicoserver.database.documents.RawData;
import fr.polytech.caspapicoserver.database.documents.RawData.InfluxDBTransfertStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface RawDataRepository extends ReactiveMongoRepository<RawData, ObjectId> {
	Flux<RawData> findByInfluxDBTransfertStatusIn(List<InfluxDBTransfertStatus> influxDBTransfertStatus);
}
