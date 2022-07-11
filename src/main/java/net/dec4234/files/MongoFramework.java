package net.dec4234.files;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.UUID;

public class MongoFramework {

	protected MongoClient mc;
	protected MongoDatabase md;
	protected MongoCollection<Document> collection;

	public MongoFramework(MongoClient mc, MongoDatabase md, MongoCollection<Document> collection) {
		this.mc = mc;
		this.md = md;
		this.collection = collection;
	}

	protected MongoClient getMongoClient() {
		return mc;
	}

	protected MongoDatabase getMongoDatabse() {
		return md;
	}

	public boolean hasData(UUID uuid) {
		Document doc = new Document("UUID", uuid.toString());
		return collection.find(doc).first() != null;
	}

	public void insertDefaultStats(UUID uuid) {
		if(!hasData(uuid)) {
			Document doc = new Document("UUID", uuid.toString())
					.append("kills", 0);

			collection.insertOne(doc);
		}
	}

	public void replaceData(UUID uuid, Document doc) {
		collection.replaceOne(new Document("UUID", uuid.toString()), doc);
	}

	public Document getDoc(String query) {
		Document doc = new Document("UUID", query);
		return collection.find(doc).first();
	}

	public Document getSubDoc(UUID uuid, String subDocName) {
		Document doc = getDoc(uuid.toString());
		return (Document) doc.get(subDocName);
	}
}
