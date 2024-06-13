package dao;

import entidades.Categoria;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.PersistenceException;
import java.util.List;


public class CategoriaDAO implements CategoriaDAOInterface{

    @Override
    public Categoria crearCategoria(Categoria c) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            session.save(c);
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return c;
    }

    public List<Categoria> devolverTodas() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Consulta HQL
        List<Categoria> todas = session.createQuery("from Categoria", Categoria.class).list();
        session.close();

        return todas;
    }



    @Override
    public Categoria buscarPorId(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Categoria c = session.find(Categoria.class, id);
        session.close();

        return c;
    }


    @Override
    public Categoria buscarCategoriaPorNombre(String nombreCategoria) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Categoria categoria = null;
        try {
            Query<Categoria> query = session.createQuery("FROM Categoria WHERE nombre = :nombre", Categoria.class);
            query.setParameter("nombre", nombreCategoria);
            categoria = query.uniqueResult();//porque solo esperamos un unico resultado
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return categoria;
    }

    @Override
    public Categoria update(Categoria categoria) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            session.update(categoria);
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return categoria;
    }

    @Override
    public boolean deleteById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            Categoria category = this.buscarPorId(id);

            session.delete(category);

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
