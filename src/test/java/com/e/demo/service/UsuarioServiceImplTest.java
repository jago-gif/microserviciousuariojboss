package com.e.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.e.demo.dto.UsuarioDTO;
import com.e.demo.entities.Usuario;
import com.e.demo.excepciones.ResourceNotFoundException;
import com.e.demo.repository.UsuarioRepository;
import com.e.demo.services.UsuarioServiceImp;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {
    
    @InjectMocks
    private UsuarioServiceImp usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

     @Test
    public void testGuardarUsuario() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("John");
        usuarioDTO.setApellido("Doe");
        usuarioDTO.setUsername("johndoe");
        usuarioDTO.setEmail("john@example.com");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setId(1L);
        usuarioGuardado.setNombre("John");
        usuarioGuardado.setApellido("Doe");
        usuarioGuardado.setUsername("johndoe");
        usuarioGuardado.setEmail("john@example.com");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        UsuarioDTO resultado = usuarioService.guardarUsuario(usuarioDTO);

        assertEquals("John", resultado.getNombre());
        assertEquals("Doe", resultado.getApellido());
        assertEquals("johndoe", resultado.getUsername());
        assertEquals("john@example.com", resultado.getEmail());
    }

     @Test
    public void testBuscarUsuarioPorUsernameExistente() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("John");
        usuario.setApellido("Doe");
        usuario.setUsername("johndoe");
        usuario.setEmail("john@example.com");

        when(usuarioRepository.findByUsername("johndoe")).thenReturn(Optional.of(usuario));

        UsuarioDTO resultado = usuarioService.buscarUsuarioPorUsername("johndoe");

        assertEquals("John", resultado.getNombre());
        assertEquals("Doe", resultado.getApellido());
        assertEquals("johndoe", resultado.getUsername());
        assertEquals("john@example.com", resultado.getEmail());
    }

  @Test
    public void testBuscarUsuarioPorUsernameNoExistente() {
        // Configuración del Mock
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Ejecución del método
        try {
           UsuarioDTO usuario = usuarioService.buscarUsuarioPorUsername("notfound");
           assertNull(usuario);
        } catch (ResourceNotFoundException ex) {
            // Verificación de la excepción
            assertNull(ex.getCause()); // Verifica que la causa de la excepción sea nula
        }

        // Verificación de interacciones con el mock
        verify(usuarioRepository, times(1)).findByUsername("notfound");
    }
    @Test
    public void testActualizarUsuarioExistente() {
        // Datos de entrada
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("NuevoNombre");
        usuarioDTO.setApellido("NuevoApellido");
        usuarioDTO.setUsername("nuevousername");
        usuarioDTO.setEmail("nuevo@example.com");

        // Mock del usuario existente
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNombre("NombreAnterior");
        usuarioExistente.setApellido("ApellidoAnterior");
        usuarioExistente.setUsername("username");
        usuarioExistente.setEmail("anterior@example.com");

        // Configuración del mock
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

        // Ejecución del método
        boolean resultado = usuarioService.actualizarUsuario(usuarioDTO);

        // Verificaciones
        assertTrue(resultado);
        assertEquals("NuevoNombre", usuarioExistente.getNombre());
        assertEquals("NuevoApellido", usuarioExistente.getApellido());
        assertEquals("nuevousername", usuarioExistente.getUsername());
        assertEquals("nuevo@example.com", usuarioExistente.getEmail());

        // Verificación de interacciones con el mock
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(usuarioExistente);
    }

    @Test
    public void testActualizarUsuarioNoExistente() {
        // Datos de entrada
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("NuevoNombre");
        usuarioDTO.setApellido("NuevoApellido");
        usuarioDTO.setUsername("nuevousername");
        usuarioDTO.setEmail("nuevo@example.com");

        // Configuración del mock
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecución del método y captura de excepción
        assertThrows(ResourceNotFoundException.class, () -> {
            usuarioService.actualizarUsuario(usuarioDTO);
        });

        // Verificación de interacciones con el mock
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

     @Test
    public void testEliminarUsuarioExistente() {
        Long idUsuarioExistente = 1L;

        // Configuración del mock
        doNothing().when(usuarioRepository).deleteById(idUsuarioExistente);

        // Ejecución del método
        boolean resultado = usuarioService.eliminarUsuario(idUsuarioExistente);

        // Verificación
        assertTrue(resultado);

        // Verificación de interacciones con el mock
        verify(usuarioRepository, times(1)).deleteById(idUsuarioExistente);
    }

    @Test
    public void testEliminarUsuarioNoExistente() {
        Long idUsuarioExistente = 999L;

        doThrow(RuntimeException.class).when(usuarioRepository).deleteById(idUsuarioExistente);

        // Ejecución del método
        boolean resultado = usuarioService.eliminarUsuario(idUsuarioExistente);

        // Verificación
        assertFalse(resultado);

        // Verificación de interacciones con el mock
        verify(usuarioRepository, times(1)).deleteById(idUsuarioExistente);
    }

     @Test
    public void testListarUsuarios() {
        // Datos de entrada
        Usuario usuario1 = new Usuario(1L, "Nombre1", "Apellido1", "username1", "email1@example.com");
        Usuario usuario2 = new Usuario(2L, "Nombre2", "Apellido2", "username2", "email2@example.com");
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario1);
        usuarios.add(usuario2);

        // Configuración del mock
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Ejecución del método
        List<UsuarioDTO> usuariosDTO = usuarioService.listarUsuarios();

        // Verificación
        assertEquals(2, usuariosDTO.size());

        UsuarioDTO usuarioDTO1 = usuariosDTO.get(0);
        assertEquals(usuario1.getId(), usuarioDTO1.getId());
        assertEquals(usuario1.getNombre(), usuarioDTO1.getNombre());
        assertEquals(usuario1.getApellido(), usuarioDTO1.getApellido());
        assertEquals(usuario1.getUsername(), usuarioDTO1.getUsername());
        assertEquals(usuario1.getEmail(), usuarioDTO1.getEmail());

        UsuarioDTO usuarioDTO2 = usuariosDTO.get(1);
        assertEquals(usuario2.getId(), usuarioDTO2.getId());
        assertEquals(usuario2.getNombre(), usuarioDTO2.getNombre());
        assertEquals(usuario2.getApellido(), usuarioDTO2.getApellido());
        assertEquals(usuario2.getUsername(), usuarioDTO2.getUsername());
        assertEquals(usuario2.getEmail(), usuarioDTO2.getEmail());

        // Verificación de interacciones con el mock
        verify(usuarioRepository, times(1)).findAll();
    }
}
