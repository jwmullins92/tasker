package com.revature.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Model.ToDo;
import com.revature.utils.ConnectionSource;
import com.revature.utils.Dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ToDoService {

    Dao<ToDo> dao;
    private ObjectMapper mapper;
    private ConnectionSource connectionSource = new ConnectionSource(
            "jdbc:postgresql://database-1.c5mz1ul4etaa.us-east-2.rds.amazonaws.com:5432/postgres?currentSchema=tasker",
            System.getenv("db_username"),
            System.getenv("db_password")
    );

    public ToDoService() {
        this.dao = new Dao<>(ToDo.class);
        this.mapper = new ObjectMapper();
    }

    public void getToDos(HttpServletRequest req, HttpServletResponse resp) {
        if(req.getParameter("toDoId") != null) {
            if(req.getParameter("toDoId").equals("")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            try {
            String json = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(getToDo(Integer.parseInt(req.getParameter("toDoId"))));
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
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(getToDos());
                resp.getOutputStream().print(json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Object getToDo(int toDoId) {
        return dao.getById(connectionSource, toDoId);
    }

    private List<ToDo> getToDos() {
        return dao.getAll(connectionSource);
    }

    public ToDo insertToDo(HttpServletRequest req, HttpServletResponse resp) {
        ToDo result = null;
        try {
            StringBuilder builder = new StringBuilder();
            req.getReader().lines().collect(Collectors.toList()).forEach(builder::append);
            ToDo toDo = mapper.readValue(builder.toString(), ToDo.class);
            result = insert(toDo);
            if(result != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ToDo insert(ToDo toDo) {
        dao.createTable(connectionSource);
        return dao.insert(connectionSource, toDo);
    }

    public ToDo updateToDo(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("in public service update");
        ToDo result = null;
        StringBuilder builder = new StringBuilder();
        try {
            req.getReader().lines().collect(Collectors.toList()).forEach(builder::append);
            ToDo toDo = mapper.readValue(builder.toString(), ToDo.class);
            if(toDo.getId() != 0) {
                System.out.println("ID does not equal 0");
                result = update(toDo);
                System.out.println(result);
                if(result != null) {
                    System.out.println("Result is not null");
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

    private ToDo update(ToDo toDo) {
        return dao.updateById(connectionSource, toDo.getId(), toDo);
    }

    public void deleteToDo(HttpServletRequest req, HttpServletResponse resp) {
        StringBuilder builder = new StringBuilder();
        try {
            req.getReader().lines().collect(Collectors.toList()).forEach(builder::append);
            ToDo toDo = mapper.readValue(builder.toString(), ToDo.class);
            if(toDo != null) {
                delete(toDo.getId());
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void delete(int id) {
        dao.deleteById(connectionSource, id);
    }
}
