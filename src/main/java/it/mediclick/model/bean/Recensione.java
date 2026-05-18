package it.mediclick.model.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Recensione implements Serializable
{

	private static final long serialVersionUID = 1L;

	private int id = -1;
	private int voto; // 1-5
	private String commento;
	private boolean isVisible;
	private LocalDateTime dataPubblicazione;

	private int prenotazioneId = -1;
	private Prenotazione prenotazione;

	public int getPrenotazioneId()
	{
		return prenotazioneId;
	}

	public void setPrenotazioneId(int prenotazioneId)
	{
		this.prenotazioneId = prenotazioneId;
	}

	public Recensione()
	{
		this.isVisible = true;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getVoto()
	{
		return voto;
	}

	public void setVoto(int voto)
	{
		if (voto < 1 || voto > 5)
			throw new IllegalArgumentException("Voto deve essere tra 1 e 5");
		this.voto = voto;
	}

	public String getCommento()
	{
		return commento;
	}

	public void setCommento(String commento)
	{
		this.commento = commento;
	}

	public boolean isVisible()
	{
		return isVisible;
	}

	public void setVisible(boolean visible)
	{
		isVisible = visible;
	}

	public LocalDateTime getDataPubblicazione()
	{
		return dataPubblicazione;
	}

	public void setDataPubblicazione(LocalDateTime dataPubblicazione)
	{
		this.dataPubblicazione = dataPubblicazione;
	}

	public Prenotazione getPrenotazione()
	{
		return prenotazione;
	}

	public void setPrenotazione(Prenotazione prenotazione)
	{
		this.prenotazione = prenotazione;
	}

	@Override
	public String toString()
	{
		return "Recensione{id=" + id + ", prenotazioneId=" + getPrenotazioneId() + ", voto=" + voto + ", visible=" + isVisible + "}";
	}
}
