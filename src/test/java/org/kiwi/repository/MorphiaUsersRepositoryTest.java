package org.kiwi.repository;

import org.junit.Before;
import org.junit.Test;
import org.kiwi.domain.User;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MorphiaUsersRepositoryTest extends MorphiaBaseTest {

    private MorphiaUsersRepository usersRepository;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        usersRepository = new MorphiaUsersRepository(datastore);
    }


    @Test
    public void should_get_user_by_id() {
        final User user = usersRepository.createUser(new User("kiwi"));

        final User userFromDb = usersRepository.getUserById(user.getId());

        assertThat(userFromDb.getName(), is("kiwi"));
    }
}
