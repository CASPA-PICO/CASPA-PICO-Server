package fr.polytech.caspapicoserver.dataparser;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class MV11Parser {
	/**
	 * C'est ici que le fichier de données est lu et interprété en une liste de points
	 * @param data : données à lire
	 * @param deviceID : deviceID de la base depuis laquelle les données ont été envoyés
	 * @return Renvoie une liste de points, représentant une mesure à un instant donné dans InfluxDB
	 */
	public static List<Point> parseData(byte[] data, ObjectId deviceID){

		ArrayList<Point> result = new ArrayList<>();

		//On lit caractère par caractère jusqu'à tomber sur un retour à la ligne puis on interprète la ligne complète
		StringBuilder lineBuilder = new StringBuilder();
		for (int pos = 0; pos < data.length; pos++) {
			lineBuilder.append((char) data[pos]);
			if (data[pos] == '\n') {
				//On sépare chaque morceau de la ligne par les ',' et si cela correspond bien à ce que l'on attend, on interprète la ligne
				String[] lineSplit = lineBuilder.toString().split(",");
				if (lineSplit.length >= 10) {
					Instant timestamp;
					try {
						String[] dateSplit = lineSplit[0].split(" ")[0].split("/");
						String[] timeSplit = lineSplit[0].split(" ")[1].split(":");
						LocalDateTime localDateTime = LocalDateTime.now().withDayOfMonth(Integer.parseInt(dateSplit[2]))
								.withMonth(Integer.parseInt(dateSplit[1]))
								.withHour(Integer.parseInt(timeSplit[0]))
								.withMinute(Integer.parseInt(timeSplit[1]))
								.withSecond(Integer.parseInt(timeSplit[2]));
						if (Integer.parseInt(dateSplit[0]) >= 1000) {
							localDateTime = localDateTime.withYear(Integer.parseInt(dateSplit[0]));
						} else {
							localDateTime = localDateTime.withYear(Integer.parseInt(dateSplit[0]) + 2000);
						}
						timestamp = localDateTime.toInstant(ZoneOffset.UTC);

						//On ajoute notre point à la liste des points résultants
						result.add(Point.measurement("MV11")
								.time(timestamp.getEpochSecond(), WritePrecision.S)
								.addTag("deviceID", deviceID.toString())
								.addField("val1", Integer.parseInt(lineSplit[4]))
								.addField("val2", Integer.parseInt(lineSplit[5]))
								.addField("val3", Integer.parseInt(lineSplit[6]))
								.addField("val4", Integer.parseInt(lineSplit[7]))
								.addField("val5", Integer.parseInt(lineSplit[8]))
								.addField("val6", Integer.parseInt(lineSplit[9])));
					} catch (Exception e) {
						//En cas d'erreur on saute la ligne
						e.printStackTrace();
					}
				}

				//On vide le lineBuilder pour pouvoir recommencer à vide pour la prochaine ligne
				lineBuilder.setLength(0);
			}
		}
		return result;
	}
}
