document.addEventListener("DOMContentLoaded",function()
{
	function ricercasuggeriti(idMedici,idCitta,idInputGroup,idSuggestionGroup,chiave)
	{
		let query = document.getElementById(idMedici).value;
		let citta = document.getElementById(idCitta).value;
		
		let url = getContextPath()+"/api/suggest?query="+query+"&citta="+citta;
		
		console.log(url);

		fetch(url)
		.then(response =>
		{
			if(!response.ok)
				{
					throw Error("Errore del server");
				}
			return response.json();
		})
		.then(data=>
		{
			let inputGroup = document.getElementById(idInputGroup);
			let lista = document.getElementById(idSuggestionGroup);
			
			let elementi = data[chiave] || [];
			
			if(elementi.length<1)
			{
				if(lista)
				{
					inputGroup.removeChild(lista);
				}
				 return;
			}
			
			if(lista)
			{
				lista.replaceChildren();
			}
			else
			{
				lista = document.createElement("div");
				lista.id = idSuggestionGroup;
				lista.classList.add("mc-suggest")
				inputGroup.appendChild(lista)
			}
			
			console.log("medici:"+data.medici);
			console.log("citta:"+data.citta);
			
			elementi.forEach((m)=>
			{
				let OptionElement = document.createElement("p");
				OptionElement.textContent = m;
				
				OptionElement.addEventListener("click",function()
				{
					let value = OptionElement.textContent;
					let idInput = chiave === "citta" ? idCitta: idMedici;
					document.getElementById(idInput).value = value;
					lista.remove();
					
				});
				console.log(m);
				lista.appendChild(OptionElement);
			});
		})
		.catch(error => 
		{
			console.error("Errore durante il recupero dei suggerimenti:", error);
		});	
	}
	
	let inputMediciElement = document.getElementById("querySpecialista");
	
	
	if(inputMediciElement)
	{
		
		inputMediciElement.addEventListener("input",()=>ricercasuggeriti("querySpecialista","citta","medici-input-search-group","medico-suggest","medici"));
	
	}
	
	let inputcittaElement = document.getElementById("citta");

		if(inputcittaElement)
		{
			inputcittaElement.addEventListener("input",()=>ricercasuggeriti("querySpecialista","citta","citta-input-search-group","citta-suggest","citta"));
		}
		
		document.addEventListener("click", function(event) 
		{
			let suggestionBoxes = document.querySelectorAll(".mc-suggest");
			   
		   suggestionBoxes.forEach(function(box) {
		      
		       let inputGroup = box.parentElement;
		       
		     
		       if (inputGroup && !inputGroup.contains(event.target)) {
		           box.remove(); 
		       }
		   });
		});
});