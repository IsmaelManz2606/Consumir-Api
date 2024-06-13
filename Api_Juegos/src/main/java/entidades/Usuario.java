package entidades;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Column(name = "nombre", length = 255)
    @Expose
    private String nombre;

    @Column(name = "email", length = 255, unique = true)
    @Expose
    private String email;

    @Column(name = "edad")
    @Expose
    private Integer edad;


    @ManyToMany(mappedBy = "usuarios")
    private List<Juego> juegos=new ArrayList<>();

    public List<Juego> getJuegos() {
        return juegos;
    }
    public void setJuegos(List<Juego> juegos) {
        this.juegos = juegos;
    }



    // constructores
    public Usuario() {}

    public Usuario(String nombre, String email, Integer edad) {
        this.nombre = nombre;
        this.email = email;
        this.edad = edad;
    }

    // getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    // toString
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", edad=" + edad +
                '}';
    }
}
