package com.revature.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Model.User;
import com.revature.utils.ConnectionSource;
import com.revature.utils.Dao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    UserService service;
    @Mock
    Dao<User> userDaoMock;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private ConnectionSource connectionSource;

    @Test
    public void testInsertUser() {
        User user = new User("jared", "m");
        when(service.insertUser(user)).thenReturn(user);
        User u = service.insertUser(user);
        Assert.assertEquals("m", u.getLastName());
        Assert.assertEquals("jared", u.getFirstName());
        verify(userDaoMock, times(1)).insert(connectionSource, user);
    }

    @Test
    public void testGetUser() {
        User user = new User("obed", "mullins");
        when(service.getUser(0)).thenReturn(user);
        User u = service.getUser(0);
        Assert.assertEquals("obed", u.getFirstName());
        Assert.assertEquals("mullins", u.getLastName());
        verify(userDaoMock, times(1)).getById(connectionSource, 0);
    }

    @Test
    public void testGetAll() {
        ArrayList<User> list = new ArrayList<>();
        User jared = new User("jared", "mullins");
        User chelsea = new User("chelsea", "mullins");
        list.add(jared);
        list.add(chelsea);
        when(userDaoMock.getAll(connectionSource)).thenReturn(list);
        List<User> userList = service.getUsers();
        Assert.assertEquals(2, userList.size());
        verify(userDaoMock, times(1)).getAll(connectionSource);
    }

    @Test
    public void testUpdateUser() {
        User user = new User("Jared", "Mullins");
        when(userDaoMock.updateById(connectionSource, 0, user)).thenReturn(user);
        Assert.assertEquals("Jared", user.getFirstName());
        Assert.assertEquals("Mullins", user.getLastName());
        Assert.assertEquals(0, user.getId());

        user.setFirstName("Chelsea");
        when(userDaoMock.updateById(connectionSource, 0, user)).thenReturn(user);
        Assert.assertEquals("Chelsea", user.getFirstName());
        Assert.assertEquals("Mullins", user.getLastName());
        Assert.assertEquals(0, user.getId());
    }

    @Test
    public void testDeleteUser() {
        service.deleteUser(5);
        verify(userDaoMock, times(1)).deleteById(connectionSource, 5);
    }

}