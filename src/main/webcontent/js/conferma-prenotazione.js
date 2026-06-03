document.addEventListener("DOMContentLoaded",function()
{
	const timerElement = document.getElementById("timer");
	if(!timerElement)
	{
		return 
	}
	
	let tempo_rimanente = 15*60;

   const contextPath = getContextPath();
   
   const interval = setInterval(function()
   {
		tempo_rimanente--;
		
		let minuti = Math.floor(tempo_rimanente/60);
		let secondi = tempo_rimanente % 60;
		
		const minutiFormattati = minuti < 10 ? "0" + minuti : minuti;
		const secondiFormattati = secondi < 10 ? "0" + secondi : secondi;
		
		timerElement.textContent = minutiFormattati + ":" +secondiFormattati ;
		
		if (tempo_rimanente <= 0) 
		{
           clearInterval(interval);         
          
           window.location.href = `${contextPath}/paziente/prenotazione?action=annulla`;
		}
	
   },1000);
   
   
   const btnCodiceScontoElement = document.getElementById("btn-codice-sconto");
   const inpCodiceScontoElement = document.getElementById("codiceSconto");
   
   if(btnCodiceScontoElement)
	{
		btnCodiceScontoElement.addEventListener("click",function()
		{
			
			if(!inpCodiceScontoElement)
			{
				return ;
			}
			
			const codice = inpCodiceScontoElement.value;
			
			const url = contextPath+"/paziente/api/sconto?codiceSconto="+codice;
			
			fetch(url)
			.then(response => 
			{
	            if (!response.ok) 
				{
	                throw new Error("Errore durante il recupero dei dati dal server");
	            }
	            return response.json();
			})
			.then(data=>
			{
				if(codice === data.codice)
				{
						
					let div_prezzo = document.getElementById("riepilogo-prezzo");
					
					const vecchiSconti = div_prezzo.querySelectorAll(".dati-sconto-dinamico");
					vecchiSconti.forEach(el => el.remove());
					
					
					let labelSconto = document.createElement("span");
					let campoSconto = document.createElement("span");
					
					let labelTotale = document.createElement("span");
					let campoTotale = document.createElement("span");
					
					let  prezzoLordo = parseFloat(div_prezzo.children[1].textContent);
					
					let sconto = prezzoLordo * (parseFloat(data.percentuale) / 100); 
					let prezzoFinale = prezzoLordo - sconto;
					
					labelSconto.className = "mc-text-muted mc-font-xs dati-sconto-dinamico mc-mt-xs";
                   campoSconto.className = "mc-text-bold mc-font-md dati-sconto-dinamico";
                   labelTotale.className = "mc-text-muted mc-font-xs dati-sconto-dinamico mc-mt-xs";
                   campoTotale.className = "mc-text-bold mc-font-md dati-sconto-dinamico text-success";
					
					labelSconto.textContent = "Sconto applicato("+codice+")";
					campoSconto.textContent = "-" + sconto.toFixed(2) + " \u20AC";
					
					labelTotale.textContent = "Prezzo totale";
					campoTotale.textContent = prezzoFinale.toFixed(2) + " \u20AC";
					
					
					div_prezzo.append(labelSconto);
					div_prezzo.append(campoSconto);
					
					div_prezzo.append(labelTotale);
					div_prezzo.append(campoTotale);
					
					inpCodiceScontoElement.disabled = true;
					btnCodiceScontoElement.disabled = true;
				}
				else
				{
					 throw new Error("Codice non valido");
				}
			}
			)		
			.catch(error => 
			{
	            console.error("Errore nel recupero dei dati:", error);
	           	showError(inpCodiceScontoElement.parentNode,"Codice sconto non valido");
	           
	        });
		});	
	}	
   
   
   
   
	
});