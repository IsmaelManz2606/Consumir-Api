document.addEventListener("DOMContentLoaded", function () {
  fetchUsuarios();
});

function fetchUsuarios() {
  fetch("http://localhost:8080/usuarios")
    .then((response) => {
      if (!response.ok) {
        throw new Error("Error en la solicitud: " + response.statusText);
      }
      return response.json();
    })
    .then((data) => {
      TarjetaUser(data);
      TablaUser(data);
    })
    .catch((error) => console.error("Error:", error));
}

function TarjetaUser(usuarios) {
  const content = document.getElementById("content");
  content.innerHTML = usuarios
    .map(
      (usuario) => `
        <div class="user-card">
          <h2 class="text-white">${usuario.nombre}</h2>
          <p>Email: ${usuario.email}</p>
          <p>Edad: ${usuario.edad}</p>
        </div>`
    )
    .join("");
}

function TablaUser(usuarios) {
  const tableBody = document.getElementById("tabla-usuarios-body");
  tableBody.innerHTML = usuarios
    .map(
      (usuario) => `
        <tr>
          <td>${usuario.nombre}</td>
          <td>${usuario.email}</td>
          <td>${usuario.edad}</td>
          <td>
            <button onclick="editarUsuario(${usuario.id})" class="btn-editar">Editar</button>
            <button onclick="borrarUsuario(${usuario.id})" class="btn-borrar">Borrar</button>
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

function editarUsuario(id) {
  if (confirm("¿Quiere editarlo?")) {
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
      actualizarUsuario(id);
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

    // Email
    const emailLabel = document.createElement("label");
    emailLabel.textContent = "Email:";
    form.appendChild(emailLabel);
    const emailInput = document.createElement("input");
    emailInput.type = "email";
    emailInput.id = "email-edit";
    form.appendChild(emailInput);

    // Edad
    const edadLabel = document.createElement("label");
    edadLabel.textContent = "Edad:";
    form.appendChild(edadLabel);
    const edadInput = document.createElement("input");
    edadInput.type = "number";
    edadInput.id = "edad-edit";
    edadInput.min = 0;
    edadInput.max = 120;
    form.appendChild(edadInput);

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

function actualizarUsuario(id) {
  const nombre = document.getElementById("nombre-edit").value;
  const email = document.getElementById("email-edit").value;
  const edad = document.getElementById("edad-edit").value;

  const usuarioActualizado = {
    nombre: nombre,
    email: email,
    edad: parseInt(edad),
  };

  fetch(`http://localhost:8080/usuarios/actualizar/${id}`, {
    method: "POST",
    body: JSON.stringify(usuarioActualizado),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Error en la solicitud: " + response.statusText);
      }
      return response.json();
    })
    .then((data) => {
      console.log("Usuario actualizado:", data);
      fetchUsuarios();
    })
    .catch((error) => console.error("Error:", error));
}

function borrarUsuario(id) {
  if (confirm("¿Estás seguro de que deseas borrar este usuario?")) {
    fetch(`http://localhost:8080/usuarios/delete/${id}`, {
      method: "POST",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Error en la solicitud: " + response.statusText);
        }
        // Actualizar la tabla después de borrar el usuario
        fetchUsuarios();
      })
      .catch((error) => console.error("Error:", error));
  }
}

document.addEventListener("DOMContentLoaded", function () {
  const crearUsuarioForm = document.getElementById("crearUsuarioForm");
  crearUsuarioForm.addEventListener("submit", function (event) {
    event.preventDefault(); // Evitar el envío del formulario por defecto

    const nombre = document.getElementById("nombre").value;
    const email = document.getElementById("email").value;
    const edad = document.getElementById("edad").value;

    const usuario = {
      nombre: nombre,
      email: email,
      edad: parseInt(edad),
    };

    fetch("http://localhost:8080/usuarios/crear", {
      method: "POST",
      body: JSON.stringify(usuario),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Error en la solicitud: " + response.statusText);
        }
        return response.json();
      })
      .then((data) => {
        console.log("Usuario creado:", data);
        // Limpiar el formulario después de crear el usuario
        crearUsuarioForm.reset();
        // Actualizar la tabla de usuarios
        fetchUsuarios();
      })
      .catch((error) => console.error("Error:", error));
  });
});
