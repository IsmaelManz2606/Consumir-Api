package dao;

import entidades.Categoria;
import entidades.Juego;
import entidades.Usuario;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class AsociacionesDAO implements AsociacionesDAOInterface{

    @Override
    public boolean asignarCategoria(Juego j, Categoria c) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            j.setCat(c);
            session.update(j);
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        }
        session.close();
        return true;
    }

    @Override
    public Categoria obtenerCategoriaJuego(Juego j) {
        Categoria categoria;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Query<Juego> query = session.createQuery("select j from Juego j join fetch j.cat where j.id =:id", Juego.class);
            query.setParameter("id", j.getId());
            categoria = query.getSingleResult().getCat();

        } catch (NoResultException nre) {
            categoria=null;
        }
        session.close();
        session.close();
        return categoria;
    }


    @Override
    public List<Juego> obtenerjuegosCategoria(Categoria c) {
        List<Juego> lista_juegos;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Query<Categoria> query = session.createQuery("select c from Categoria c join fetch c.variable where c.id =:id", Categoria.class);
            query.setParameter("id", c.getId());
            lista_juegos = query.getSingleResult().getVariable();
        } catch (NoResultException nre) {
            lista_juegos = new ArrayList<>();
        }

        session.close();

        return lista_juegos;
    }


    //MANYTOMANY JUEGOS Y USUARIOS
    @Override
    public boolean asignarUsuario(Juego j, Usuario u) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<Juego> lista_juegos = juegosAccedidos(u);
            lista_juegos.add(j);
            u.setJuegos(lista_juegos);

            List<Usuario> lista_usuarios = usuariosConJuego(j);
            lista_usuarios.add(u);
            j.setUsuarios(lista_usuarios);

            session.beginTransaction();

            session.update(j);
            session.update(u);
            session.getTransaction().commit();

        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        }
        session.close();
        return true;
    }

    @Override
    public List<Usuario> usuariosConJuego(Juego j) {
        List<Usuario> lista_usuarios = null;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Query<Juego> query = session.createQuery("select j from Juego j join fetch j.usuarios where j.id =:id", Juego.class);
            query.setParameter("id", j.getId());
            lista_usuarios = query.getSingleResult().getUsuarios();
        } catch (NoResultException nre) {
            lista_usuarios = new ArrayList<>();
        }
        session.close();

        return lista_usuarios;
    }

    @Override
    public List<Juego> juegosAccedidos(Usuario usuario) {
        List<Juego> listaJuegos = null;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Query<Usuario> query = session.createQuery("select u from Usuario u join fetch u.juegos where u.id = :id", Usuario.class);
            query.setParameter("id", usuario.getId());
            listaJuegos = query.getSingleResult().getJuegos();
        } catch (NoResultException nre) {
            listaJuegos = new ArrayList<>();
        }

        session.close();
        return listaJuegos;
    }

    @Override
    public boolean accederJuego(Juego juego, Usuario usuario) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<Juego> listaJuegos = juegosAccedidos(usuario);
            listaJuegos.add(juego);
            usuario.setJuegos(listaJuegos);

            List<Usuario> listaUsuarios = usuariosConJuego(juego);
            listaUsuarios.add(usuario);
            juego.setUsuarios(listaUsuarios);

            session.beginTransaction();

            session.update(juego);
            session.update(usuario);
            session.getTransaction().commit();

        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        }

        session.close();
        return true;
    }


}
