package org.kiwi.resource.repository;

import com.mongodb.*;
import org.bson.types.ObjectId;
import org.kiwi.domain.Order;
import org.kiwi.domain.OrderItem;
import org.kiwi.domain.Payment;
import org.kiwi.domain.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.kiwi.domain.OrderWithId.orderWithId;
import static org.kiwi.domain.UserWithId.userWithId;

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
        final Order order = new Order((String) orderDocument.get("receiver"),
                (String) orderDocument.get("shippingAddress"),
                Timestamp.valueOf((String) orderDocument.get("createdAt")), mapOrderItemsFromDocumentList((BasicDBList) orderDocument.get("orderItems")));

        if (orderDocument.get("payment") != null) {
            order.pay(mapPaymentFromDocument((DBObject) orderDocument.get("payment")));
        }

        return orderWithId(orderDocument.get("_id").toString(), order);
    }

    private Payment mapPaymentFromDocument(DBObject paymentDocument) {
        return new Payment((String) paymentDocument.get("paymentType"),
                (int) paymentDocument.get("amount"),
                Timestamp.valueOf((String) paymentDocument.get("createdAt")));
    }

    private List<OrderItem> mapOrderItemsFromDocumentList(BasicDBList orderItemsDocumentList) {
        final List<OrderItem> orderItems = new ArrayList<>();

        for (Object orderItem : orderItemsDocumentList) {
            orderItems.add(mapOrderItemFromDocument((DBObject) orderItem));
        }

        return orderItems;
    }

    private OrderItem mapOrderItemFromDocument(DBObject orderItem) {
        return new OrderItem((ObjectId) ((DBRef) orderItem.get("productId")).fetch().get("_id"),
                (int) orderItem.get("quantity"),
                (int) orderItem.get("price"));
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

    @Override
    public Payment payOrder(User user, Order order, Payment payment) {
        final DBObject userDocument = db.getCollection("users").findOne(new BasicDBObject("_id", user.getId()));

        final BasicDBList ordersDocumentList = (BasicDBList) userDocument.get("orders");

        for (Object anOrder : ordersDocumentList) {
            final DBObject dbOrder = (DBObject) anOrder;
            if (dbOrder.get("_id").toString().equals(order.getId().toString())) {
                order.pay(payment);
                dbOrder.put("payment", mapPaymentToDocument(payment));
                break;
            }
        }

        db.getCollection("users").findAndModify(new BasicDBObject("_id", user.getId()), userDocument);

        return payment;
    }

    private DBObject mapPaymentToDocument(Payment payment) {
        return new BasicDBObjectBuilder()
                .add("paymentType", payment.getPaymentType())
                .add("amount", payment.getAmount())
                .add("createdAt", payment.getCreatedAt().toString())
                .get();
    }

    private DBObject mapOrderToDocument(ObjectId orderId, Order order) {
        return new BasicDBObjectBuilder()
                .add("_id", orderId)
                .add("receiver", order.getReceiver())
                .add("shippingAddress", order.getShippingAddress())
                .add("createdAt", order.getCreatedAt().toString())
                .add("orderItems", mapOrderItemsToDocumentList(order.getOrderItems()))
                .get();
    }

    private BasicDBList mapOrderItemsToDocumentList(List<OrderItem> orderItems) {
        final BasicDBList orderItemsDocumentList = new BasicDBList();

        for (OrderItem orderItem : orderItems) {
            orderItemsDocumentList.add(mapOrderItemToDocument(orderItem));
        }

        return orderItemsDocumentList;
    }

    private DBObject mapOrderItemToDocument(OrderItem orderItem) {
        return new BasicDBObjectBuilder()
                .add("productId", new DBRef(db, "products", orderItem.getProductId()))
                .add("quantity", orderItem.getQuantity())
                .add("price", orderItem.getPrice())
                .get();
    }

}
