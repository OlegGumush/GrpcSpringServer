package scr.security.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import scr.proto.registration.RegistrationRequest;
import scr.proto.registration.RegistrationResponse;
import scr.proto.registration.RegistrationServiceGrpc.RegistrationServiceImplBase;
import scr.security.bl.RegistrationBL;

@GrpcService
public class RegistrationService extends RegistrationServiceImplBase {

	@Autowired
	private RegistrationBL registrationBL;

	@Override
	public void register(RegistrationRequest request, StreamObserver<RegistrationResponse> responseObserver) {

		System.out.println("Start Registration Service " + Thread.currentThread().getName() + " " + LocalDateTime.now());
		
		registrationBL.register(request.getRegistrationModel()).whenComplete((result, error) -> {
			
            if (error != null) {
            	responseObserver.onError(error);
            } else {
            	responseObserver.onNext(RegistrationResponse
								            			.newBuilder()
								            			.setIsSucceeded(true).build());
            }
            responseObserver.onCompleted();
        });
		
		System.out.println("End Registration Service " + Thread.currentThread().getName() + " " + LocalDateTime.now());
	}
}
