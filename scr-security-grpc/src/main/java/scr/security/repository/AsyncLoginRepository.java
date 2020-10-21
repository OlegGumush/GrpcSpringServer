package scr.security.repository;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import scr.security.model.request.LoginModel;
import scr.security.model.response.JwtTokenModel;
import scr.security.repository.base.AsyncRepository;

@Repository
public class AsyncLoginRepository extends AsyncRepository {
	
	private final static String LOGIN_API = BASE_URL + "login/";  

	@Async("repoExecutor")
	public CompletableFuture<ResponseEntity<JwtTokenModel>> login(LoginModel model) {
		
		System.out.println("Start createUser function " + Thread.currentThread().getName() + " " + LocalDateTime.now());

		System.out.println(LOGIN_API);
		System.out.println(model);
		ResponseEntity<JwtTokenModel> result = postRequest(LOGIN_API,
															JwtTokenModel.class,
															model);
		
		System.out.println("End createUser function " + Thread.currentThread().getName() + " " + LocalDateTime.now());
		return CompletableFuture.completedFuture(result);
	}
}
