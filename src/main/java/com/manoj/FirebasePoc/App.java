package com.manoj.FirebasePoc;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.TopicManagementResponse;

/**
 * Firebase Topic Subscription Sample
 *
 */
public class App 
{
	private static final List<String> REG_TOKEN = new ArrayList<String>() {{
		add("RegTokenOfDeviceOne"); // Pixel Token
		add("RegTokenOfDeviceTwo"); // Nexus Token
	}};

	private static final String TOPIC = "MyTopic";

	public static void main( String[] args )
	{
		try {
			initializeFirebase();
			
			/*Comment and uncomment based on the action you want to perform*/
			
			// Subscribe to the topic
			//subscribeTopic();
			
			// Send Message to subscribed topic
//			sendMessageToTopic();
			
			// Unsubscribe from topic
			//unsubscribeTopic();
			
			System.out.println("Success");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initializeFirebase() throws Exception {
		String keyPath = "path/to/service_key.json";
		FileInputStream serviceAccount =
				new FileInputStream(keyPath);

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com")
				.build();

		FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
	}

	private static void subscribeTopic() throws Exception {
		TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(REG_TOKEN, TOPIC);
		System.out.println("Subscribed : " + response.getSuccessCount());
	}

	private static void unsubscribeTopic() throws Exception {
		TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(
				REG_TOKEN, TOPIC);

		System.out.println(response.getSuccessCount() + " tokens were unsubscribed successfully");
	}

	private static void sendMessageToTopic() throws Exception {
		System.out.println("Sending Message");
		Message message = Message.builder()
				.setAndroidConfig(AndroidConfig.builder()
						.setTtl(3600 * 1000) // 1 hour in milliseconds
						.setPriority(AndroidConfig.Priority.NORMAL)
						.setNotification(AndroidNotification.builder()
								.setTitle("Notification Title")
								.setBody("Notification Body")
								.setColor("#f45342")
								.build())
						.build())
				.setTopic(TOPIC)
				.build();

		String response = FirebaseMessaging.getInstance().send(message);
		// Response is a message ID string.
		System.out.println("Successfully sent message: " + response);
	}
}
