package entidades;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Categoria")
public class Categoria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Column(name = "nombre", length = 255, unique = true)
    @Expose
    private String nombre;

    @Column(name = "descripcion", length = 500)
    @Expose
    private String descripcion;

    @Column(name = "popularidad")
    @Expose
    private Integer popularidad;


    @OneToMany(mappedBy = "cat",fetch = FetchType.LAZY)
    private List<Juego> variable=new ArrayList<>();
    public List<Juego> getJuego() {
        return this.variable;
    }

    public List<Juego> getVariable() {
        return variable;
    }

    public void setVariable(List<Juego> variable) {
        this.variable = variable;
    }


    // constructores
    public Categoria() {}

    public Categoria(String nombre, String descripcion, Integer popularidad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.popularidad = popularidad;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getPopularidad() {
        return popularidad;
    }

    public void setPopularidad(Integer popularidad) {
        this.popularidad = popularidad;
    }

    // toString
    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", popularidad=" + popularidad +
                '}';
    }
}

