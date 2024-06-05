package Arambyeol.chat.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Arambyeol.chat.domain.chat.entity.DeviceInfo;

public interface DeviceInfoRepository extends JpaRepository<DeviceInfo,String> {
	Optional<DeviceInfo> findDeviceInfoByDeviceId(String deviceId);
}
