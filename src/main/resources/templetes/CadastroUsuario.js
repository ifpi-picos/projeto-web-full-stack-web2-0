function register() {
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;

    var user = {
        "username": username,
        "senha": password
    };

    $.ajax({
        type: "POST",
        url: "/register",
        contentType: "application/json",
        data: JSON.stringify(user),
        success: function (response) {
            alert("Usuário registrado com sucesso!");
        },
        error: function (error) {
            alert("Erro ao registrar usuário: " + error.responseText);
        }
    });
}
