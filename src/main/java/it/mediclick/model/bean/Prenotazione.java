package it.mediclick.model.bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Prenotazione implements Serializable
{

	private static final long serialVersionUID = 1L;

	public enum Stato {
		CONFERMATA("Confermata"), COMPLETATA("Completata"), CANCELLATA("Cancellata"), RIMBORSATA("Rimborsata");

		public static Stato fromString(String statoStr)
		{
			if (statoStr == null)
				return null;

			for (Prenotazione.Stato s : Prenotazione.Stato.values())
			{
				if (s.getLabel().equalsIgnoreCase(statoStr) || s.name().equalsIgnoreCase(statoStr))
				{
					return s;
				}
			}

			return null;
		}

		private final String label;

		Stato(String label)
		{
			this.label = label;
		}

		public String getLabel()
		{
			return label;
		}
	}

	private int id = -1;
	private Stato stato;
	private String metodoPagamento;
	private String idTransazioneEsterno;
	private double importoPagato;
	private double ricavoNettoMedicoEuro;
	private double trattenutaPiattaformaEuro;
	private double tasseStimateEuro;
	private LocalDateTime dataPagamento;

	private int pazienteId;
	private Paziente paziente;

	private int disponibilitaId = -1;
	private Disponibilita disponibilita;

	private int erogazionePrestazioneId = -1;
	private ErogazionePrestazione erogazionePrestazione;

	private int codiceScontoId = -1;
	private CodiceSconto codiceSconto;

	public int getPazienteId()
	{
		return pazienteId;
	}

	public void setPazienteId(int pazienteId)
	{
		this.pazienteId = pazienteId;
	}

	public int getDisponibilitaId()
	{
		return disponibilitaId;
	}

	public void setDisponibilitaId(int disponibilitaId)
	{
		this.disponibilitaId = disponibilitaId;
	}

	public int getErogazionePrestazioneId()
	{
		return erogazionePrestazioneId;
	}

	public void setErogazionePrestazioneId(int erogazionePrestazioneId)
	{
		this.erogazionePrestazioneId = erogazionePrestazioneId;
	}

	public int getCodiceScontoId()
	{
		return codiceScontoId;
	}

	public void setCodiceScontoId(int codiceScontoId)
	{
		this.codiceScontoId = codiceScontoId;
	}

	public Prenotazione()
	{
		this.stato = Stato.CONFERMATA;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Stato getStato()
	{
		return stato;
	}

	public void setStato(Stato stato)
	{
		this.stato = stato;
	}

	public String getMetodoPagamento()
	{
		return metodoPagamento;
	}

	public void setMetodoPagamento(String metodoPagamento)
	{
		this.metodoPagamento = metodoPagamento;
	}

	public String getIdTransazioneEsterno()
	{
		return idTransazioneEsterno;
	}

	public void setIdTransazioneEsterno(String idTransazioneEsterno)
	{
		this.idTransazioneEsterno = idTransazioneEsterno;
	}

	public double getImportoPagato()
	{
		return importoPagato;
	}

	public void setImportoPagato(double importoPagato)
	{
		this.importoPagato = importoPagato;
	}

	public double getRicavoNettoMedicoEuro()
	{
		return ricavoNettoMedicoEuro;
	}

	public void setRicavoNettoMedicoEuro(double ricavoNettoMedicoEuro)
	{
		this.ricavoNettoMedicoEuro = ricavoNettoMedicoEuro;
	}

	public double getTrattenutaPiattaformaEuro()
	{
		return trattenutaPiattaformaEuro;
	}

	public void setTrattenutaPiattaformaEuro(double trattenutaPiattaformaEuro)
	{
		this.trattenutaPiattaformaEuro = trattenutaPiattaformaEuro;
	}

	public double getTasseStimateEuro()
	{
		return tasseStimateEuro;
	}

	public void setTasseStimateEuro(double tasseStimateEuro)
	{
		this.tasseStimateEuro = tasseStimateEuro;
	}

	public LocalDateTime getDataPagamento()
	{
		return dataPagamento;
	}

	public void setDataPagamento(LocalDateTime dataPagamento)
	{
		this.dataPagamento = dataPagamento;
	}

	public Paziente getPaziente()
	{
		return paziente;
	}

	public void setPaziente(Paziente paziente)
	{
		this.paziente = paziente;
	}

	public Disponibilita getDisponibilita()
	{
		return disponibilita;
	}

	public void setDisponibilita(Disponibilita disponibilita)
	{
		this.disponibilita = disponibilita;
	}

	public ErogazionePrestazione getErogazionePrestazione()
	{
		return erogazionePrestazione;
	}

	public void setErogazionePrestazione(ErogazionePrestazione erogazionePrestazione)
	{
		this.erogazionePrestazione = erogazionePrestazione;
	}

	public CodiceSconto getCodiceSconto()
	{
		return codiceSconto;
	}

	public void setCodiceSconto(CodiceSconto codiceSconto)
	{
		this.codiceSconto = codiceSconto;
	}

	public boolean isFutura()
	{
		if (this.disponibilita == null || this.disponibilita.getDataOraInizio() == null)
		{
			return false;
		}
		return this.disponibilita.getDataOraInizio().isAfter(java.time.LocalDateTime.now());
	}

	public String dataPagamentoFormattata(String patter)
	{
		if (this.dataPagamento == null)
			return "";
		else
			return this.dataPagamento.format(DateTimeFormatter.ofPattern(patter));
	}

	@Override
	public String toString()
	{
		return "Prenotazione{id=" + id + ", pazienteId=" + getPazienteId() + ", importo=" + importoPagato + ", stato=" + stato + "}";
	}
}
