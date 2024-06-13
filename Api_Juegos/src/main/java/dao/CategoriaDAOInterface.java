package dao;

import entidades.Categoria;
import entidades.Juego;
import entidades.Usuario;

import java.util.List;

public interface CategoriaDAOInterface {
    Categoria crearCategoria(Categoria c);

    List<Categoria> devolverTodas();


    Categoria buscarPorId(Long id);


    Categoria buscarCategoriaPorNombre(String nombreCategoria);


    Categoria update(Categoria categoria);

    boolean deleteById(Long id);
}
