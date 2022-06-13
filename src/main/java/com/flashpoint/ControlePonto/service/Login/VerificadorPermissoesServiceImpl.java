package com.flashpoint.ControlePonto.service.Login;
import java.util.List;

import com.flashpoint.ControlePonto.enumerators.TipoUsuario;
import com.flashpoint.ControlePonto.error.Login.AcessoNegadoException;
import com.flashpoint.ControlePonto.error.Login.UsuarioNaoExisteException;
import com.flashpoint.ControlePonto.model.entities.Usuario;
import com.flashpoint.ControlePonto.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificadorPermissoesServiceImpl implements VerificadorPermissoesService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private UsuarioServices usuarioServices;

	@Override
	public Usuario verificarPermissao(String loginUsuario, List<TipoUsuario> permitidos) throws UsuarioNaoExisteException, AcessoNegadoException {
		
		
		Usuario usuario = usuarioServices.getUsuarioByLogin(loginUsuario);
		
		if (!permitidos.contains(usuario.getCargo())) throw new AcessoNegadoException();
		
		return usuario;
	}
	
	@Override
	public boolean existeUsuarioByLogin(String login) {
		return usuarioRepository.existsByLoginIgnoreCase(login);
	}
}
