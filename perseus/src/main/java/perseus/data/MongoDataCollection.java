//package perseus.data;
//
//import java.util.Arrays;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.MongoCredential;
//import com.mongodb.ServerAddress;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.MongoClient;
//
//import org.bson.Document;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MongoDataCollection {
//	
//	public MongoDataCollection() {
//		this.testConnection();
//	}
//
//	public void testConnection() {
//		String user = "tester";
//		String source = "news";
//		char[] password = "tester".toCharArray();
//		MongoCredential credential = MongoCredential.createCredential(user, source, password);
//		
//		MongoClient mongoClient = MongoClients.create(
//				MongoClientSettings.builder()
//                .applyToClusterSettings(builder ->
//                        builder.hosts(Arrays.asList(new ServerAddress("localhost", 27017))))
//                .credential(credential)
//                .build());
//		MongoDatabase database = mongoClient.getDatabase("news");
//		MongoCollection<Document> collection = database.getCollection("article");
//		System.out.println("Ho trovato questi articoli:");
//		// Spezzare in moduli/metodi separati prima di fare la query
//		// C'è da attendere un attimo prima di poter fare query
//		System.out.println(collection.countDocuments());
//		return;
//	}
//	
//}
