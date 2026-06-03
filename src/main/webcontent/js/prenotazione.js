document.addEventListener("DOMContentLoaded", function() 
{
	const studioSelect = document.getElementById("studioSelect");
	const prestazioneSelect = document.getElementById("prestazioneSelect");
	const slotsContainer = document.getElementById("slotsContainer");
	const btnConferma = document.getElementById("btnConferma");
	
	const urlParams = new URLSearchParams(window.location.search);
	const medicoId = urlParams.get("id");
	
	const contextPath = getContextPath();
	
	studioSelect.addEventListener("change", function() 
	{
	    const studioId = this.value; 
	    if (!studioId) return;

	   
	    slotsContainer.innerHTML = '<p class="mc-text-muted">Caricamento in corso...</p>';
	  
	    const url = `${contextPath}/api/serviziStudio?studioId=${studioId}&medicoId=${medicoId}`;
		console.log(url);
		
	    fetch(url)
	        .then(response => 
			{
	            if (!response.ok) 
				{
	                throw new Error("Errore durante il recupero dei dati dal server");
	            }
	            return response.json();
	        })
	        .then(data => 
			{
	            
	            prestazioneSelect.innerHTML = '<option value="" disabled selected>Seleziona la Prestazione</option>';
	            data.prestazioni.forEach((prestazione) => 
				{
	                let opt = document.createElement("option");
	                opt.value = prestazione.id;
	                opt.textContent = `${prestazione.nome} - Durata: ${prestazione.durata} min - \u20AC${prestazione.prezzo.toFixed(2)}`;
	                prestazioneSelect.appendChild(opt);
	            });
	            
	          
	            slotsContainer.innerHTML = "";

	            
	            if (data.disponibilita.length === 0) 
				{
	                slotsContainer.innerHTML = '<p class="mc-text-muted">Nessun orario disponibile per questo studio.</p>';
	                btnConferma.disabled = true;
	                return;
	            }

	           
	            btnConferma.disabled = false;

	            
	            const orariPerGiorno = {};
	            data.disponibilita.forEach(function(slot) {
	                if (!orariPerGiorno[slot.data]) {
	                    orariPerGiorno[slot.data] = []; 
	                }
	                orariPerGiorno[slot.data].push(slot);
	            });

	            
	            const coppieOrdinate = Object.entries(orariPerGiorno).sort((a, b) => a[0].localeCompare(b[0]));

	           
	            coppieOrdinate.forEach(function(coppia) {
	                const dateStr = coppia[0];
	                const slots = coppia[1];
	                
	                
	                slots.sort((a, b) => a.ora.localeCompare(b.ora));
	                
	                const parti = dateStr.split("-");
	                const dataFormattata = `${parti[2]}/${parti[1]}/${parti[0]}`;
	               
	                const bloccoGiorno = document.createElement("div");
	                bloccoGiorno.className = "blocco-giorno mc-mb-md";
	                
	                const h4 = document.createElement("h4");
	                h4.className = "mc-text-muted mc-mb-sm";
	                h4.textContent = dataFormattata;
	                bloccoGiorno.appendChild(h4);
	                
	                const pulsantiera = document.createElement("div");
	                pulsantiera.className = "pulsantiera-orari mc-flex-row mc-gap-sm";
	                
	                slots.forEach(function(slot) {
	                    const opzioneOrario = document.createElement("div");
	                    opzioneOrario.className = "opzione-orario";
	                    
	                    const inputRadio = document.createElement("input");
	                    inputRadio.type = "radio";
	                    inputRadio.id = `slot_${slot.id}`;
	                    inputRadio.name = "disponibilitaId";
	                    inputRadio.value = slot.id;
	                    inputRadio.required = true;
	                    
	                    const label = document.createElement("label");
	                    label.setAttribute("for", `slot_${slot.id}`);
	                    label.textContent = slot.ora; 
	                    
	                    opzioneOrario.appendChild(inputRadio);
	                    opzioneOrario.appendChild(label);
	                    pulsantiera.appendChild(opzioneOrario);
	                });
	                
	                bloccoGiorno.appendChild(pulsantiera);
	                slotsContainer.appendChild(bloccoGiorno);
	            });
	        })
	        .catch(error => 
			{
	            console.error("Errore nel recupero dei dati:", error);
	            slotsContainer.innerHTML = '<p class="mc-text-danger">Errore di caricamento. Riprova.</p>';
	            btnConferma.disabled = true;
	        });
	});
});