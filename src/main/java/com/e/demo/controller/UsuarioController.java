package com.e.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e.demo.dto.UsuarioDTO;
import com.e.demo.services.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    
    @Autowired
    UsuarioService usuarioService;
    
    @GetMapping("/hola")
    public String hola() {
    	return "hola";
    }

    @PostMapping("/guardar")
    public ResponseEntity<UsuarioDTO> guardarUsuario(@RequestBody UsuarioDTO usuario) {
        return new ResponseEntity<>(usuarioService.guardarUsuario(usuario), HttpStatus.CREATED );
    }

    @GetMapping("/listar")
    public ResponseEntity<Iterable<UsuarioDTO>> listarUsuarios() {
        return new ResponseEntity<>(usuarioService.listarUsuarios(), HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Boolean> eliminarUsuario(@PathVariable Long id) {
        return new ResponseEntity<>(usuarioService.eliminarUsuario(id), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UsuarioDTO> usuarioByUsername(@PathVariable String username) {
        return new ResponseEntity<>(usuarioService.buscarUsuarioPorUsername(username), HttpStatus.OK);
    }

    @PutMapping("/actualiza")
    public ResponseEntity<Boolean> actualizarUsuario(@RequestBody UsuarioDTO usuario) {
        boolean actualizado = usuarioService.actualizarUsuario(usuario);
        if(actualizado) {
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(actualizado, HttpStatus.NOT_FOUND);
    
    }
}
