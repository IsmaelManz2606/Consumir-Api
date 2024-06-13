document.addEventListener("DOMContentLoaded", function () {
  fetchCategorias();
});

function fetchCategorias() {
  fetch("http://localhost:8080/categorias")
    .then((response) => {
      if (!response.ok) {
        throw new Error("Error en la solicitud: " + response.statusText);
      }
      return response.json();
    })
    .then((data) => {
      TarjetasCategoria(data);
      TablaCategorias(data);
    })
    .catch((error) => console.error("Error:", error));
}

function TarjetasCategoria(categorias) {
  const content = document.getElementById("content");
  content.innerHTML = categorias
    .map(
      (categoria) => `
            <div class="category-card">
              <h2 class="text-white">${categoria.nombre}</h2>
              <p>Descripción: ${categoria.descripcion}</p>
              <p>Popularidad: ${categoria.popularidad}</p>
            </div>`
    )
    .join("");
}

function TablaCategorias(categorias) {
  const tableBody = document.getElementById("tabla-categorias-body");
  tableBody.innerHTML = categorias
    .map(
      (categoria) => `
            <tr>
              <td>${categoria.nombre}</td>
              <td>${categoria.descripcion}</td>
              <td>${categoria.popularidad}</td>
              <td>
                <button onclick="editarCategoria(${categoria.id})">Editar</button>
                <button onclick="borrarCategoria(${categoria.id})">Borrar</button>
              </td>
            </tr>`
    )
    .join("");
}

function cancelarEdicion() {
  if (formContainer) {
    formContainer.style.display = "none";
    const overlay = document.getElementById("overlay");
    overlay.style.display = "none";
  }
}

let formContainer;

function editarCategoria(id) {
  if (confirm("¿Quiere editarlo?")) {
    const overlay = document.getElementById("overlay");
    overlay.style.display = "flex";

    if (!formContainer) {
      formContainer = document.createElement("div");
      formContainer.id = "form-container";
      document.body.appendChild(formContainer);
    } else {
      formContainer.innerHTML = "";
    }

    const form = document.createElement("form");
    form.id = "form";
    form.onsubmit = function (event) {
      event.preventDefault();
      actualizarCategoria(id);
      formContainer.style.display = "none";
      overlay.style.display = "none";
    };

    // Nombre
    const nombreLabel = document.createElement("label");
    nombreLabel.textContent = "Nombre:";
    form.appendChild(nombreLabel);
    const nombreInput = document.createElement("input");
    nombreInput.type = "text";
    nombreInput.id = "nombre-edit";
    form.appendChild(nombreInput);

    // Descripción
    const descripcionLabel = document.createElement("label");
    descripcionLabel.textContent = "Descripción:";
    form.appendChild(descripcionLabel);
    const descripcionInput = document.createElement("input");
    descripcionInput.type = "text";
    descripcionInput.id = "descripcion-edit";
    form.appendChild(descripcionInput);

    // Popularidad
    const popularidadLabel = document.createElement("label");
    popularidadLabel.textContent = "Popularidad:";
    form.appendChild(popularidadLabel);
    const popularidadInput = document.createElement("input");
    popularidadInput.type = "number";
    popularidadInput.id = "popularidad-edit";
    popularidadInput.min = 0;
    popularidadInput.max = 10;
    form.appendChild(popularidadInput);

    // Botón de enviar
    const submitButton = document.createElement("button");
    submitButton.type = "submit";
    submitButton.textContent = "Actualizar";
    form.appendChild(submitButton);

    // Espacio entre los botones
    const spaceDiv = document.createElement("div");
    spaceDiv.style.height = "10px";
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

function actualizarCategoria(id) {
  const nombre = document.getElementById("nombre-edit").value;
  const descripcion = document.getElementById("descripcion-edit").value;
  const popularidad = document.getElementById("popularidad-edit").value;

  const categoriaActualizada = {
    nombre: nombre,
    descripcion: descripcion,
    popularidad: parseFloat(popularidad),
  };

  fetch(`http://localhost:8080/categorias/actualizar/${id}`, {
    method: "POST",
    body: JSON.stringify(categoriaActualizada),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Error en la solicitud: " + response.statusText);
      }
      return response.json();
    })
    .then((data) => {
      console.log("Categoría actualizada:", data);
      fetchCategorias();
    })
    .catch((error) => console.error("Error:", error));
}

function borrarCategoria(id) {
  if (confirm("¿Estás seguro de que deseas borrar esta categoría?")) {
    fetch(`http://localhost:8080/categorias/delete/${id}`, {
      method: "POST",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Error en la solicitud: " + response.statusText);
        }
        fetchCategorias();
      })
      .catch((error) => console.error("Error:", error));
  }
}

document.addEventListener("DOMContentLoaded", function () {
  const crearCategoriaForm = document.getElementById("crearCategoriaForm");
  crearCategoriaForm.addEventListener("submit", function (event) {
    event.preventDefault();

    const nombre = document.getElementById("nombre").value;
    const descripcion = document.getElementById("descripcion").value;
    const popularidad = document.getElementById("popularidad").value;

    const nuevaCategoria = {
      nombre: nombre,
      descripcion: descripcion,
      popularidad: parseFloat(popularidad),
    };

    fetch("http://localhost:8080/categorias/crear", {
      method: "POST",
      body: JSON.stringify(nuevaCategoria),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Error en la solicitud: " + response.statusText);
        }
        return response.json();
      })
      .then((data) => {
        console.log("Nueva categoría creada:", data);
        fetchCategorias();
        crearCategoriaForm.reset();
      })
      .catch((error) => console.error("Error:", error));
  });
});
