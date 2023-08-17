package com.e.demo.services;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e.demo.dto.UsuarioDTO;
import com.e.demo.entities.Usuario;
import com.e.demo.excepciones.ResourceNotFoundException;
import com.e.demo.repository.UsuarioRepository;


@Service
public class UsuarioServiceImp implements UsuarioService{

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Override
	public UsuarioDTO guardarUsuario(UsuarioDTO usuarioDTO) {
		Usuario user = new Usuario();
		user.setApellido(usuarioDTO.getApellido());
		user.setNombre(usuarioDTO.getNombre());
		user.setUsername(usuarioDTO.getUsername());
		user.setEmail(usuarioDTO.getEmail());

		Usuario usuarioGuardado = usuarioRepository.save(user);
		UsuarioDTO usuarioDTORespuesta = new UsuarioDTO();
		usuarioDTORespuesta.setApellido(usuarioGuardado.getApellido());
		usuarioDTORespuesta.setId(usuarioGuardado.getId());
		usuarioDTORespuesta.setNombre(usuarioGuardado.getNombre());
		usuarioDTORespuesta.setUsername(usuarioGuardado.getUsername());
		usuarioDTORespuesta.setEmail(usuarioGuardado.getEmail());
		return usuarioDTORespuesta;
	}
	@Override
	public UsuarioDTO buscarUsuarioPorUsername(String username) {
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		if(usuario.isPresent()) {
			UsuarioDTO usuarioDTO = new UsuarioDTO();
			usuarioDTO.setApellido(usuario.get().getApellido());
			usuarioDTO.setId(usuario.get().getId());
			usuarioDTO.setNombre(usuario.get().getNombre());
			usuarioDTO.setUsername(usuario.get().getUsername());
			usuarioDTO.setEmail(usuario.get().getEmail());
			return usuarioDTO;
		}else{
			throw new ResourceNotFoundException("usuario no existe", username, null);
		}
	}
	
	@Override
    public boolean actualizarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioDTO.getId()).orElse(null);
        if (usuario != null) {
            usuario.setApellido(usuarioDTO.getApellido());
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setUsername(usuarioDTO.getUsername());
            usuario.setEmail(usuarioDTO.getEmail());
            usuarioRepository.save(usuario);
            return true;
        } else {
            throw new ResourceNotFoundException("Usuario no encontrado", String.valueOf(usuarioDTO.getId()), null);
        }
    }

	@Override
	public boolean eliminarUsuario(Long id) {
		boolean retorno = true;
		try {
			usuarioRepository.deleteById(id);
		} catch (Exception e) {
			retorno = false;
		}
		return retorno;
	}
	@Override
	public List<UsuarioDTO> listarUsuarios() {
		List<UsuarioDTO> retorno = new ArrayList<UsuarioDTO>();
		for(Usuario usuario : usuarioRepository.findAll()) {
			if(usuario.getId()>0){
				retorno.add(new UsuarioDTO(
						usuario.getId(),
						usuario.getNombre(),
						usuario.getApellido(),
						usuario.getUsername(),
						usuario.getEmail()
						));
			}
		}
		return retorno;
	}

}
