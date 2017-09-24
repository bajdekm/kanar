package ldc.accenture.kanarapp.model;

import lombok.Data;

@Data
public class NotificationEvent {

    private String Comment__c;
    private String Latitude__c;
    private String Longitude__c;
    private String 	SpottedAt__c;
    private String 	TramNr__c;

    public String getComment__c() {
        return Comment__c;
    }

    public void setComment__c(String comment__c) {
        Comment__c = comment__c;
    }

    public String getLatitude__c() {
        return Latitude__c;
    }

    public void setLatitude__c(String latitude__c) {
        Latitude__c = latitude__c;
    }

    public String getLongitude__c() {
        return Longitude__c;
    }

    public void setLongitude__c(String longitude__c) {
        Longitude__c = longitude__c;
    }

    public String getSpottedAt__c() {
        return SpottedAt__c;
    }

    public void setSpottedAt__c(String spottedAt__c) {
        SpottedAt__c = spottedAt__c;
    }

    public String getTramNr__c() {
        return TramNr__c;
    }

    public void setTramNr__c(String tramNr__c) {
        TramNr__c = tramNr__c;
    }
}
