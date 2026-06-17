const cssErrorClass= "mc-input--error";
const cssErrorText = "mc-input-error-text";

const validators = 
{
	email: function(value) 
	{
       const re = /^\S+@\S+\.\S+$/g;
       return re.test(String(value).toLowerCase());
   },
   
   partitaIva: function(value) 
   {
       const re = /^[0-9]{11}$/;
       return re.test(value);
   },
   
   codiceFiscale: function(value) 
   {
       const re = /^[A-Z]{6}[0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{3}[A-Z]$/i;
       return re.test(value);
   },
   
   telefono: function(value) 
   {
       const re = /^\+?[0-9]{9,15}$/;
       return re.test(value);
   },
   
   isFuture: function(value) 
   {
       	if (!value)
		{
			return true;
	   	} 
       const inputDate = new Date(value);
       const today = new Date();
       today.setHours(0, 0, 0, 0);
       return inputDate >= today;
   },
   
   isInRange: function(value,min,max) 
      {
         if (value<min || value > max)
   		{
   			return false;
   	   	} 
		
		return true
          
      },
   
   isPast: function(value) 
   {
       if (!value)
		{
			return true;	
		} 
       const inputDate = new Date(value);
       const today = new Date();
       return inputDate <= today;
   },	
   
   isStep30: function(time1,time2) 
	  {
		if (!time1 || !time2)
		{
			return false;	
		} 
		
	    let h1 ,h2 ,m1 ,m2;
		
		h1 = parseInt(time1.split(":")[0],10);
		h2 = parseInt(time2.split(":")[0],10);
		m1 = parseInt(time1.split(":")[1],10);
		m2 = parseInt(time2.split(":")[1],10);
		
		let totalMin1 = h1*60+m1;
		let totalMin2 = h2*60+m2;
		
		return Math.abs(totalMin1-totalMin2) %30 ==0;
	  }
};

const getContextPath = () => 
	{
		const path = window.location.pathname;
		const index = path.indexOf("/", 1);
		return index !== -1 ? path.substring(0, index) : "";
	};

function showError(inputElement,message)
{
	clearError(inputElement);
	inputElement.classList.add(cssErrorClass);
	
	const errorText = document.createElement("span");
	errorText.className = cssErrorText;
	errorText.textContent = message;
	
	const parentgroup = inputElement.parentNode;
	
	parentgroup.appendChild(errorText);
}

function clearError(inputElement)
{
	inputElement.classList.remove(cssErrorClass);
   	const parentgroup = inputElement.parentNode;
    const errorText = parentgroup.querySelector("."+ cssErrorText);
    if (errorText) 
	{
        errorText.remove();
    }	
}

function validaCampo(inputElement, validatorFunc, errorMessage) 
{
    const valore = inputElement.value.trim();
     
    if (validatorFunc(valore)) 
	{
        clearError(inputElement);
        return true;
    } 
	else 
	{
        showError(inputElement, errorMessage);
        return false;
    }
}

