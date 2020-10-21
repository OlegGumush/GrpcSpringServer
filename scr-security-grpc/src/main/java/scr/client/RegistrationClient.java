package scr.client;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import scr.proto.registration.RegistrationModel;
import scr.proto.registration.RegistrationRequest;
import scr.proto.registration.RegistrationResponse;
import scr.proto.registration.RegistrationServiceGrpc;
import scr.proto.registration.RegistrationServiceGrpc.RegistrationServiceBlockingStub;
import scr.proto.registration.RegistrationServiceGrpc.RegistrationServiceStub;

public class RegistrationClient {

	public static void main(String[] args) {
		
		System.out.println("Hello I'm a gRpc SumUnaryClient");
		
		// create channel
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8082)
														// disable ssl
														.usePlaintext()
														.build();
		
		// create a greeting service client (blocking - synchronous)
		RegistrationServiceStub syncClient = RegistrationServiceGrpc.newStub(channel);

		RegistrationRequest request = RegistrationRequest.newBuilder()
														.setRegistrationModel(RegistrationModel.newBuilder()
																								.setEmail("oleg@gmail.com")
																								.setPassword("123456").build())
																								.build();
		final Long start = System.currentTimeMillis();
		
		try {
			for (int i = 0; i < 1; i++) {
				
				syncClient.register(request, new StreamObserver<RegistrationResponse>() {
					
					@Override
					public void onNext(RegistrationResponse value) {
						System.out.println("Result time :" + (System.currentTimeMillis() - start));					
					}
					
					@Override
					public void onError(Throwable t) {
					}
					
					@Override
					public void onCompleted() {
						
					}
				});											
			}			
		} catch (StatusRuntimeException e) {
			System.out.println("Error " + e.getMessage());
		}
		
		try {
			channel.awaitTermination(500000, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// The Java warning is harmless. Your application should continue running normally after the exception.
		// It would be nice to reduce the log level to have less logspam, but it also shouldn't impact the correctness of your application.
//		try {
//			System.out.println("Shutting down channel");
//			channel.shutdown().awaitTermination(15, TimeUnit.SECONDS);
//			System.out.println("The channel is showted down");
//		} catch (InterruptedException e) {
//		}
	}
}
