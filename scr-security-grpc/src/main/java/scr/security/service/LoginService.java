package scr.security.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import scr.proto.login.LoginRequest;
import scr.proto.login.LoginResponse;
import scr.proto.login.LoginServiceGrpc.LoginServiceImplBase;
import scr.security.bl.LoginBL;

@GrpcService
public class LoginService extends LoginServiceImplBase {

	@Autowired
	private LoginBL loginBL;
		
	@Override
	public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
			
		System.out.println("Start LoginService " + Thread.currentThread().getName() + " " + LocalDateTime.now());

		loginBL.login(request.getUsername(), request.getPassword(), request.getFarmId())
				.whenComplete((result, error) -> {
		
            if (error != null) {
            	responseObserver.onError(error);
            } else {
            	responseObserver.onNext(LoginResponse.newBuilder()
							            			.setToken(result.getBody().getToken())
							            			.build());
            }
            responseObserver.onCompleted();
        });
	}
}
