package com.revature.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Model.Tasc;
import com.revature.utils.ConnectionSource;
import com.revature.utils.Dao;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TascService {
    private static final Logger logger = LogManager.getLogger(TascService.class);

    private Dao<Tasc> dao;
    private ObjectMapper mapper;
    private ConnectionSource conn = new ConnectionSource();

    public TascService() {
        dao = new Dao<>(Tasc.class);
        mapper = new ObjectMapper();
    }

    public void getTascs(HttpServletRequest req, HttpServletResponse resp) {
        if(req.getParameter("taskId") != null) {
            if(req.getParameter("taskId").equals("")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            try {
                String json = mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(getTasc(Integer.parseInt(req.getParameter("taskId"))));
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
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(getAllTascs());
                resp.getOutputStream().print(json);
            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    public Tasc insertTasc(HttpServletRequest req, HttpServletResponse resp) {
        Tasc result = null;
        try {
            StringBuilder builder = new StringBuilder();
            req.getReader().lines().collect(Collectors.toList()).forEach(builder::append);
            Tasc tasc = mapper.readValue(builder.toString(), Tasc.class);
            result = insertTasc(tasc);
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

    public Tasc updateTasc(HttpServletRequest req, HttpServletResponse resp) {
        Tasc result = null;
        StringBuilder builder = new StringBuilder();
        try {
            req.getReader().lines().collect(Collectors.toList()).forEach(builder::append);
            Tasc tasc = mapper.readValue(builder.toString(), Tasc.class);
            if(tasc.getId() != 0) {
                result = updateTasc(tasc);
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

    public void deleteTasc(HttpServletRequest req, HttpServletResponse resp) {
        StringBuilder builder = new StringBuilder();
        try {
            req.getReader().lines().collect(Collectors.toList()).forEach(builder::append);
            Tasc tasc = mapper.readValue(builder.toString(), Tasc.class);
            if(tasc != null) {
                deleteTasc(tasc.getId());
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Tasc> getAllTascs() {
        return dao.getAll(conn);
    }

    private Tasc getTasc(int id) {
        return dao.getById(conn, id);
    }

    private Tasc insertTasc(Tasc tasc) {
        dao.createTable(conn);
        return dao.insert(conn, tasc);
    }

    private Tasc updateTasc(Tasc tasc) {
        return dao.updateById(conn, tasc.getId(), tasc);
    }

    private void deleteTasc(int id) {
        dao.deleteById(conn, id);
    }



}
