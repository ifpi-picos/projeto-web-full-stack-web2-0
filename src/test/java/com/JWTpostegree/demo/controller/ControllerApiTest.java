package com.JWTpostegree.demo.controller;

//import com.JWTpostegree.demo.Consultas;
//import com.JWTpostegree.demo.model.TokenReqRes;
import com.JWTpostegree.demo.model.Users;
import com.JWTpostegree.demo.repository.ConsultasRepository;
import com.JWTpostegree.demo.repository.UserRepository;
import com.JWTpostegree.demo.utils.JwtTokenUtil;

//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerApi.class)
public class ControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ConsultasRepository consultasRepository;

    @Test
    public void testRegisterUser_Success() throws Exception {
        // Dados de exemplo
        Users sampleUser = new Users();
        sampleUser.setUsername("testUser");
        sampleUser.setSenha("testPassword");

        // Simulando comportamento do userRepository
        // Quando o userRepository.save() for chamado, nós retornamos o usuário com um ID atribuído
        when(userRepository.save(any(Users.class))).thenAnswer(i -> {
            Users user = i.getArgument(0);
            user.setId(1L);
            return user;
        });

        // Executando o teste
        // Esperamos que, ao fazer uma requisição POST com dados válidos de usuário, o status retornado seja 200 (OK)
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser\", \"senha\": \"testPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário criado com sucesso!"));
    }



    @Test
public void testRegisterUser_Failure() throws Exception {
    // Dados de exemplo
    Users sampleUser = new Users();
    sampleUser.setUsername("testUser");
    sampleUser.setSenha("testPassword");

    // Simulando comportamento do userRepository
    // Quando o userRepository.save() for chamado, nós retornamos um usuário sem um ID
    // Isso simula um cenário em que o salvamento no banco de dados falhou
    when(userRepository.save(any(Users.class))).thenAnswer(i -> {
        Users user = i.getArgument(0);
        user.setId(null); // ID nulo simula falha ao salvar
        return user;
    });

    // Executando o teste
    // Esperamos que, quando a operação de salvamento falhar, o status retornado seja 500 (Erro Interno do Servidor)
    mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"testUser\", \"senha\": \"testPassword\"}"))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("Usuário não salvo."));
}


}

