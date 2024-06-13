document.addEventListener("DOMContentLoaded", function () {
  fetchJuegos();
});

// Mostrar todos los juegos
function fetchJuegos() {
  fetch("http://localhost:8080/juegos")
    .then((response) => {
      if (!response.ok) {
        throw new Error("Error en la solicitud: " + response.statusText);
      }
      return response.json();
    })
    .then((data) => {
      renderCards(data);
      renderTable(data);
    })
    .catch((error) => console.error("Error:", error));
}

function renderCards(juegos) {
  const content = document.getElementById("content");
  content.innerHTML = juegos
    .map(
      (juego) => `
        <div class="game-card">
          <a href="game.html?id=${juego.id}">
            <h2 class="text-white">${juego.nombre}</h2>
            <img src="${juego.imagenURL}" alt="${juego.nombre}">
            <p>Desarrollador: ${juego.desarrollador}</p>
            <p>Fecha de Lanzamiento: ${juego.fechaLanzamiento}</p>
            <p>Plataforma: ${juego.plataforma}</p>
            <p>Puntuación: ${juego.puntuacion}</p>
            <p>Categoría: ${juego.cat ? juego.cat.nombre : "Desconocida"}</p>
          </a>
        </div>`
    )
    .join("");
}

function renderTable(juegos) {
  const tableBody = document.getElementById("tabla-juegos-body");
  tableBody.innerHTML = juegos
    .map(
      (juego) => `
        <tr>
          <td>${juego.nombre}</td>
          <td>${juego.desarrollador}</td>
          <td>${juego.fechaLanzamiento}</td>
          <td>${juego.plataforma}</td>
          <td>${juego.puntuacion}</td>
          <td>${juego.cat ? juego.cat.nombre : "Desconocida"}</td>
          <td>
            <button onclick="editarJuego(${juego.id})">Editar</button>
            <button onclick="borrarJuego(${juego.id})">Borrar</button>
          </td>
        </tr>`
    )
    .join("");
}
function cancelarEdicion() {
  if (formContainer) {
    formContainer.style.display = "none"; // Ocultar el formContainer
    const overlay = document.getElementById("overlay");
    overlay.style.display = "none"; // Ocultar el overlay
  }
}
// Variable global para el formContainer
let formContainer;

function editarJuego(id) {
  if (confirm("Quiere editarlo?")) {
    const overlay = document.getElementById("overlay");
    overlay.style.display = "flex"; // Mostrar el overlay

    if (!formContainer) {
      // Si el formContainer no existe, crear uno nuevo
      formContainer = document.createElement("div");
      formContainer.id = "form-container";
      document.body.appendChild(formContainer);
    } else {
      // Si el formContainer ya existe, limpiar su contenido
      formContainer.innerHTML = "";
    }

    const form = document.createElement("form");
    form.id = "form";
    form.onsubmit = function (event) {
      event.preventDefault(); // Evitar que se envíe el formulario automáticamente
      actualizarJuego(id);
      formContainer.style.display = "none"; // Ocultar el formContainer después de actualizar
      overlay.style.display = "none"; // Ocultar el overlay después de actualizar
    };

    // Nombre
    const nombreLabel = document.createElement("label");
    nombreLabel.textContent = "Nombre:";
    form.appendChild(nombreLabel);
    const nombreInput = document.createElement("input");
    nombreInput.type = "text";
    nombreInput.id = "nombre-edit";
    form.appendChild(nombreInput);

    // Desarrollador
    const desarrolladorLabel = document.createElement("label");
    desarrolladorLabel.textContent = "Desarrollador:";
    form.appendChild(desarrolladorLabel);
    const desarrolladorInput = document.createElement("input");
    desarrolladorInput.type = "text";
    desarrolladorInput.id = "desarrollador-edit";
    form.appendChild(desarrolladorInput);

    // Fecha de Lanzamiento
    const fechaLabel = document.createElement("label");
    fechaLabel.textContent = "Fecha de Lanzamiento:";
    form.appendChild(fechaLabel);
    const fechaInput = document.createElement("input");
    fechaInput.type = "date";
    fechaInput.id = "fechaLanzamiento-edit";
    form.appendChild(fechaInput);

    // Plataforma
    const plataformaLabel = document.createElement("label");
    plataformaLabel.textContent = "Plataforma:";
    form.appendChild(plataformaLabel);
    const plataformaInput = document.createElement("input");
    plataformaInput.type = "text";
    plataformaInput.id = "plataforma-edit";
    form.appendChild(plataformaInput);

    // Puntuación
    const puntuacionLabel = document.createElement("label");
    puntuacionLabel.textContent = "Puntuación:";
    form.appendChild(puntuacionLabel);
    const puntuacionInput = document.createElement("input");
    puntuacionInput.type = "number";
    puntuacionInput.id = "puntuacion-edit";
    puntuacionInput.min = 0;
    puntuacionInput.max = 10;
    form.appendChild(puntuacionInput);

    // Botón de enviar
    const submitButton = document.createElement("button");
    submitButton.type = "submit";
    submitButton.textContent = "Actualizar";
    form.appendChild(submitButton);

    // Espacio entre los botones
    const spaceDiv = document.createElement("div");
    spaceDiv.style.height = "10px"; // Ajusta la altura según necesites
    form.appendChild(spaceDiv);

    // Botón de cancelar
    const cancelButton = document.createElement("button");
    cancelButton.type = "button";
    cancelButton.textContent = "Cancelar";
    cancelButton.onclick = cancelarEdicion;
    form.appendChild(cancelButton);

    formContainer.appendChild(form);
    document.body.appendChild(formContainer);
    formContainer.style.display = "block";
  }
}

