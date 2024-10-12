package mock.claimrequest;

import mock.claimrequest.entity.ProjectRole;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MockClaimRequestApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockClaimRequestApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
