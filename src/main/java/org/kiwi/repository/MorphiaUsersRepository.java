package org.kiwi.repository;

import org.bson.types.ObjectId;
import org.kiwi.domain.Order;
import org.kiwi.domain.Payment;
import org.kiwi.domain.User;
import org.mongodb.morphia.Datastore;

import static org.kiwi.domain.OrderWithId.orderWithId;

public class MorphiaUsersRepository implements UsersRepository {
    private final Datastore datastore;

    public MorphiaUsersRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public User getUserById(ObjectId userId) {
        return datastore.get(User.class, userId);
    }

    @Override
    public User createUser(User user) {
        datastore.save(user);
        return user;
    }

    @Override
    public Order placeOrder(User user, Order order) {
        final Order orderWithId = orderWithId(new ObjectId().toString(), order);
        user.placeOrder(orderWithId);
        datastore.save(user);
        return orderWithId;
    }

    @Override
    public Payment payOrder(User user, Order order, Payment payment) {
        final Order orderById = user.getOrderById(order.getId());
        orderById.pay(payment);
        datastore.save(user);
        return payment;
    }
}