function actualizarJuego(id) {
  const nombre = document.getElementById("nombre-edit").value;
  const desarrollador = document.getElementById("desarrollador-edit").value;
  const fechaLanzamiento = document.getElementById(
    "fechaLanzamiento-edit"
  ).value;
  const plataforma = document.getElementById("plataforma-edit").value;
  const puntuacion = document.getElementById("puntuacion-edit").value;

  const juegoActualizado = {
    nombre: nombre,
    desarrollador: desarrollador,
    fechaLanzamiento: fechaLanzamiento,
    plataforma: plataforma,
    puntuacion: parseFloat(puntuacion),
  };

  console.log(JSON.stringify(juegoActualizado));

  fetch(`http://localhost:8080/juegos/actualizar/${id}`, {
    method: "POST",
    body: JSON.stringify(juegoActualizado),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Error en la solicitud: " + response.statusText);
      }
      return response.json();
    })
    .then((data) => {
      console.log("Juego actualizado:", data);
      fetchJuegos();
    })
    .catch((error) => console.error("Error:", error));
}

function borrarJuego(id) {
  if (confirm("¿Estás seguro de que deseas borrar este juego?")) {
    fetch(`http://localhost:8080/juegos/delete/${id}`, {
      method: "POST",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Error en la solicitud: " + response.statusText);
        }
        // Actualizar la tabla después de borrar el juego
        fetchJuegos();
      })
      .catch((error) => console.error("Error:", error));
  }
}

document.addEventListener("DOMContentLoaded", function () {
  const crearJuegoForm = document.getElementById("crearJuegoForm");
  crearJuegoForm.addEventListener("submit", function (event) {
    event.preventDefault(); // Evitar que se envíe el formulario automáticamente

    const nombre = document.getElementById("nombre").value;
    const desarrollador = document.getElementById("desarrollador").value;
    const fechaLanzamiento = document.getElementById("fechaLanzamiento").value;
    const plataforma = document.getElementById("plataforma").value;
    const puntuacion = document.getElementById("puntuacion").value;

    const nuevoJuego = {
      nombre: nombre,
      desarrollador: desarrollador,
      fechaLanzamiento: fechaLanzamiento,
      plataforma: plataforma,
      puntuacion: parseFloat(puntuacion),
    };

    // Enviar los datos al servidor para crear un nuevo juego
    fetch("http://localhost:8080/juegos/crear-nuevo", {
      method: "POST",
      body: JSON.stringify(nuevoJuego),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Error en la solicitud: " + response.statusText);
        }
        return response.json();
      })
      .then((data) => {
        console.log("Nuevo juego creado:", data);
        // Actualizar la lista de juegos
        fetchJuegos();
        // Limpiar el formulario
        crearJuegoForm.reset();
      })
      .catch((error) => console.error("Error:", error));
  });
});
