package org.kiwi.resource;

import org.bson.types.ObjectId;
import org.kiwi.resource.domain.User;
import org.kiwi.resource.repository.UsersRepository;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("users")
public class UsersResource {
    @Inject
    private UsersRepository usersRepository;

    @Path("{userId}/orders")
    public OrdersResource getOrdersResource(@PathParam("userId") ObjectId userId) {
        final User user = usersRepository.getUserById(userId);

        return new OrdersResource(user, usersRepository);
    }
}
