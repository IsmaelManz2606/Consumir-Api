package dao;

import dto.JuegoDTO;
import entidades.Juego;

import java.util.List;

public interface JuegoDAOInterface {

    List<Juego> devolverTodos();

    List<Juego> devolverTodos(int pagina, int tamaño);

    List<Juego> devolverMejorPuntuados();



    List<String> devolverTodasImages();

    List<JuegoDTO> devolverNombreYImagenes();

//    List<Juego> buscarPorCategorias(List<String> categorias);

    //para el endpoints nuevo de sacar la categoria de un juego
    List<Juego> buscarPorCategoria(Long categoriaId);

    Long totalJuegos();

    Juego buscarPorId(Long id);

    Double mediaPuntuacion();

    List<Juego> buscarPorNombreLike(String nombre);

    List<Juego> buscarEntreFechas(String fechaMin, String fechaMax);

    List<Juego> buscarPorPlataformas(List<String> plataformas);

    List<Juego> buscarPorDesarrollador(String desarrollador);

    List<Juego> buscarPorPuntuacionMinima(Double puntuacionMin);

    List<Juego> buscarPorPuntuacionMaxima(Double puntuacionMax);

    Juego create(Juego juego);

    Juego update(Juego juego);

    boolean deleteById(Long id);

    boolean deleteAll();

}
