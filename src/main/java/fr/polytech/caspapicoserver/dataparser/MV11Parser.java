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
	public static List<Point> parseData(byte[] data, ObjectId deviceID) throws NumberFormatException{

		ArrayList<Point> result = new ArrayList<>();
		StringBuilder lineBuilder = new StringBuilder();
		for (int pos = 0; pos < data.length; pos++) {
			lineBuilder.append((char) data[pos]);
			if (data[pos] == '\n') {
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
						e.printStackTrace();
					}
				}

				lineBuilder.setLength(0);
			}
		}
		return result;
	}
}
