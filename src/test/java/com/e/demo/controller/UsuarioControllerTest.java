package com.e.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.*;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.e.demo.dto.UsuarioDTO;
import com.e.demo.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private List<UsuarioDTO> usuarios;

    @BeforeEach
    void setup() {
        usuarios = new ArrayList<>();
        usuarios.add(new UsuarioDTO(1L, "Javier", "Garrido", "jg", "jg@example.com"));
        usuarios.add(new UsuarioDTO(2L, "Maria", "Lopez", "ml", "ml@example.com"));
    }

    @Test
    void testListarUsuarios() throws Exception {
        when(usuarioService.listarUsuarios()).thenReturn(usuarios);

        mockMvc.perform(get("/usuario/listar"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(usuarios)));
    }

    @Test
    void testGuardarUsuario() throws Exception {
        UsuarioDTO nuevoUsuario = new UsuarioDTO(null, "Nuevo", "Usuario", "nu", "nu@example.com");
        UsuarioDTO usuarioGuardado = new UsuarioDTO(3L, "Nuevo", "Usuario", "nu", "nu@example.com");

        when(usuarioService.guardarUsuario(any(UsuarioDTO.class))).thenReturn(usuarioGuardado);

        mockMvc.perform(post("/usuario/guardar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(usuarioGuardado)));
    }

    @Test
    void testEliminarUsuario() throws Exception {
        Long idUsuario = 1L;

        // Configurar el comportamiento simulado del servicio mock
        when(usuarioService.eliminarUsuario(idUsuario)).thenReturn(true);
    
        // Ejecutar la solicitud DELETE en el controlador
        mockMvc.perform(delete("/usuario/eliminar/{id}", idUsuario))
                .andExpect(status().isOk()); // Esperamos un estado HTTP 200
    
        // Verificar que el método eliminarUsuario del servicio fue llamado con el idUsuario esperado
        verify(usuarioService, times(1)).eliminarUsuario(idUsuario);
    }

    @Test
    void testUsuarioByUsername() throws Exception {
        String username = "jg";
        UsuarioDTO usuario = new UsuarioDTO(1L, "Javier", "Garrido", "jg", "jg@example.com");

        when(usuarioService.buscarUsuarioPorUsername(username)).thenReturn(usuario);

        mockMvc.perform(get("/usuario/{username}", username))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(usuario)));
    }

    @Test
    void testActualizarUsuario() throws Exception {
        UsuarioDTO usuarioActualizado = new UsuarioDTO(1L, "Nuevo", "Usuario", "nu", "nu@example.com");

        when(usuarioService.actualizarUsuario(any(UsuarioDTO.class))).thenReturn(true);

        mockMvc.perform(put("/usuario/actualiza")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isOk());
    }
}
