package dao;

import entidades.Categoria;
import entidades.Juego;
import entidades.Usuario;

import java.util.List;

public interface AsociacionesDAOInterface {
    boolean asignarCategoria(Juego j, Categoria c);
    Categoria obtenerCategoriaJuego(Juego j);


    List<Juego> obtenerjuegosCategoria(Categoria c);

    //MANYTOMANY JUEGOS Y USUARIOS
    boolean asignarUsuario(Juego j, Usuario u);

    List<Usuario> usuariosConJuego(Juego j);

    List<Juego> juegosAccedidos(Usuario usuario);

    boolean accederJuego(Juego juego, Usuario usuario);
}
