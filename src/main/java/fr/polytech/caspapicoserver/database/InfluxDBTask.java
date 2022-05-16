package fr.polytech.caspapicoserver.database;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.write.Point;
import fr.polytech.caspapicoserver.database.documents.RawData;
import fr.polytech.caspapicoserver.database.repositories.AccountRepository;
import fr.polytech.caspapicoserver.database.repositories.RawDataRepository;
import fr.polytech.caspapicoserver.dataparser.MV11Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class InfluxDBTask {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	final AccountRepository accountRepository;
	final RawDataRepository rawDataRepository;

	public InfluxDBTask(AccountRepository accountRepository, RawDataRepository rawDataRepository) {
		this.accountRepository = accountRepository;
		this.rawDataRepository = rawDataRepository;
	}

	@Value("${influxdb.token}")
	private String influxDBToken;

	@Value("${influxdb.bucket}")
	private String influxDBBuket;

	@Value("${influxdb.org}")
	private String influxDBOrg;

	@Value("${influxdb.url}")
	private String influxDBUrl;

	@Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
	public void runTask(){

		InfluxDBClient client = InfluxDBClientFactory.create(influxDBUrl, influxDBToken.toCharArray());
		List<RawData> dataList = rawDataRepository.findByInfluxDBTransfertStatusIn(List.of(RawData.InfluxDBTransfertStatus.WaitingTransfert, RawData.InfluxDBTransfertStatus.ParseError)).collectList().block();
		if(dataList == null || dataList.size() <= 0){
			return;
		}

		logger.info("Transfert des données de MongoDB à InfluxDB : "+dataList.size()+" fichiers");
		WriteApiBlocking writeApiBlocking = client.getWriteApiBlocking();
		for(RawData data : dataList){
			try{
				List<Point> points = MV11Parser.parseData(data.getData(), data.getDeviceID());
				for(Point point : points){
					try{
						writeApiBlocking.writePoint(influxDBBuket, influxDBOrg, point);
						data.setInfluxDBTransfertStatus(RawData.InfluxDBTransfertStatus.Transfered);
						rawDataRepository.save(data).block();
					}
					catch (Exception e){
						e.printStackTrace();
					}
				}
			}
			catch (Exception e){
				data.setInfluxDBTransfertStatus(RawData.InfluxDBTransfertStatus.ParseError);
				data.setInfluxDBTransfertErrorStr(e.getLocalizedMessage());
				rawDataRepository.save(data).block();
			}
		}

		client.close();
	}
}
