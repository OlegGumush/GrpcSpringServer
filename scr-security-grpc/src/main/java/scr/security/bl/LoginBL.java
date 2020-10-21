package scr.security.bl;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.grpc.Status;
import scr.security.model.request.LoginModel;
import scr.security.model.response.JwtTokenModel;
import scr.security.repository.AsyncLoginRepository;

@Service
public class LoginBL {
	
	@Autowired
	private AsyncLoginRepository loginRepo;

	public CompletableFuture<ResponseEntity<JwtTokenModel>> login(String username, String password, String farmId ) {
			
		System.out.println("Start Registration BL " + Thread.currentThread().getName() + " " + LocalDateTime.now());
		
		if (Strings.isEmpty(username) || Strings.isEmpty(password) || Strings.isEmpty(farmId)) {
			
			throw Status.INVALID_ARGUMENT.asRuntimeException();
		}
		
		CompletableFuture<ResponseEntity<JwtTokenModel>> response = loginRepo.login(new LoginModel(username, password, farmId));
		
		return response;
	}
}
