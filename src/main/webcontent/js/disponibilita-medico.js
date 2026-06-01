document.addEventListener("DOMContentLoaded",function()
{
	
	const giorno = document.getElementById("dataGiornata");
	const oraInizio = document.getElementById("oraInizio");
	const oraFine = document.getElementById("oraFine");
	
	if(giorno)
	{
		giorno.addEventListener("blur",() =>validaCampo(giorno,validators.isFuture , "Non puoi inserire una data passata"));;
	}
	
	if(oraFine)
	{
		oraFine.addEventListener("blur",() =>	validaCampo(oraFine,(val)=> val > oraInizio.value ,"L'ora fine deve essere successiva a quella di inizio") && 
												validaCampo(oraFine,(val)=> validators.isStep30(val,oraInizio.value) ,"L'intervallo deve essere un multiplo di 30 minuti"));
	}

	const form = document.getElementById("disponibilita-medico");
	
	if(form)
	{
		form.addEventListener("submit", function(event) 
			{
			   
			    let isFormValido = true;
			    
			    if (giorno && !validaCampo(giorno,validators.isFuture , "Non puoi inserire una data passata")) 
				{
			        isFormValido = false;
			    }
				
				if (oraFine && !(validaCampo(oraFine,(val)=> val > oraInizio.value ,"L'ora fine deve essere successiva a quella di inizio") && 	validaCampo(oraFine,(val)=> validators.isStep30(val,oraInizio.value) ,"L'intervallo deve essere un multiplo di 30 minuti"))) 
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