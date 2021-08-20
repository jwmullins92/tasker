package com.revature.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Driver;
import com.revature.Model.User;
import com.revature.utils.ConnectionSource;
import com.revature.utils.Dao;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private static final Logger logger = LogManager.getLogger(Driver.class);

    Dao<User> dao;
    private ObjectMapper mapper;
    private ConnectionSource connectionSource = new ConnectionSource(
            "jdbc:postgresql://database-1.c5mz1ul4etaa.us-east-2.rds.amazonaws.com:5432/postgres?currentSchema=tasker",
            System.getenv("db_username"),
            System.getenv("db_password")
    );

    public UserService() {
        dao = new Dao<>(User.class);
        mapper = new ObjectMapper();
    }

    public void getUsers(HttpServletRequest req, HttpServletResponse resp) {
        if(req.getParameter("userId") != null) {
            System.out.println("id is not null");
            if(req.getParameter("userId").equals("")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                System.out.println("id params are empty");
                return;
            }
            try {
                System.out.println("looking for user");
                String json = mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(getUser(Integer.parseInt(req.getParameter("userId"))));
                if(json.equals("null")) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                resp.getOutputStream().print(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(getUsers());
                resp.getOutputStream().print(json);
            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    public User insertUser(HttpServletRequest req, HttpServletResponse resp) {
        User result = null;
        try {
            StringBuilder builder = new StringBuilder();
            req.getReader().lines().collect(Collectors.toList()).forEach(builder::append);
            User user = mapper.readValue(builder.toString(), User.class);
            result = insertUser(user);
            if(result != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public User updateUser(HttpServletRequest req, HttpServletResponse resp) {
        User result = null;
        StringBuilder builder = new StringBuilder();
        try {
            req.getReader().lines().collect(Collectors.toList()).forEach(builder::append);
            User user = mapper.readValue(builder.toString(), User.class);
            if(user.getId() != 0) {
                result = updateUser(user);
                if(result != null) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
                    resp.getWriter().print(json);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void deleteUser(HttpServletRequest req, HttpServletResponse resp) {
        StringBuilder builder = new StringBuilder();
        try {
            req.getReader().lines().collect(Collectors.toList()).forEach(builder::append);
            User user = mapper.readValue(builder.toString(), User.class);
            if(user != null) {
                deleteUser(user.getId());
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private User insertUser(User user) {
        dao.createTable(connectionSource);
        return dao.insert(connectionSource, user);
    }

    private User getUser(int id) {
        return dao.getById(connectionSource, id);
    }

    private List<User> getUsers() {
        return dao.getAll(connectionSource);
    }

    private User updateUser(User user) {
        return dao.updateById(connectionSource, user.getId(), user);
    }

    private void deleteUser(int id) {
        dao.deleteById(connectionSource, id);
    }
}
