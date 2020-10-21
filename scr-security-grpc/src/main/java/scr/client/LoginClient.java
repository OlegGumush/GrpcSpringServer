package scr.client;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import scr.proto.login.LoginRequest;
import scr.proto.login.LoginResponse;
import scr.proto.login.LoginServiceGrpc;
import scr.proto.login.LoginServiceGrpc.LoginServiceBlockingStub;

public class LoginClient {

	public static void main(String[] args) {
		
		System.out.println("Hello I'm a gRpc SumUnaryClient");
		
		// create channel
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8082)
														// disable ssl
														.usePlaintext()
														.build();
		
		LoginServiceBlockingStub syncClient = LoginServiceGrpc.newBlockingStub(channel);

		LoginRequest request = LoginRequest.newBuilder().setFarmId("EU123456")
														.setPassword("123456")
														.setUsername("Oleg")
														.build();
		try {
			LoginResponse response = syncClient.login(request);						
			System.out.println("Respone " + response.getToken());
			
		} catch (StatusRuntimeException e) {
			System.out.println("Error " + e.getMessage());
		}
		
		// The Java warning is harmless. Your application should continue running normally after the exception.
		// It would be nice to reduce the log level to have less logspam, but it also shouldn't impact the correctness of your application.
		try {
			System.out.println("Shutting down channel");
			channel.shutdown().awaitTermination(15, TimeUnit.SECONDS);
			System.out.println("The channel is showted down");
		} catch (InterruptedException e) {
			
		}
	}
}
