package org.kiwi.resource.repository;

import org.bson.types.ObjectId;
import org.kiwi.domain.Order;
import org.kiwi.domain.Payment;
import org.kiwi.domain.User;

public interface UsersRepository {
    User getUserById(ObjectId userId);

    User createUser(User user);

    Order placeOrder(User user, Order order);

    Payment payOrder(User user, Order order, Payment payment);
}
