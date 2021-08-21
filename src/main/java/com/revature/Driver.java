package com.revature;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

public class Driver {

    private static final Logger logger = LogManager.getLogger(Driver.class);

    public static void main(String[] args) {

        ArrayList<Object> test = new ArrayList<>();

        test.add("\'hello\'");
        test.add(true);
        test.add(44);

//        System.out.println(String.join(", ", test.toString()));

        String s = Arrays.toString(test.toArray()).replace("[", "").replace("]", "");

        System.out.println(s);

//        Dao<ToDo> tDao = new Dao<>(ToDo.class);
//        ToDo toDo = new ToDo("Clean", "Clean it good");
//
//        System.out.println(toDo.getId());
//
//        tDao.createTable(new ConnectionSource());
//
//        toDo = tDao.insert(new ConnectionSource(), toDo);
//
//
//
//        System.out.println(toDo.getTaskDescription());
//
//        System.out.println(toDo.getId());
//
//        toDo.setTaskDescription("Do it better this time");
//
//        toDo = tDao.updateById(new ConnectionSource(), toDo.getId(), toDo);
//
//        System.out.println(toDo.getTaskDescription());


    }
}
