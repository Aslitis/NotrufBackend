//aus den HTML-Elementen werden die Informationen ueber die verwendeten IDs genommen
const loginForm = document.getElementById("login-form");
const loginButton = document.getElementById("login-form-submit");
const loginErrorMsg = document.getElementById("login-error-msg");

loginButton.addEventListener("click", (e) => {
    e.preventDefault();
    const username = loginForm.username.value;
    const password = loginForm.password.value;

    //pruefen, ob das korrekte Passwort eingegeben wurde
    //noch hardcoded
    //wenn korrekter Login dann Weiterleiten auf die eigentliche Website
    if (username === "Admin" && password === "SicheresPasswort") {
        alert("You have successfully logged in.");
        //location.reload();
        location.replace("./Website/website.html");
    } else {
        loginErrorMsg.style.opacity = 1;
    }
})