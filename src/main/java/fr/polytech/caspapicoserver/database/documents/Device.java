package fr.polytech.caspapicoserver.database.documents;

import com.mongodb.lang.NonNull;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "devices")
public class Device {

	@Id
	final private ObjectId id;
	@NonNull
	private String displayName;
	@NonNull
	final private String key;
	@NonNull
	private boolean allowed;

	public Device(@NonNull String displayName) {
		this.id = new ObjectId();
		this.displayName = displayName;
		this.key = RandomStringUtils.randomAlphabetic(64);
		this.allowed = true;
	}

	public ObjectId getId() {
		return id;
	}

	@NonNull
	public String getDisplayName() {
		return displayName;
	}

	@NonNull
	public String getKey() {
		return key;
	}

	public boolean isAllowed() {
		return allowed;
	}
}
