package com.cat.app.catapp;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.cat.app.catapp.controllers.CatController;
import com.cat.app.catapp.entities.CatEntity;
import com.cat.app.catapp.services.CatService;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(CatController.class)
public class CatControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CatService catService;

    CatEntity cat1 = new CatEntity(1l, "Guido", 3, 4);
    CatEntity cat2 = new CatEntity(2l, "Guidosi", 8, 6);

    @Test
    public void getAll() throws Exception {
        List<CatEntity> cats = new ArrayList<>(Arrays.asList(cat1, cat2));
        Mockito.when(catService.getAll()).thenReturn(cats);

        mockMvc.perform(MockMvcRequestBuilders
            .get("/cats")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[1].name", is("Guidosi"))
        );
    }

    @Test
    public void getCatById() throws Exception {
        Mockito.when(catService.getCatById(cat1.getId())).thenReturn(cat1);

        mockMvc.perform(MockMvcRequestBuilders
            .get("/cats/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.name", is("Guido"))
        );
    }

    @Test
    public void createCat() throws Exception {
        CatEntity cat = new CatEntity(1l, "Guido", 3, 4);
        Mockito.when(catService.createCat(cat)).thenReturn(cat);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/cats")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(cat));

        mockMvc.perform(mockRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.name", is("Guido")));
    }
}
