package servicios;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.*;
import entidades.Categoria;
import entidades.Juego;
import entidades.Usuario;
import spark.Spark;
import util.LocalDateAdapter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class JuegosAPIREST {

    private JuegoDAOInterface dao_juego;
    private CategoriaDAOInterface dao_categoria;

    private UsuarioDAOInterface dao_usuario;

    private AsociacionesDAOInterface dao_asociaciones;

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public JuegosAPIREST(JuegoDAOInterface implementacion_juego,
                         CategoriaDAOInterface implementacion_categoria,
                         UsuarioDAOInterface implementacion_usuario,
                         AsociacionesDAOInterface implementacion_asociaciones) {
        Spark.port(8080);
        dao_juego = implementacion_juego;
        dao_categoria = implementacion_categoria;
        dao_usuario = implementacion_usuario;
        dao_asociaciones = implementacion_asociaciones;

        Spark.exception(Exception.class, (e, req, res) -> {
            e.printStackTrace(); // Imprime la excepción en la consola
            res.status(500); // Establece el código de estado HTTP 500
            res.body("Excepcion en tu codigo"); // Mensaje de error para el cliente
        });

        Spark.before((request, response) -> {
                    response.header("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
                    response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
                    response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
                    response.header("Access-Control-Allow-Credentials", "true");
            });


        //RELACIONES
        //manytoone de juego a categoria
        //Asignar una categoria a un juego
        Spark.post("/juegos/categoria/:idcat/:idju", (request, response) -> {
            Long idcat = Long.parseLong(request.params(":idcat"));
            Long idju = Long.parseLong(request.params(":idju"));
            Juego j= dao_juego.buscarPorId(idju);
            Categoria c= dao_categoria.buscarPorId(idcat);
            response.type("application/json");

            return gson.toJson(dao_asociaciones.asignarCategoria(j,c));
        });
        // Endpoint para obtener la categoría de un juego por su ID
        Spark.get("/juegos/categoria/:id", (request, response) -> {
            Long id = Long.parseLong(request.params(":id"));
            Juego j= dao_juego.buscarPorId(id);
            Categoria c = dao_asociaciones.obtenerCategoriaJuego(j);
            response.type("application/json");
            if (c!=null) {
                return gson.toJson(c);
            }
            else{
                return "No existe este juego";
            }
        });
        // Endpoint para obtener todos los juegos de una categoría por su ID
        Spark.get("/categorias/juegos/:idcat", (request, response) -> {
            Long id = Long.parseLong(request.params(":idcat"));
            Categoria c= dao_categoria.buscarPorId(id);
            List<Juego> a = dao_asociaciones.obtenerjuegosCategoria(c);
            response.type("application/json");
            if (a!=null) {
                return gson.toJson(a);
            }
            else{
                return "No existe esta categoria";
            }
        });

        //MANYTOMANY DE JUEGOS A USUARIOS
        // Asignar usuario a un juego
        Spark.post("/usuarios/juegos/:idusuario/:idjuego", (request, response) -> {
            Long idjuego = Long.parseLong(request.params(":idjuego"));
            Long idusuario = Long.parseLong(request.params(":idusuario"));
            Juego j= dao_juego.buscarPorId(idjuego);
            Usuario u= dao_usuario.buscarPorID(idusuario);
            response.type("application/json");
            return gson.toJson(dao_asociaciones.asignarUsuario(j,u));
        });

        // Endpoint para obtener todos los usuarios que tienen acceso a un juego por su ID
        Spark.get("/juegos/usuarios/:id", (request, response) -> {
            response.type("application/json");
            Long juegoId = Long.parseLong(request.params(":id"));
            Juego juego = new Juego();
            juego.setId(juegoId);
            // Obtener la lista de usuarios que tienen acceso al juego
            List<Usuario> usuarios = dao_asociaciones.usuariosConJuego(juego);
            // Verificar si se encontraron usuarios
            if (usuarios.isEmpty()) {
                response.status(404);
                return "No se encontraron usuarios que tengan acceso al juego con ID: " + juegoId;
            }
            // Devolver la lista de usuarios como JSON
            return gson.toJson(usuarios);
        });

        // Endpoint para obtener todos los juegos a los que tiene acceso un usuario por su ID
        Spark.get("/usuarios/juegos/:id", (request, response) -> {
            response.type("application/json");
            Long usuarioId = Long.parseLong(request.params(":id"));

            // Crear un objeto Usuario con el ID obtenido
            Usuario usuario = new Usuario();
            usuario.setId(usuarioId);
            // Obtener la lista de juegos a los que tiene acceso el usuario
            List<Juego> juegos = dao_asociaciones.juegosAccedidos(usuario);

            // Verificar si se encontraron juegos
            if (juegos.isEmpty()) {
                response.status(404);
                return "No se encontraron juegos a los que el usuario con ID: " + usuarioId + " tenga acceso";
            }

            // Devolver la lista de juegos como JSON
            return gson.toJson(juegos);
        });






        //JUEGOS
        //CREAR UN Juego CON TODOS LOS DATOS, PORQUE SE HACE CON EL POST EN POSTMAN
        Spark.post("/juegos/crear-nuevo", (request, response) -> {
            String body = request.body();
            Juego nuevoJuego = gson.fromJson(body, Juego.class);

            Juego creado = dao_juego.create(nuevoJuego);
            return gson.toJson(creado);
        });

        // Endpoint para obtener todos los juegos
        Spark.get("/juegos", (request, response) -> {
            response.type("application/json");

            List<Juego> juegos = dao_juego.devolverTodos();
            return gson.toJson(juegos);
        });

        //Buscar un juego por su ID
        Spark.get("/juegos/search/:id", (request, response) -> {
            response.type("application/json");

            Long id = Long.parseLong(request.params(":id"));
            Juego buscado = dao_juego.buscarPorId(id);
            if (buscado != null) {
                return gson.toJson(buscado);
            } else {
                response.status(404);
                return "Juego no encontrado";
            }
        });

        //Actualizar un juego por su ID, PORQUE SE HACE CON EL PUT EN POSTMAN
        Spark.post("/juegos/actualizar/:id", (request, response) -> {
            response.type("application/json");

            Long id = Long.parseLong(request.params(":id"));
            String body = request.body();
            Juego juegoActualizado = gson.fromJson(body, Juego.class);
            juegoActualizado.setId(id);
            Juego actualizado = dao_juego.update(juegoActualizado);
            if (actualizado != null) {
                return gson.toJson(actualizado);
            } else {
                response.status(404);
                return "Juego no encontrado";
            }
        });

        // Endpoint para eliminar un juego por su ID
        Spark.post("/juegos/delete/:id", (request, response) -> {
            response.type("application/json");

            Long id = Long.parseLong(request.params(":id"));
            boolean eliminado = dao_juego.deleteById(id);
            if (eliminado) {
                return "Juego eliminado correctamente";
            } else {
                response.status(404);
                return "Juego no encontrado";
            }
        });







        //CATEGORIA
        // Endpoint para crear una nueva categoría
        Spark.post("/categorias/crear", (request, response) -> {
            response.type("application/json");

            // Obtener los datos de la nueva categoría del cuerpo de la solicitud
            Categoria nuevaCategoria = gson.fromJson(request.body(), Categoria.class);

            // Llamar al método crearCategoria para guardar la nueva categoría en la base de datos
            Categoria categoriaCreada = dao_categoria.crearCategoria(nuevaCategoria);

            // Devolver la categoría creada como JSON
            return gson.toJson(categoriaCreada);
        });


        // Endpoint para obtener todas las categorias
        Spark.get("/categorias", (request, response) -> {
            List<Categoria> categorias = dao_categoria.devolverTodas();
            System.out.println(categorias);
            response.type("application/json");
            return gson.toJson(categorias);
        });

        //Actualizar una categoria por su ID
        Spark.post("/categorias/actualizar/:id", (request, response) -> {
            Long id = Long.parseLong(request.params(":id"));
            String body = request.body();
            Categoria categoriaActualizada = gson.fromJson(body, Categoria.class);
            categoriaActualizada.setId(id);
            Categoria actualizado = dao_categoria.update(categoriaActualizada);
            if (actualizado != null) {
                return gson.toJson(actualizado);
            } else {
                response.status(404);
                return "Categoria no encontrada";
            }
        });

        // Endpoint para eliminar una categoria por su ID
        Spark.post("/categorias/delete/:id", (request, response) -> {
            response.type("application/json");

            Long id = Long.parseLong(request.params(":id"));
            boolean eliminado = dao_categoria.deleteById(id);
            if (eliminado) {
                return "Categoria eliminada correctamente";
            } else {
                response.status(404);
                return "Categoria no encontrada";
            }
        });


        //USUARIOS
        // Endpoint para crear un nuevo usuario
        Spark.post("/usuarios/crear", (request, response) -> {
            response.type("application/json");

            // Obtener los datos de la nueva categoría del cuerpo de la solicitud
            Usuario nuevoUsuario = gson.fromJson(request.body(), Usuario.class);

            // Llamar al método crearCategoria para guardar la nueva categoría en la base de datos
            Usuario usuarioCreado = dao_usuario.crearUsuario(nuevoUsuario);

            // Devolver la categoría creada como JSON
            return gson.toJson(usuarioCreado);
        });


        // Endpoint para obtener todos los usuarios
        Spark.get("/usuarios", (request, response) -> {
            List<Usuario> usuarios = dao_usuario.devolverTodos();
            System.out.println(usuarios);
            response.type("application/json");
            return gson.toJson(usuarios);
        });

        //Actualizar un Usuario por su ID
        Spark.post("/usuarios/actualizar/:id", (request, response) -> {
            Long id = Long.parseLong(request.params(":id"));
            String body = request.body();
            Usuario usuarioActualizado = gson.fromJson(body, Usuario.class);
            usuarioActualizado.setId(id);
            Usuario actualizado = dao_usuario.update(usuarioActualizado);
            if (actualizado != null) {
                return gson.toJson(actualizado);
            } else {
                response.status(404);
                return "Usuario no encontrado";
            }
        });

        // Endpoint para eliminar un Usuario por su ID
        Spark.post("/usuarios/delete/:id", (request, response) -> {
            response.type("application/json");

            Long id = Long.parseLong(request.params(":id"));
            boolean eliminado = dao_usuario.deleteById(id);
            if (eliminado) {
                return "Usuario eliminado correctamente";
            } else {
                response.status(404);
                return "Usuario no encontrado";
            }
        });















        //En caso de intentar un endpoint incorrecto
        Spark.notFound((request, response) -> {
            response.type("application/json");
            return "{\"error\": \"Ruta no encontrada\",\"hint1\": \"/juegos\"," +
                    "\"hint2\": \"/juegos/paginado/:pagina/:tam_pagina\",\"hint3\": \"/juegos/id/:id\"}";
        });

        //DEVOLVER DATOS DE MANERA PAGINADA DE TODOS LOS JUEGOS ENDPOINT PAGINADO
        Spark.get("/juegos/paginado/:pagina/:tam_pagina", (request, response) -> {

            Integer pagina = Integer.parseInt(request.params("pagina"));
            Integer tamaño_pagina = Integer.parseInt(request.params("tam_pagina"));

            List<Juego> juegos = dao_juego.devolverTodos(pagina, tamaño_pagina);

            Long totalElementos = dao_juego.totalJuegos(); // Obtener el total de juegos

            RespuestaPaginacion<Juego> paginaResultado = new RespuestaPaginacion<>(juegos, totalElementos, pagina, tamaño_pagina);

            return gson.toJson(paginaResultado);

        });




        // Endpoint para obtener JUEGOS Mejor Puntuados
//        Spark.get("/juegos/mejorpuntuados", (request, response) -> {
//            response.type("application/json");
//
//            List<Juego> mejorPuntuados = dao_juego.devolverMejorPuntuados();
//            System.out.println(mejorPuntuados);
//            return gson.toJson(mejorPuntuados);
//        });
//
//        // Endpoint para buscar JUEGOS por nombre
//        Spark.get("/juegos/buscarnombre/:nombre", (request, response) -> {
//            String nombre = request.params(":nombre");
//
//            if (nombre != null && !nombre.isEmpty()) {
//                List<Juego> juegosEncontrados = dao_juego.buscarPorNombreLike(nombre);
//                response.type("application/json");
//                return gson.toJson(juegosEncontrados);
//            } else {
//                response.status(400);  // Bad Request
//                response.type("application/json");
//                return gson.toJson("Por favor, proporciona un nombre de juego válido en el parámetro 'nombre'.");
//            }
//        });

//        // Endpoint para obtener todas las imágenes de juegos
//        Spark.get("/juegos/imagenes", (request, response) -> {
//            response.type("application/json");
//
//            List<String> imagenes = dao_juego.devolverTodasImages();
//            return gson.toJson(imagenes);
//        });

        // Endpoint para buscar juegos por categorías
//        Spark.get("/juegos/categorias/:categorias", (request, response) -> {
//            String categoriasParam = request.params(":categorias");
//            List<String> categorias = Arrays.asList(categoriasParam.split(","));
//
//            List<Juego> juegosPorCategorias = dao_juego.buscarPorCategorias(categorias);
//            response.type("application/json");
//            return gson.toJson(juegosPorCategorias);
//        });

//        // Endpoint para sacar la media de la puntuacion de todos los juegos
//        Spark.get("/juegos/mediapuntuacion", (request, response) -> {
//            response.type("application/json");
//
//            Double media = dao_juego.mediaPuntuacion();
//            return media.toString();
//        });
//
//        // Endpoint para obtener el nombre y la URL
//        Spark.get("/juegos/nombreyimg", (request, response) -> {
//            response.type("application/json");
//
//            //List<Map> resumen = dao.devolverNombreImagenes();
//            List<JuegoDTO> resumen = dao_juego.devolverNombreYImagenes();
//            return gson.toJson(resumen);
//        });
//
//        // Endpoint para obtener el total de juegos
//        Spark.get("/juegos/total", (request, response) -> {
//            response.type("application/json");
//
//            Long total = dao_juego.totalJuegos();
//            return total.toString();
//        });
//
//        // Endpoint para buscar JUEGOS por fecha y resultado ordenado ascendentemente
//        Spark.get("/juegos/buscarfecha/:fechaMin/:fechaMax", (request, response) -> {
//            String fechaMin = request.params(":fechaMin");
//            String fechaMax = request.params(":fechaMax");
//
//            if (!fechaMin.isEmpty() && !fechaMax.isEmpty()) {
//                List<Juego> juegosEncontrados = dao_juego.buscarEntreFechas(fechaMin, fechaMax);
//                response.type("application/json");
//                return gson.toJson(juegosEncontrados);
//            } else {
//                response.status(400);  // Bad Request
//                response.type("application/json");
//                return gson.toJson("Por favor, proporciona fechas de búsqueda válidas.");
//            }
//        });
//
//        // Endpoint para buscar juegos por plataformas
//        Spark.get("/juegos/buscarplataformas/:plataformas", (request, response) -> {
//            String plataformasParam = request.params(":plataformas");
//            List<String> plataformas = Arrays.asList(plataformasParam.split(","));
//
//            List<Juego> juegos = dao_juego.buscarPorPlataformas(plataformas);
//            response.type("application/json");
//            return gson.toJson(juegos);
//        });
//
//        // Endpoint para buscar juegos por desarrollador
//        Spark.get("/juegos/buscardesarrollador/:desarrollador", (request, response) -> {
//            String desarrollador = request.params(":desarrollador");
//            List<Juego> juegos = dao_juego.buscarPorDesarrollador(desarrollador);
//            response.type("application/json");
//            return gson.toJson(juegos);
//        });
//
//        // Endpoint para buscar juegos por puntuación mínima
//        Spark.get("/juegos/buscarpuntuacionmin/:puntuacionMin", (request, response) -> {
//            Double puntuacionMin = Double.parseDouble(request.params(":puntuacionMin"));
//            List<Juego> juegos = dao_juego.buscarPorPuntuacionMinima(puntuacionMin);
//            response.type("application/json");
//            return gson.toJson(juegos);
//        });
//
//        // Endpoint para buscar juegos por puntuación máxima
//        Spark.get("/juegos/buscarpuntuacionmax/:puntuacionMax", (request, response) -> {
//            Double puntuacionMax = Double.parseDouble(request.params(":puntuacionMax"));
//            List<Juego> juegos = dao_juego.buscarPorPuntuacionMaxima(puntuacionMax);
//            response.type("application/json");
//            return gson.toJson(juegos);
//        });


    }
}