import dao.AsociacionesDAO;
import dao.CategoriaDAO;
import dao.JuegoDAO;
import dao.UsuarioDAO;
import servicios.JuegosAPIREST;

public class Servidor {

    public static void main(String[] args) {

        JuegosAPIREST api=new JuegosAPIREST(new JuegoDAO(),new CategoriaDAO(),new UsuarioDAO(), new AsociacionesDAO() );
    }
}