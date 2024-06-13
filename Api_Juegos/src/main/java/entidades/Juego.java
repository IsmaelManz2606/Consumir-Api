package entidades;

import com.google.gson.annotations.Expose;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Juego")
public class Juego implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Column(name = "nombre", length = 255)
    @NotNull
    @Expose
    private String nombre;

    @Column(name = "puntuacion")
    @NotNull
    @Expose
    private Double puntuacion;

    @Column(name = "fechaLanzamiento")
    @Expose
    private String fechaLanzamiento;

    @Column(name = "imagenURL")
    @Expose
    private String imagenURL;

    @Column(name = "desarrollador")
    @Expose
    private String desarrollador;

    @Column(name = "plataforma")
    @Expose
    private String plataforma;

    //Relaciones
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="categoria_id",foreignKey = @ForeignKey(name = "fk_juego_cat"))
    @Expose
    private Categoria cat;
    public Categoria getCat() {
        return cat;
    }
    public void setCat(Categoria cat) {
        this.cat = cat;
    }


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_juego",
            joinColumns = @JoinColumn(name = "juego_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )

    private List<Usuario> usuarios=new ArrayList<>();

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    // constructores
    public Juego() {}

    public Juego(String nombre, Double puntuacion, String fechaLanzamiento, String imagenURL, String desarrollador, String plataforma) {
        this.nombre = nombre;
        this.puntuacion = puntuacion;
        this.fechaLanzamiento = fechaLanzamiento;
        this.imagenURL = imagenURL;
        this.desarrollador = desarrollador;
        this.plataforma = plataforma;
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

    public Double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(String fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(String desarrollador) {
        this.desarrollador = desarrollador;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    // toString
    @Override
    public String toString() {
        return "Juego{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", puntuacion=" + puntuacion +
                ", fechaLanzamiento='" + fechaLanzamiento + '\'' +
                ", imagenURL='" + imagenURL + '\'' +
                ", desarrollador='" + desarrollador + '\'' +
                ", plataforma='" + plataforma + '\'' +
                '}';
    }
}