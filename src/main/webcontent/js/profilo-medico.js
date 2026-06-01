document.addEventListener("DOMContentLoaded",function()
{
	
	const pIvaInput = document.getElementById("pIva");
	
	if(pIvaInput)
	{
		pIvaInput.addEventListener("blur",() =>validaCampo(pIvaInput, validators.partitaIva, "Inserire una PIVA valida"));
	}
	
	
	const formProfilo = document.getElementById("form-profilo-medico");
		
		if(formProfilo)
		{
			formProfilo.addEventListener("submit", function(event) 
				{
				   
				    let isFormValido = true;
				    
				    if (pIvaInput && !validaCampo(pIvaInput, validators.partitaIva, "Inserire una PIVA valida")) 
					{
				        isFormValido = false;
				    }
					
					if(!isFormValido)
					{
						event.preventDefault();
					}
				});
		}
	
	
	const durata = document.getElementById("durata");
	
	const prezzo = document.getElementById("prezzo");
	
	if(durata)
	{
		durata.addEventListener("blur",()=> validaCampo(durata,(val)=>parseInt(val) >= 30 && parseInt(val) % 30 === 0,"Inserisci una durata che sia multipla di 30 min"));
	}
	
	if(prezzo)
	{
		prezzo.addEventListener("blur",()=> validaCampo(prezzo,(val)=>parseFloat(val)>=0,"Inserire un prezzo positivo"));
	}
	
		
	const formPrestazioni = document.getElementById("form-prestazioni-medico");
	
	if(formPrestazioni)
	{
		formPrestazioni.addEventListener("submit", function(event) 
		{
		   
		    let isFormValido = true;
		    
		    
		    if (durata && !validaCampo(durata,(val)=>validators.isStep30(0,val),"Inserisci una durata che sia multipla di 30 min")) 
			{
		        isFormValido = false;
		    }
			
			if (prezzo && !validaCampo(prezzo,(val)=>parseFloat(val)>=0,"Inserire un prezzo positivo")) 
			{
		        isFormValido = false;
		    }
		    
			if(!isFormValido)
			{
				event.preventDefault();
			}
		   
		   
		});
	}
});