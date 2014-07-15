package org.kiwi.resource.repository;

import com.mongodb.*;
import org.bson.types.ObjectId;
import org.kiwi.resource.domain.Order;
import org.kiwi.resource.domain.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.kiwi.resource.domain.OrderWithId.orderWithId;
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
        final List<Order> orders = mapOrdersFromDocumentList((BasicDBList) userDocument.get("orders"));

        final User user = new User(name);

        for (Order order : orders) {
            user.placeOrder(order);
        }

        return userWithId(id.toString(), user);
    }

    private List<Order> mapOrdersFromDocumentList(BasicDBList ordersDocumentList) {
        final List<Order> orders = new ArrayList<>();

        for (Object order : ordersDocumentList) {
            final DBObject orderDocument = (DBObject) order;
            orders.add(mapOrderFromDocument(orderDocument));
        }

        return orders;
    }

    private Order mapOrderFromDocument(DBObject orderDocument) {
        return orderWithId(orderDocument.get("_id").toString(), new Order((String) orderDocument.get("receiver"),
                (String) orderDocument.get("shippingAddress"),
                Timestamp.valueOf((String) orderDocument.get("createdAt")), null));
    }

    @Override
    public User createUser(User user) {
        final DBObject userDocument = new BasicDBObjectBuilder()
                .add("name", user.getName())
                .get();

        db.getCollection("users").insert(userDocument);

        return userWithId(userDocument.get("_id").toString(), user);
    }

    @Override
    public Order placeOrder(User user, Order order) {
        final DBObject userDocument = db.getCollection("users").findOne(new BasicDBObject("_id", user.getId()));

        ObjectId orderId = new ObjectId();
        final DBObject orderDocument = mapOrderToDocument(orderId, order);

        BasicDBList ordersDocumentList = (BasicDBList) userDocument.get("orders");

        if (ordersDocumentList == null) {
            ordersDocumentList = new BasicDBList();
        }

        ordersDocumentList.add(orderDocument);

        userDocument.put("orders", ordersDocumentList);

        db.getCollection("users").findAndModify(new BasicDBObject("_id", user.getId()), userDocument);

        return orderWithId(orderId.toString(), order);
    }

    private DBObject mapOrderToDocument(ObjectId orderId, Order order) {
        return new BasicDBObjectBuilder()
                .add("_id", orderId)
                .add("receiver", order.getReceiver())
                .add("shippingAddress", order.getShippingAddress())
                .add("createdAt", order.getCreatedAt().toString())
                .get();
    }

}
