package Arambyeol.chat.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Arambyeol.chat.domain.auth.dto.JwtToken;
import Arambyeol.chat.domain.chat.dto.SuccessSingleResponse;
import Arambyeol.chat.domain.auth.service.LoginService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthController {
	private final LoginService loginService;

	@GetMapping("/login")
	public ResponseEntity<SuccessSingleResponse<JwtToken>> login( @RequestParam String deviceId){
		JwtToken jwtToken = loginService.login(deviceId);
		return ResponseEntity.ok().body(new SuccessSingleResponse<>(HttpStatus.OK.getReasonPhrase(), jwtToken));
	}
	@GetMapping("/generateAccessToken")
	public ResponseEntity<SuccessSingleResponse<JwtToken>> generateAccessToken( @RequestHeader("Authorization") String refreshToken){
		JwtToken jwtToken = loginService.generateNewAccessToken(refreshToken);
		return ResponseEntity.ok().body(new SuccessSingleResponse<>(HttpStatus.OK.getReasonPhrase(), jwtToken));
	}
}