package com.flashpoint.ControlePonto.service.Login;

import java.util.List;

import com.flashpoint.ControlePonto.enumerators.TipoUsuario;
import com.flashpoint.ControlePonto.error.Login.AcessoNegadoException;
import com.flashpoint.ControlePonto.error.Login.UsuarioNaoExisteException;
import com.flashpoint.ControlePonto.model.entities.Usuario;


public interface VerificadorPermissoesService {
	public Usuario verificarPermissao(String loginUsuario, List<TipoUsuario> permitidos) throws UsuarioNaoExisteException, AcessoNegadoException;
	public boolean existeUsuarioByLogin(String login);

}
