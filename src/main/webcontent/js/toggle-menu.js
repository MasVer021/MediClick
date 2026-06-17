function toggleMenu() {
    const links = document.getElementById("menuLinks");
    const toggle = document.querySelector(".mc-menu-toggle");
    if (links && toggle) {
        links.classList.toggle("active");
        toggle.classList.toggle("active");
    }
}


let pulsantehamburgerMenuElement = document.getElementById("hamburger-menu");

pulsantehamburgerMenuElement.addEventListener("click",()=>toggleMenu());



document.addEventListener("click", function(event) {
    const links = document.getElementById("menuLinks");
    const toggle = document.querySelector(".mc-menu-toggle");
    if (links && toggle && links.classList.contains("active")) {
        if (!links.contains(event.target) && !toggle.contains(event.target)) {
            links.classList.remove("active");
            toggle.classList.remove("active");
        }
    }
});