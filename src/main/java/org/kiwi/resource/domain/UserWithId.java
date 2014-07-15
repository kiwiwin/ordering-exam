package org.kiwi.resource.domain;

import org.bson.types.ObjectId;

public class UserWithId {
    private static User userWithId(ObjectId id, User user) {
        user.id = id;
        return user;
    }

    public static User userWithId(String id, User user) {
        return userWithId(new ObjectId(id), user);
    }
}
