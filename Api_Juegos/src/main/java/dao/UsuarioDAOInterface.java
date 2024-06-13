package dao;

import entidades.Usuario;

import java.util.List;

public interface UsuarioDAOInterface {
    Usuario crearUsuario(Usuario u);

    List<Usuario> devolverTodos();

    Usuario buscarPorID(Long id);


    Usuario update(Usuario usuario);

    boolean deleteById(Long id);
}
