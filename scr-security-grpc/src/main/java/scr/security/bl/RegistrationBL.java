package scr.security.bl;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.grpc.Status;
import scr.proto.registration.RegistrationModel;
import scr.security.model.IdModel;
import scr.security.model.UserModel;
import scr.security.repository.AsyncRegistrationRepository;

@Service
public class RegistrationBL {

	@Autowired
	private AsyncRegistrationRepository registrationRepo;
	
	public CompletableFuture<ResponseEntity<IdModel>> register(RegistrationModel registrationModel) {
		
		System.out.println("Start Registration BL " + Thread.currentThread().getName() + " " + LocalDateTime.now());
		
		if (Strings.isEmpty(registrationModel.getEmail()) || Strings.isEmpty(registrationModel.getPassword())) {
			
			throw Status.INVALID_ARGUMENT.asRuntimeException();
		}
		
		CompletableFuture<ResponseEntity<IdModel>> response = registrationRepo.isUserExists(registrationModel.getEmail()).thenCompose(responseFromisExistsApi -> {
			
			System.out.println("Result from isUserExists API " + responseFromisExistsApi.getBody().isExists() + " " + Thread.currentThread().getName() + " " + LocalDateTime.now());

			if (responseFromisExistsApi.getStatusCode() == HttpStatus.OK && !responseFromisExistsApi.getBody().isExists()) {
				
				return registrationRepo.createUser(new UserModel(registrationModel.getEmail(), registrationModel.getPassword()));				
			}
			throw Status.ABORTED.asRuntimeException();
		
		}).thenCompose(responseFromCreateUser -> {

			System.out.println("Result from CreateUser API " + responseFromCreateUser.getBody().getId() + " " + Thread.currentThread().getName() + " " + LocalDateTime.now());

			if (responseFromCreateUser.getStatusCode() == HttpStatus.CREATED && responseFromCreateUser.getBody().getId() > 0) {
				
				return registrationRepo.getUserPassword(registrationModel.getPassword());				
			}
			throw Status.ABORTED.asRuntimeException();
		
		}).thenCompose(responseFromGetUserPassword -> {
			
			String password = responseFromGetUserPassword.getBody().getPassword();
			System.out.println("Result from DeleteUser API " + password + " " + Thread.currentThread().getName() + " " + LocalDateTime.now());

			if (responseFromGetUserPassword.getStatusCode() == HttpStatus.OK && password != null) {
				
				return registrationRepo.deleteUserApi(registrationModel.getEmail());				
			}
			throw Status.ABORTED.asRuntimeException();
		});
															
		System.out.println("End Registration BL " + Thread.currentThread().getName() + " " + LocalDateTime.now());

		return response;
	}
}
