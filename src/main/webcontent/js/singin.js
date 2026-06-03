document.addEventListener("DOMContentLoaded",function()
{
	const emailInput = document.getElementById("email");
	const passwordInput = document.getElementById("password");
	const passwordRepInput = document.getElementById("passwordRipetuta");
	const pIvaInput = document.getElementById("PIva");
	const cfInput = document.getElementById("CF");
	const telInput = document.getElementById("telefono");
	const birthInput = document.getElementById("DataNascita");

	emailInput.addEventListener("blur",() =>validaCampo(emailInput,validators.email,"Inserire una mail valida"));
	passwordInput.addEventListener("blur",() =>validaCampo(passwordInput,(val)=> val.length >=8,"Inserire una password di almento 8 caratteri"));
	passwordRepInput.addEventListener("blur",() =>validaCampo(passwordRepInput,(val)=> val === passwordInput.value,"Le due password non corrispondono"));
	
	
	
	if (emailInput) 
	{
		emailInput.addEventListener("blur", function() 
		{
			const email = emailInput.value.trim();
			
			
			if (email === "" || !validators.email(email)) return;
			
			const contextPath = getContextPath();
			
			const url = `${contextPath}/api/checkEmail?email=${encodeURIComponent(email)}`;
			
			
			fetch(url)
				.then(response => {
					if (!response.ok) {
						throw new Error("Errore del server");
					}
					return response.json();
				})
				.then(data => 
					{
					if (data.exists) 
					{
						
						showError(emailInput, "Questa e-mail è già registrata nel sistema.");
					}
					 else 
					{
						
						clearError(emailInput);
					}
				})
				.catch(error => {
					console.error("Errore durante il controllo email:", error);
				});
		});
	}

	if(pIvaInput)
	{
		pIvaInput.addEventListener("blur",() =>validaCampo(pIvaInput,validators.partitaIva,"Inserire una PIVA valida"));
	}

	if(cfInput)
	{
		cfInput.addEventListener("blur",() =>validaCampo(cfInput,validators.codiceFiscale,"Inserire un codice fiscale valido"));
	}

	if(telInput)
	{
		telInput.addEventListener("blur",() =>validaCampo(telInput,validators.telefono,"Inserire un numero di telefono valido"));
	}

	if(birthInput)
	{
		birthInput.addEventListener("blur",() =>validaCampo(birthInput,validators.birthDate,"Inserire un data passata"));
	}

	const form = document.getElementById("registrazioneForm");

	form.addEventListener("submit", function(event) 
	{
	   
	    let isFormValido = true;
	    
	    
	    if (!validaCampo(emailInput, validators.email, "Inserire una mail valida")) 
		{
	        isFormValido = false;
	    }
	    
	    if (!validaCampo(passwordInput, (val) => val.length >= 8, "Inserire una password di almeno 8 caratteri")) 
		{
	        isFormValido = false;
	    }
	    
	    if (!validaCampo(passwordRepInput, (val) => val === passwordInput.value, "Le due password non corrispondono")) 
		{
	        isFormValido = false;
	    }
	   
	    if (pIvaInput && !validaCampo(pIvaInput, validators.partitaIva, "Inserire una PIVA valida")) 
		{
	        isFormValido = false;
	    }
	    if (cfInput && !validaCampo(cfInput, validators.codiceFiscale, "Inserire un codice fiscale valido")) 
		{
	        isFormValido = false;
	    }
	    if (telInput && !validaCampo(telInput, validators.telefono, "Inserire un numero di telefono valido")) 
		{
	        isFormValido = false;
	    }
	    if (birthInput && !validaCampo(birthInput, validators.isPast, "Inserire una data passata")) 
		{
	        isFormValido = false;
	    }
	   
	    if (!isFormValido) 
		{
	        event.preventDefault(); 
	    }
	
	});
	
	
	
	
	
	
	
	
});