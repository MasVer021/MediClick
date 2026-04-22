package it.mediclick.util;

public class TestOperazioniDB {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/MediClick";
        String user = "root";
        String password = "";

        Contex db = new Contex(url, user, password);

        try {
            var risultati = db.eseguiSelect("SELECT * FROM Utente");

            if (risultati.isEmpty()) 
            {
                System.out.println("Nessun utente trovato (tabella vuota)");
            } 
            else 
            {
                for (var riga : risultati) 
                {
                    System.out.println(riga);
                }
            }

            System.out.println("Connessione OK!");

        } 
        catch (Exception e) 
        {
            System.out.println("Errore: " + e.getMessage());
        }
    }
}
