package scr.security.repository;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.AsyncRestTemplate;

import scr.security.model.IdModel;
import scr.security.model.IsUserExistsModel;
import scr.security.model.UserModel;
import scr.security.model.UserPasswordModel;
import scr.security.repository.base.AsyncRepository;

@Repository
public class AsyncRegistrationRepository extends AsyncRepository {
	
	private final static String IS_USER_EXISTS_API = BASE_URL + "isUserExist/%s/"; 
	private final static String CREATE_USER_API = BASE_URL + "user/"; 
	private final static String USER_PASSWORD_API = BASE_URL + "user/password/%s/"; 
	private final static String DELETE_USER_API = CREATE_USER_API + "%s"; 

	@Autowired
	private AsyncRestTemplate templ;
	
	@Async("repoExecutor")
	public CompletableFuture<ResponseEntity<IsUserExistsModel>> isUserExists(String email) {
		
		System.out.println("Start isUserExists function " + Thread.currentThread().getName() + " " + LocalDateTime.now());
		
		ResponseEntity<IsUserExistsModel> result = getRequest(String.format(IS_USER_EXISTS_API, email), IsUserExistsModel.class);
		
		System.out.println("End isUserExists function " + Thread.currentThread().getName() + " " + LocalDateTime.now());

	    return CompletableFuture.completedFuture(result);
	}

	@Async("repoExecutor")
	public CompletableFuture<ResponseEntity<IdModel>> createUser(UserModel userModel) {
		
		System.out.println("Start createUser function " + Thread.currentThread().getName() + " " + LocalDateTime.now());
		
		ResponseEntity<IdModel> result = postRequest(CREATE_USER_API,
																IdModel.class,
																userModel);
		
		System.out.println("End createUser function " + Thread.currentThread().getName() + " " + LocalDateTime.now());

	    return CompletableFuture.completedFuture(result);
	}
	
	@Async("repoExecutor")
	public CompletableFuture<ResponseEntity<UserPasswordModel>> getUserPassword(String email) {
		
		System.out.println("Start getUserPassword function " + Thread.currentThread().getName() + " " + LocalDateTime.now());
		
		ResponseEntity<UserPasswordModel> result = getRequest(String.format(USER_PASSWORD_API, email), UserPasswordModel.class);
		
		System.out.println("End getUserPassword function " + Thread.currentThread().getName() + " " + LocalDateTime.now());

	    return CompletableFuture.completedFuture(result);
	}
	
	@Async("repoExecutor")
	public CompletableFuture<ResponseEntity<IdModel>> deleteUserApi(String email) {
		
		System.out.println("Start deleteUserApi function " + Thread.currentThread().getName() + " " + LocalDateTime.now());
		
		ResponseEntity<IdModel> result = deleteRequest(String.format(DELETE_USER_API, email), IdModel.class);
		
		System.out.println("End getUserPassword function " + Thread.currentThread().getName() + " " + LocalDateTime.now());

	    return CompletableFuture.completedFuture(result);
	}
	
	public ResponseEntity<IsUserExistsModel> isUserExists1(String email) {
		
		System.out.println("Start isUserExists function " + Thread.currentThread().getName() + " " + LocalDateTime.now());
		
		ResponseEntity<IsUserExistsModel> result = getRequest(String.format(IS_USER_EXISTS_API, email), IsUserExistsModel.class);
		
		System.out.println("End isUserExists function " + Thread.currentThread().getName() + " " + LocalDateTime.now());

	    return result;
	}

	public ResponseEntity<IdModel> createUser1(UserModel userModel) {
		
		System.out.println("Start createUser function " + Thread.currentThread().getName() + " " + LocalDateTime.now());
		
		new HttpEntity<>(userModel, getHeaders());
		ResponseEntity<IdModel> result = restTemplate.exchange(CREATE_USER_API,
																HttpMethod.POST,
																new HttpEntity<>(userModel, getHeaders()),
																IdModel.class);
		
		System.out.println("End createUser function " + Thread.currentThread().getName() + " " + LocalDateTime.now());

	    return result;
	}
	
	public ResponseEntity<UserPasswordModel> getUserPassword1(String email) {
		
		System.out.println("Start getUserPassword function " + Thread.currentThread().getName() + " " + LocalDateTime.now());
		
		ResponseEntity<UserPasswordModel> result = getRequest(String.format(USER_PASSWORD_API, email), UserPasswordModel.class);
		
		System.out.println("End getUserPassword function " + Thread.currentThread().getName() + " " + LocalDateTime.now());

	    return result;
	}
	

	public ResponseEntity<IdModel> deleteUserApi1(String email) {
		
		System.out.println("Start deleteUserApi function " + Thread.currentThread().getName() + " " + LocalDateTime.now());
		
		ResponseEntity<IdModel> result = deleteRequest(String.format(DELETE_USER_API, email), IdModel.class);
		
		System.out.println("End getUserPassword function " + Thread.currentThread().getName() + " " + LocalDateTime.now());

	    return result;
	}

//	private CompletableFuture<ResponseEntity<UserModel>> callApi(HttpEntity<Data> request) {
//		
//		return CompletableFuture.supplyAsync(() -> {
//			
//			return restTemplate.exchange("http://localhost:8090/api10seconds", HttpMethod.GET, request, UserModel.class);
//		}, Executors.newFixedThreadPool(10));
//	}
	
}
