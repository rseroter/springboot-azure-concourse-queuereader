package seroter.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.microsoft.windowsazure.core.utils.StreamUtils;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveQueueMessageResult;

@SpringBootApplication
@PropertySource("secure.properties")
public class SpringbootAzureConcourseQueuereaderApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootAzureConcourseQueuereaderApplication.class, args);
	}
	
	@Value("${sbNamespace}")
	private String sbNamespace;
	
	@Value("${sbSasKeyName}")
	private String sbSasKeyName;
	
	@Value("${sbSaSKey}")
	private String sbSaSKey;
	

	@Override
	public void run(String... arg0) throws Exception {
		
		//connect to service bus
		com.microsoft.windowsazure.Configuration config = 
			//namespace, sasKeyName, sasKey, serviceBusRootUri
			ServiceBusConfiguration.configureWithSASAuthentication(
					sbNamespace, 
					sbSasKeyName, 
					sbSaSKey, 
					".servicebus.windows.net");
		
		ServiceBusContract svc = ServiceBusService.create(config);
		
		//get message from the queue
		ReceiveQueueMessageResult result = svc.receiveQueueMessage("demoqueue");

		if(result.getValue() == null) {
			System.out.println("No message to read!");
		}
		else {

			BrokeredMessage m = result.getValue();
			String payload = StreamUtils.toString(m.getBody());
			System.out.println("message is " + payload);
		}
	}
}
