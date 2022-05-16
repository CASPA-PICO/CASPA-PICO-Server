package fr.polytech.caspapicoserver.database.documents;

import com.mongodb.internal.HexUtils;
import com.mongodb.lang.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document("rawdata")
public class RawData {

	public enum InfluxDBTransfertStatus {WaitingTransfert, Transfered, ParseError};
	@Id
	@NonNull
	private ObjectId id;
	@NonNull
	private String dataHash;
	@NonNull
	private byte[] data;
	@NonNull
	private ObjectId deviceID;
	@NonNull
	private String filename = "";
	@NonNull
	private LocalDateTime uploadDate;

	@NonNull
	private InfluxDBTransfertStatus influxDBTransfertStatus;
	private String influxDBTransfertErrorStr;

	public RawData(String filename, byte[] data, ObjectId deviceID) throws NoSuchAlgorithmException{
		this.filename = filename;
		this.data = data;
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
		dataHash = HexUtils.toHex(messageDigest.digest(data)).toLowerCase();
		this.deviceID = deviceID;
		this.uploadDate = LocalDateTime.now();
		this.id = new ObjectId();
		influxDBTransfertStatus = InfluxDBTransfertStatus.WaitingTransfert;
	}

	@NonNull
	public String getDataHash() {
		return dataHash;
	}

	@NonNull
	public byte[] getData() {
		return data;
	}

	@NonNull
	public ObjectId getDeviceID() {
		return deviceID;
	}

	public String getFilename(){
		return filename;
	}
	@NonNull
	public LocalDateTime getUploadDate(){
		return uploadDate;
	}

	@NonNull
	public InfluxDBTransfertStatus getInfluxDBTransfertStatus() {
		return influxDBTransfertStatus;
	}

	public void setInfluxDBTransfertStatus(@NonNull InfluxDBTransfertStatus influxDBTransfertStatus) {
		this.influxDBTransfertStatus = influxDBTransfertStatus;
	}

	public String getInfluxDBTransfertErrorStr() {
		return influxDBTransfertErrorStr;
	}

	public void setInfluxDBTransfertErrorStr(String influxDBTransfertErrorStr) {
		this.influxDBTransfertErrorStr = influxDBTransfertErrorStr;
	}
}
