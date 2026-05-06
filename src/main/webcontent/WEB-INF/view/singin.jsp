<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Mediclick Registrazione</title>
</head>
<body>
	<form method="post" action='<%=response.encodeUrl(request.getContextPath() +"/singin")%>'>
    	<input type="email" name="email" placeholder="Email" required>
        <input type="password" name="password" placeholder="Password" required>
        <input type="password" name="passwordRipetuta" placeholder="Ripeti Password" required>
        <input type="text" name="nome" placeholder="Nome" required>
        <input type="text" name="cognome" placeholder="Cognome" required>
        <label for="isMedico">Sei un professionista ?</label>
        <input type="checkbox" name="isMedico" id="isMedico">
        <hr>
        <p>Paziente</p>
        <input type="text" name="CF" placeholder="Inserisci Codice Fiscale">
        <input type="tel" name="telefono" placeholder="Numero di telefono">
        <input type="date" name="DataNascita" placeholder="DataNascita">
        <hr>
        <p>Medico</p>
        <textarea id="Bio" name="Bio" rows="4" cols="50" placeholder="Biografia"></textarea>
        <input type="text" name="PIva" placeholder="Partita Iva">
       <input list="RegimeFiscale-list" id="RegimeFiscale" name="RegimeFiscale" placeholder="Regime fiscale">
			<datalist id="RegimeFiscale-list">
			  <option  value=1 label="Regime Forfettario (primi 5 anni)">
			  <option  value=2 label="Regime Forfettario">
			  <option  value=3 label="Regime Semplificato">
			  <option  value=4 label="Regime Ordinario">
			</datalist>
        <br>
        <br>
        <button type="submit">Registrati</button>
    </form>
 	${errore}
</body>
</html>