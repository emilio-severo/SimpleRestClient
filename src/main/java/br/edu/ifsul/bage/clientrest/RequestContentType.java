package br.edu.ifsul.bage.clientrest;

/**
 *
 * @author emilio
 */
public enum RequestContentType {

    XML("text/xml"),
    TEXT("text/plain"),
    JSON("application/json");

    private String contentType;

    RequestContentType(String contentType) {
        this.contentType = contentType;
    }

    String getType(){
        return contentType;
    }
}

