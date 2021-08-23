package com.revature.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Model.Tasc;
import com.revature.utils.ConnectionSource;
import com.revature.utils.Dao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TascServiceTest {

    @InjectMocks
    TascService service;
    @Mock
    Dao<Tasc> tascDaoMock;
    @Mock
    private ConnectionSource connectionSource;


    @Test
    public void testGetTascsWithParameters() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        StringBuilder builder = new StringBuilder();

        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockRes = mock(HttpServletResponse.class);
        Tasc tasc = new Tasc("Clean house", "Vacuum floors", 3, false);

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tasc);
        System.out.println(json);

        when(mockReq.getParameter("taskId")).thenReturn("1");
        when(mockReq.getReader().lines()).thenReturn((Stream<String>) tasc);

//        StringWriter stringWriter = new StringWriter();
//        PrintWriter writer = new PrintWriter(stringWriter);
//        when(mockRes.getWriter()).thenReturn(writer);

        service.insertTasc(mockReq, mockRes);

        verify(mockReq, atLeast(1)).getParameter("taskId");

//        Tasc t = service.insertTasc(mockReq, mockRes);
//        Assert.assertEquals("Clean house", t.getTask());
//        Assert.assertEquals("Vacuum floors", t.getDescription());
//        Assert.assertFalse(t.getCompleted());
//        Assert.assertEquals(3, t.getPriority());


//        req.setAttribute();
//
//        when(req.getReader().lines())

//        Tasc tasc = new Tasc("Clean house", "Vacuum floors", 2, false);
//        when(service.insertTasc(req, res)).thenReturn(tasc);
//        Tasc t = service.insertTasc(req, res);
//        Assert.assertEquals("Clean house", tasc.getTask());
//        Assert.assertEquals("Vacuum floors", tasc.getDescription());
//        Assert.assertFalse(tasc.getCompleted());
    }


    @Test
    public void testInsertTascFromProtectedMethod() {
        Tasc tasc = new Tasc("Clean house", "Vacuum floors", 3, false);
        when(service.insertTasc(tasc)).thenReturn(tasc);
        Tasc t = service.insertTasc(tasc);
        Assert.assertEquals("Clean house", t.getTask());
        Assert.assertEquals("Vacuum floors", t.getDescription());
        Assert.assertFalse(t.getCompleted());
        Assert.assertEquals(3, t.getPriority());
        verify(tascDaoMock, times(1)).insert(connectionSource, tasc);
    }

    @Test
    public void getTascs(){
        Tasc tasc = new Tasc("Clean house", "Vacuum floors", 3, false);
        tasc.setId(6);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("userId")).thenReturn("6");



    }

    @Test
    public void testGetTasc() {
        Tasc tasc = new Tasc("Clean house", "Wash toilets", 4, false);
        when(service.getTasc(0)).thenReturn(tasc);
        Tasc t = service.getTasc(0);
        Assert.assertEquals("Clean house", t.getTask());
        Assert.assertEquals("Wash toilets", t.getDescription());
        Assert.assertEquals(4, t.getPriority());
        Assert.assertFalse(t.getCompleted());
        verify(tascDaoMock, times(1)).getById(connectionSource, 0);
    }

    @Test
    public void testGetAll() {
        ArrayList<Tasc> list = new ArrayList<>();
        Tasc task1 = new Tasc("Clean house", "Wash toilets", 4, false);
        Tasc task2 = new Tasc("Clean house", "Vacuum floors", 3, false);
        list.add(task1);
        list.add(task2);
        when(tascDaoMock.getAll(connectionSource)).thenReturn(list);
        List<Tasc> tascList = service.getAllTascs();
        Assert.assertEquals(2, tascList.size());
        verify(tascDaoMock, times(1)).getAll(connectionSource);
    }

//    @Test
//    public void testUpdateTasc() {
//        Tasc tasc = new Tasc("Clean Car", "Take it to car wash", 7, false);
//        when(tascDaoMock.insert(connectionSource, tasc)).thenReturn(tasc);
//        Assert.assertEquals("Clean Car", tasc.getTask());
//        Assert.assertEquals("Take it to car wash", tasc.getDescription());
//        Assert.assertEquals(7, tasc.getPriority());
//        Assert.assertFalse(tasc.getCompleted());
//        Assert.assertEquals(0, tasc.getId());
//
//        tasc.setCompleted(true);
//        when(tascDaoMock.updateById(connectionSource, 0, tasc)).thenReturn(tasc);
//        Assert.assertEquals("Clean Car", tasc.getTask());
//        Assert.assertEquals("Take it to car wash", tasc.getDescription());
//        Assert.assertEquals(7, tasc.getPriority());
//        Assert.assertTrue(tasc.getCompleted());
//        Assert.assertEquals(0, tasc.getId());
//    }

    @Test
    public void testDeleteTasc() {
        service.deleteTasc(5);
        verify(tascDaoMock, times(1)).deleteById(connectionSource, 5);
    }
}