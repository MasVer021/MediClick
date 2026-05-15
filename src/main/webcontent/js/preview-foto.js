document.addEventListener('DOMContentLoaded', function() {
    const fotoInput = document.getElementById('fotoprofilo');
    const previewImg = document.getElementById('preview-foto');

    if (fotoInput && previewImg) {
        fotoInput.addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (!file) {
                previewImg.style.display = 'none';
                return;
            }
            
            const reader = new FileReader();
            reader.onload = function(ev) {
                previewImg.src = ev.target.result;
                previewImg.style.display = 'block';
            };
            reader.readAsDataURL(file);
        });
    }
});
