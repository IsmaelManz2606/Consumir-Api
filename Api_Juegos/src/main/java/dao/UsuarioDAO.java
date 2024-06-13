package dao;

import entidades.Usuario;
import org.hibernate.Session;
import util.HibernateUtil;

import javax.persistence.PersistenceException;
import java.util.List;

public class UsuarioDAO implements UsuarioDAOInterface{

    @Override
    public Usuario crearUsuario(Usuario u) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            session.save(u);
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return u;
    }

    @Override
    public List<Usuario> devolverTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Consulta HQL
        List<Usuario> todos = session.createQuery("from Usuario", Usuario.class).list();
        session.close();

        return todos;
    }

    @Override
    public Usuario buscarPorID(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Usuario u = session.find(Usuario.class, id);
        session.close();

        return u;
    }

    @Override
    public Usuario update(Usuario usuario) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            session.update(usuario);
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return usuario;
    }

    @Override
    public boolean deleteById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            Usuario usuario = this.buscarPorID(id);

            session.delete(usuario);

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
