package org.kiwi.resource.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.kiwi.resource.domain.User;

import static org.kiwi.resource.domain.UserWithId.userWithId;

public class MongoUsersRepository implements UsersRepository {
    private final DB db;

    public MongoUsersRepository(DB db) {
        this.db = db;
    }

    @Override
    public User getUserById(ObjectId userId) {
        final DBObject userDocument = db.getCollection("users").findOne(new BasicDBObject("_id", userId));

        return mapUserDocument(userDocument);

    }

    private User mapUserDocument(DBObject userDocument) {
        final String name = (String) userDocument.get("name");
        final Object id = userDocument.get("_id");

        return userWithId(id.toString(), new User(name));
    }
    
    @Override
    public User createUser(User user) {
        final DBObject userDocument = new BasicDBObjectBuilder()
                .add("name", user.getName())
                .get();

        db.getCollection("users").insert(userDocument);

        return userWithId(userDocument.get("_id").toString(), user);
    }
}
