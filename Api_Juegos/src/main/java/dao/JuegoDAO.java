package dao;

import dto.JuegoDTO;
import entidades.Juego;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.PersistenceException;
import java.util.List;

public class JuegoDAO implements JuegoDAOInterface{
    public List<Juego> devolverTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Consulta HQL
        List<Juego> todos = session.createQuery("from Juego", Juego.class).list();
        session.close();

        return todos;
    }
    @Override
    public List<Juego> devolverTodos(int pagina,int objetos_por_pagina) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Consulta HQL con limits
        Query query=session.createQuery("from Juego", Juego.class);
        query.setMaxResults(objetos_por_pagina);
        query.setFirstResult((pagina-1)*objetos_por_pagina);
        List<Juego> todos = query.list();

        session.close();

        return todos;
    }


    @Override
    public List<Juego> devolverMejorPuntuados() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Consulta HQL para devolver los juegos con mejor puntuación
        List<Juego> mejorPuntuados = session.createQuery("from Juego j order by j.puntuacion desc", Juego.class)
                .setMaxResults(5)//Para los juegos que quiera obtener
                .list();
        session.close();

        return mejorPuntuados;
    }


    @Override
    public List<Juego> buscarPorNombreLike(String busqueda) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Consulta HQL modificada para buscar juegos por nombre
        Query<Juego> query = session.createQuery("from Juego j where j.nombre like :busqueda", Juego.class);
        List<Juego> resultados = query.setParameter("busqueda", "%" + busqueda +"%").list();
        session.close();

        return resultados;
    }

//    public List<Juego> buscarPorCategorias(List<String> categorias) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//
//        try {
//            Query<Juego> query = session.createQuery("from Juego j where j.categoria in (:categorias)", Juego.class);
//            query.setParameterList("categorias", categorias);
//
//            return query.list();
//        } finally {
//            session.close();
//        }
//    }
    @Override
    public List<Juego> buscarPorCategoria(Long categoriaId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Juego> query = session.createQuery("FROM Juego j WHERE j.cat.id = :categoriaId", Juego.class);
            query.setParameter("categoriaId", categoriaId);
            List<Juego> resultados = query.getResultList();
            return resultados; // Devuelve la lista de resultados, no la consulta misma
        } finally {
            session.close();
        }
    }


    @Override
    public List<String> devolverTodasImages() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Consulta HQL
        List<String> imagenes = session.createQuery("select j.imagenURL from Juego j", String.class).list();
        session.close();

        return imagenes;
    }

    @Override
    //public List<Map> devolverNombreImagenes() {
    public List<JuegoDTO> devolverNombreYImagenes() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Consulta HQL
        List<JuegoDTO> juegos = session.createQuery("select new dto.JuegoDTO(j.nombre, j.imagenURL) from Juego j", JuegoDTO.class).list();

        session.close();

        return juegos;
    }


    @Override
    public Double mediaPuntuacion() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Double media = session.createQuery("select avg(j.puntuacion) from Juego j", Double.class).getSingleResult();

        session.close();

        return media;
    }

    @Override
    public Long totalJuegos() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Long contador = (Long) session.createQuery("select count(j) from Juego j").getSingleResult();

        session.close();

        return contador;
    }


    @Override
    public Juego buscarPorId(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Juego employee = session.find(Juego.class, id);
        session.close();

        return employee;
    }



    @Override
    public List<Juego> buscarEntreFechas(String fechaMin, String fechaMax) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Consulta HQL para buscar juegos por fecha
            Query<Juego> query = session.createQuery(
                    "from Juego j where j.fechaLanzamiento between :fechaMin and :fechaMax order by j.fechaLanzamiento asc", Juego.class);
            query.setParameter("fechaMin", fechaMin);
            query.setParameter("fechaMax", fechaMax);

            return query.list();
        }
    }


    @Override
    public List<Juego> buscarPorPlataformas(List<String> plataformas) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Query<Juego> query = session.createQuery("from Juego j where j.plataforma in :plataformas", Juego.class);
            query.setParameterList("plataformas", plataformas);
            return query.list();
        } finally {
            session.close();
        }
    }


    @Override
    public List<Juego> buscarPorDesarrollador(String desarrollador) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Query<Juego> query = session.createQuery("from Juego j where j.desarrollador = :desarrollador", Juego.class);
            query.setParameter("desarrollador", desarrollador);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Juego> buscarPorPuntuacionMinima(Double puntuacionMin) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Query<Juego> query = session.createQuery("from Juego j where j.puntuacion >= :puntuacionMin", Juego.class);
            query.setParameter("puntuacionMin", puntuacionMin);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Juego> buscarPorPuntuacionMaxima(Double puntuacionMax) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Juego> query = session.createQuery("from Juego j where j.puntuacion <= :puntuacionMax", Juego.class);
            query.setParameter("puntuacionMax", puntuacionMax);
            return query.list();
        }
    }



    @Override
    public Juego create(Juego juego) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            session.save(juego);
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return juego;
    }

    @Override
    public Juego update(Juego juego) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            session.update(juego);
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return juego;
    }

    @Override
    public boolean deleteById(Long id)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try { session.beginTransaction(); Juego juego = this.buscarPorId(id);
            if(juego!=null){ session.delete(juego); }
            else{ return false; }
            session.getTransaction().commit(); }
        catch (PersistenceException e) { e.printStackTrace(); session.getTransaction().rollback();
            return false; }
        finally { session.close(); }
        return true;
    }

    @Override
    public boolean deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            String deleteQuery = "DELETE FROM Juego";
            Query query = session.createQuery(deleteQuery);
            query.executeUpdate();

            session.getTransaction().commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        } finally {
            session.close();
        }

        return true;
    }
}