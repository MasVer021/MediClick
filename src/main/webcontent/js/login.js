document.addEventListener("DOMContentLoaded", function() {
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const form = document.querySelector("form");

    function validaEmail() {
        if (!emailInput) {
            return true;
        }
        return validaCampo(emailInput, validators.email, "Inserire una mail valida");
    }

    function validaPassword() {
        if (!passwordInput) {
            return true;
        }
        return validaCampo(passwordInput, (val) => val.trim().length > 0, "La password non può essere vuota");
    }

    if (emailInput) {
        emailInput.addEventListener("blur", validaEmail);
    }
    if (passwordInput) {
        passwordInput.addEventListener("blur", validaPassword);
    }

    if (form) {
        form.addEventListener("submit", function(event) {
            let isFormValido = true;
            if (!validaEmail()) {
                isFormValido = false;
            }
            if (!validaPassword()) {
                isFormValido = false;
            }

            if (!isFormValido) {
                event.preventDefault();
            }
        });
    }
});
