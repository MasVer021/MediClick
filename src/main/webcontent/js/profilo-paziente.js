document.addEventListener("DOMContentLoaded",function()
{
	
	const passwordInput = document.getElementById("nuovaPassword");
	const passwordRepInput = document.getElementById("confermaPassword");
	const telInput = document.getElementById("nuovoTelefono");
	
	if(passwordInput)
	{
		passwordInput.addEventListener("blur",() =>validaCampo(passwordInput, (val) => val.length >= 8, "Inserire una password di almeno 8 caratteri"));
	}
	
	if(passwordRepInput)
	{
		passwordRepInput.addEventListener("blur",() =>validaCampo(passwordRepInput, (val) => val === passwordInput.value, "Le due password non corrispondono"));
	}
	
	if(telInput)
	{
		telInput.addEventListener("blur",() =>validaCampo(telInput,validators.telefono,"Inserire un numero di telefono valido"));
	}

	const formTelefono = document.getElementById("form-telefono-paziente");
	
	if(formTelefono)
	{
		formTelefono.addEventListener("submit", function(event) 
			{
			   
			    let isFormValido = true;
			    
			    if (telInput && !validaCampo(telInput, validators.telefono, "Inserire un numero di telefono valido")) 
				{
			        isFormValido = false;
			    }
				
				if(!isFormValido)
				{
					event.preventDefault();
				}
			});
	}
	

	const formPassword = document.getElementById("form-password-paziente");
	
	if(formPassword)
	{
		formPassword.addEventListener("submit", function(event) 
		{
		   
		    let isFormValido = true;
		    
		    
		    if (passwordInput && !validaCampo(passwordInput, (val) => val.length >= 8, "Inserire una password di almeno 8 caratteri")) 
			{
		        isFormValido = false;
		    }
		    
		    if (passwordRepInput && !validaCampo(passwordRepInput, (val) => val === passwordInput.value, "Le due password non corrispondono")) 
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