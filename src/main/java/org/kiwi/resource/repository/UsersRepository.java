package org.kiwi.resource.repository;

import org.bson.types.ObjectId;
import org.kiwi.resource.domain.User;

public interface UsersRepository {
    User getUserById(ObjectId userId);
}
