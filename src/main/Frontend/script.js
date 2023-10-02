function login() {
    const server = "http://127.0.0.1:"

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;
  const data = {
    email: email,
    password: password,
  };
  fetch(server + "8084/api/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  })
    .then((response) => response.json())
    .then((data) => {
      document.getElementById("user_id").innerText = data.user_id;
      document.getElementById("token").innerText = data.token;
      document.getElementById("issued_at").innerText = data.issued_at;
      document.getElementById("expires_at").innerText = data.expires_at;
      authFetch(data.token, server, "info", "full_info");
      authFetch(data.token, server, "myhobbies", "hobby");
      authFetch(data.token, server, "allusers", "all_users");


    })
    .catch((error) => {
      console.error("Error:", error);
    });
}
function authFetch(token, server, endpoint, divToFill) {
    fetch(server + "8084/api/" + endpoint, {
        method: "GET",
        headers: { 
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        }
      })
        .then((response) => response.json())
        .then((data) => {
          document.getElementById(divToFill).innerText = JSON.stringify(data);
        })
        .catch((error) => {
          console.error("Error:", error);
        });
}

function register() {
    const server = "http://127.0.0.1:"
    const token = document.getElementById("token").value;

  const email = document.getElementById("email_reg").value;
  const password = document.getElementById("password_reg").value;
  const role = document.getElementById("role_reg").value;
  const first_name = document.getElementById("first_name_reg").value;
  const last_name = document.getElementById("last_name_reg").value;

  const data = {
    email: email,
    password: password,
    role: role,
    first_name: first_name,
    last_name: last_name
  };
  console.log(data);
  fetch(server + "8084/api/register", {
    method: "POST",
    headers: {
        "Content-Type": "application/json", 
        "Authorization": "Bearer " + token
    },
    body: JSON.stringify(data),
  })
    .then((response) => response.json())
    .then((data) => console.log(data))
    .catch((error) => {
      console.error("Error:", error);
    });

}
