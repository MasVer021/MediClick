package it.mediclick.model.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class Studio implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String placeId;
    private String nomeSede;
    private String indirizzoMaps;
    private String citta;
    private BigDecimal lat;
    private BigDecimal lng;

    public Studio() {}

    public Studio(int id, String placeId, String nomeSede, String indirizzoMaps,
                  String citta, BigDecimal lat, BigDecimal lng) {
        this.id = id;
        this.placeId = placeId;
        this.nomeSede = nomeSede;
        this.indirizzoMaps = indirizzoMaps;
        this.citta = citta;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPlaceId() { return placeId; }
    public void setPlaceId(String placeId) { this.placeId = placeId; }

    public String getNomeSede() { return nomeSede; }
    public void setNomeSede(String nomeSede) { this.nomeSede = nomeSede; }

    public String getIndirizzoMaps() { return indirizzoMaps; }
    public void setIndirizzoMaps(String indirizzoMaps) { this.indirizzoMaps = indirizzoMaps; }

    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }

    public BigDecimal getLat() { return lat; }
    public void setLat(BigDecimal lat) { this.lat = lat; }

    public BigDecimal getLng() { return lng; }
    public void setLng(BigDecimal lng) { this.lng = lng; }

    @Override
    public String toString() {
        return "Studio{id=" + id + ", nomeSede='" + nomeSede + "', citta='" + citta + "'}";
    }
}
