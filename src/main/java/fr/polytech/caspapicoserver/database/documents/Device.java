package fr.polytech.caspapicoserver.database.documents;

import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.util.annotation.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document(collection = "devices")
public class Device {

	@Id
	@NonNull
	private ObjectId id;
	@NotBlank
	private String displayName;
	private String description;

	@NonNull
	private String key;
	private String activationKey = null;
	private LocalDateTime activationKeyExpiration = null;

	@NonNull
	private boolean allowed;
	@NotNull
	private boolean publicDevice;

	@NonNull
	private LocalDateTime createDate;
	private LocalDateTime lastUpdate = null;

	public Device(){}

	public Device(String displayName) {
		this.id = new ObjectId();
		this.displayName = displayName;
		this.key = RandomStringUtils.randomAlphanumeric(64);
		this.allowed = true;
		this.createDate = LocalDateTime.now();
		this.publicDevice = true;
	}

	public Device(@NonNull String displayName, @NonNull String activationKey){
		this(displayName);
		setActivationKey(activationKey);
	}

	public Device(@NonNull String displayName, @NonNull boolean publicDevice, @NonNull String description){
		this.id = new ObjectId();
		this.displayName = displayName;
		this.description = description;
		this.key = RandomStringUtils.randomAlphanumeric(64);
		this.allowed = true;
		this.publicDevice = publicDevice;
		this.createDate = LocalDateTime.now();
		this.lastUpdate = null;
	}

	@PersistenceConstructor
	public Device(@NonNull ObjectId id, @NonNull String displayName, String description, @NonNull String key, String activationKey, LocalDateTime activationKeyExpiration, boolean allowed, boolean publicDevice, @NonNull LocalDateTime createDate, LocalDateTime lastUpdate) {
		this.id = id;
		this.displayName = displayName;
		this.description = description;
		this.key = key;
		this.activationKey = activationKey;
		this.activationKeyExpiration = activationKeyExpiration;
		this.allowed = allowed;
		this.publicDevice = publicDevice;
		this.createDate = createDate;
		this.lastUpdate = lastUpdate;
	}

	@NonNull
	public ObjectId getId() {
		return id;
	}

	public void setDisplayName(@NonNull String displayName) {
		this.displayName = displayName;
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
		if(activationKey.isBlank()){
			this.activationKey = null;
			this.activationKeyExpiration = null;
		}
		else{
			this.activationKey = activationKey;
			this.activationKeyExpiration = LocalDateTime.now().plusHours(24);
		}
	}

	public void removeActivationKey(){
		this.activationKey = null;
		this.activationKeyExpiration = null;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPublicDevice() {
		return publicDevice;
	}

	public void setPublicDevice(boolean publicDevice) {
		this.publicDevice = publicDevice;
	}

	@NonNull
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}
}
