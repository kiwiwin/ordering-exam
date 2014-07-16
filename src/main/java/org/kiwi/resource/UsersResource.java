package org.kiwi.resource;

import org.bson.types.ObjectId;
import org.kiwi.domain.User;
import org.kiwi.repository.ProductsRepository;
import org.kiwi.repository.UsersRepository;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("users")
public class UsersResource {
    @Inject
    private UsersRepository usersRepository;

    @Inject
    private ProductsRepository productsRepository;

    @Path("{userId}/orders")
    public OrdersResource getOrdersResource(@PathParam("userId") ObjectId userId) {
        final User user = usersRepository.getUserById(userId);

        return new OrdersResource(user, usersRepository, productsRepository);
    }
}
