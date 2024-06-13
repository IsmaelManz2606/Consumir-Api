//
document.addEventListener("DOMContentLoaded", () => {
  startApp();
});

function startApp() {
  searchedGames();
}

function searchedGames() {
  const searchParams = new URL(document.location).searchParams;
  const id = searchParams.get("id");
  console.log(searchParams.get("id"));
  fetch(`http://localhost:8080/juegos/search/${id}`)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Error en la solicitud: " + response.statusText);
      }
      return response.json();
    })
    .then((data) => {
      const content = document.getElementById("content-game");
      content.innerHTML = `
                  <div class="game-card">
                      <h2 class="text-white">${data.nombre}</h2>
                      <img src="${data.imagenURL}" alt="${data.nombre}">
                      <p>Desarrollador: ${data.desarrollador}</p>
                      <p>Fecha de Lanzamiento: ${data.fechaLanzamiento}</p>
                      <p>Plataforma: ${data.plataforma}</p>
                      <p>Puntuación: ${data.puntuacion}</p>
                      <p>Categoría: ${
                        data.cat ? data.cat.nombre : "Desconocida"
                      }</p>
                  </div>
              `;
    })
    .catch((error) => console.error("Error:", error));
}
