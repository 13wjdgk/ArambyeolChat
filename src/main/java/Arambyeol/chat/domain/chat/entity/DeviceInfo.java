package Arambyeol.chat.domain.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jdk.jfr.Name;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Entity
@Getter
@Setter
public class DeviceInfo {
	@Name("deviceId")
	@Id
	private String deviceId;
	private	String nickname;

	@Builder
	public DeviceInfo(String deviceId , String nickname){
		this.deviceId = deviceId;
		this.nickname = nickname;
	}
}
