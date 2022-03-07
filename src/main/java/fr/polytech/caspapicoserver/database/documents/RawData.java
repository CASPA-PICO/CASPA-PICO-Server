package fr.polytech.caspapicoserver.database.documents;

import com.mongodb.internal.HexUtils;
import com.mongodb.lang.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Document("rawdata")
public class RawData {
	@Id
	@NonNull
	final private String dataHash;
	@NonNull
	final private byte[] data;
	@NonNull
	final private ObjectId deviceID;

	public RawData(byte[] data, ObjectId deviceID) throws NoSuchAlgorithmException {
		this.data = data;
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
		dataHash = HexUtils.toHex(messageDigest.digest(data)).toLowerCase();
		this.deviceID = deviceID;
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
}
