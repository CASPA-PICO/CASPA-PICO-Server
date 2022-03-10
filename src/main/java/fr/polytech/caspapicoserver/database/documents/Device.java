package fr.polytech.caspapicoserver.database.documents;

import com.mongodb.lang.NonNull;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "devices")
public class Device {

	@Id
	@NonNull
	private ObjectId id;
	@NonNull
	private String displayName;
	@NonNull
	private String key;
	@NonNull
	private boolean allowed;

	private String activationKey = null;
	private LocalDateTime activationKeyExpiration = null;

	public Device(@NonNull String displayName) {
		this.id = new ObjectId();
		this.displayName = displayName;
		this.key = RandomStringUtils.randomAlphabetic(64);
		this.allowed = true;
	}

	public Device(@NonNull String displayName, @NonNull String activationKey){
		this(displayName);
		setActivationKey(activationKey);
	}

	@PersistenceConstructor
	public Device(@NonNull ObjectId id, @NonNull String displayName, @NonNull String key, boolean allowed, String activationKey, LocalDateTime activationKeyExpiration) {
		this.id = id;
		this.displayName = displayName;
		this.key = key;
		this.allowed = allowed;
		this.activationKey = activationKey;
		this.activationKeyExpiration = activationKeyExpiration;
	}

	@NonNull
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

	public String getActivationKey() {
		return activationKey;
	}

	public LocalDateTime getActivationKeyExpiration() {
		return activationKeyExpiration;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
		this.activationKeyExpiration = LocalDateTime.now().plusHours(24);
	}

	public void removeActivationKey(){
		this.activationKey = null;
		this.activationKeyExpiration = null;
	}
}
