document.addEventListener("DOMContentLoaded", () => {
  startApp();
});

function startApp() {
  // Agregar evento click a los botones
  document.getElementById("btn-juegos").addEventListener("click", () => {
    fetchAndDisplayData("juegos");
  });

  document.getElementById("btn-categorias").addEventListener("click", () => {
    fetchAndDisplayData("categorias");
  });

  document.getElementById("btn-usuarios").addEventListener("click", () => {
    fetchAndDisplayData("usuarios");
  });
}

function fetchAndDisplayData(tabla) {
  fetch(`http://localhost:8080/${tabla}`)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Error en la solicitud: " + response.statusText);
      }
      return response.json();
    })
    .then((data) => {
      displayData(data, tabla);
    })
    .catch((error) => console.error("Error:", error));
}

function displayData(data, tabla) {
  const adminContent = document.getElementById("admin-content");
  let tableHTML = `<table class="admin-table"><thead><tr>`;
  // Crear encabezados de tabla basados en la primera fila de datos
  Object.keys(data[0]).forEach((key) => {
    tableHTML += `<th>${key}</th>`;
  });
  tableHTML += `</tr></thead><tbody>`;
  // Agregar filas de datos
  data.forEach((row) => {
    tableHTML += `<tr>`;
    Object.values(row).forEach((value) => {
      tableHTML += `<td>${value}</td>`;
    });
    tableHTML += `</tr>`;
  });
  tableHTML += `</tbody></table>`;
  adminContent.innerHTML = tableHTML;
}
