package it.mediclick.model.DTO;

import java.time.LocalDateTime;

public class SlotAgendaDTO
{
	private int disponibilitaId;
	private LocalDateTime dataOraInizio;
	private LocalDateTime dataOraFine;
	private String statoSlot;

	private Integer prenotazioneId;
	private String nomePaziente;
	private String cognomePaziente;
	private String telefonoPaziente;
	private String nomePrestazione;
	private String statoPrenotazione;

	public int getDisponibilitaId()
	{
		return disponibilitaId;
	}

	public void setDisponibilitaId(int disponibilitaId)
	{
		this.disponibilitaId = disponibilitaId;
	}

	public LocalDateTime getDataOraInizio()
	{
		return dataOraInizio;
	}

	public void setDataOraInizio(LocalDateTime dataOraInizio)
	{
		this.dataOraInizio = dataOraInizio;
	}

	public LocalDateTime getDataOraFine()
	{
		return dataOraFine;
	}

	public void setDataOraFine(LocalDateTime dataOraFine)
	{
		this.dataOraFine = dataOraFine;
	}

	public String getStatoSlot()
	{
		return statoSlot;
	}

	public void setStatoSlot(String statoSlot)
	{
		this.statoSlot = statoSlot;
	}

	public Integer getPrenotazioneId()
	{
		return prenotazioneId;
	}

	public void setPrenotazioneId(Integer prenotazioneId)
	{
		this.prenotazioneId = prenotazioneId;
	}

	public String getNomePaziente()
	{
		return nomePaziente;
	}

	public void setNomePaziente(String nomePaziente)
	{
		this.nomePaziente = nomePaziente;
	}

	public String getCognomePaziente()
	{
		return cognomePaziente;
	}

	public void setCognomePaziente(String cognomePaziente)
	{
		this.cognomePaziente = cognomePaziente;
	}

	public String getTelefonoPaziente()
	{
		return telefonoPaziente;
	}

	public void setTelefonoPaziente(String telefonoPaziente)
	{
		this.telefonoPaziente = telefonoPaziente;
	}

	public String getNomePrestazione()
	{
		return nomePrestazione;
	}

	public void setNomePrestazione(String nomePrestazione)
	{
		this.nomePrestazione = nomePrestazione;
	}

	public String getStatoPrenotazione()
	{
		return statoPrenotazione;
	}

	public void setStatoPrenotazione(String statoPrenotazione)
	{
		this.statoPrenotazione = statoPrenotazione;
	}
}