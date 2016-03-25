package br.edu.ifsul.bage.clientrest;

import java.util.List;

/**
 * Interface RestClientInterface a qual define contantes utilizadas para definição
 * do conteúdo gerado por um recurso REST. Bem como, métodos abstratos que serão
 * implementados pela classe RestClient a qual irá fornecer acesso aos recursos.
 * 
 * @author      Carlos Emilio Padilla Severo.
 * @since       03/2016.
 * @version     1.0
 * @param <T>   A interface é genérica.
 */
public interface RestClientInterface<T> {
    
    public static final int POST = 0;
    public static final int GET = 1;
    public static final int PUT = 2;
    public static final int DELETE = 3;    
    
    /**
     *  Constante utilizada para definir o conteúdo gerado como JSON.
     */
    public static final String TYPE_JSON = "application/json";

    /**
     *  Constante utilizada para definir o conteúdo gerado como XML.
     */
    public static final String TYPE_XML = "text/xml";

    /**
     *  Constante utilizada para definir requisição do tipo texto.
     */
    public static final String TYPE_TEXT = "text/plain";

   
    /**
     * Método abstrato que define o tipo de retorno gerado pelo recurso GET.
     * @return      A lista de objetos gerados pelo recurso GET no servidor.
     */
     List<T> get();
    
    /**
     * Método abstrato que define o parâmetro enviado ao recurso POST.
     * @param object    Um objeto que será inserido no servidor.
     */
    void post(T object);
    
    /**
     * Método abstrato que define o parâmetro enviado ao recurso PUT.
     * @param object    Um objeto que será atualializado no servidor.
     */
    void put(T object);
    
    /**
     * Método abstrato que define o parâmetro enviado ao recurso DELETE.
     */
    void delete();
    
    /**
     * Método abstrato que define o parâmetro passado para setUrl().
     * @param url   Uma string que define a URL de um determinado recurso.
     */
    void setUrl(String url);
    
    /**
     * Método abstrato que define o parâmetro indicativo do tipo de conteúdo gerado.
     * @param contentType   Uma string que define o tipo de conteúdo gerado pelo recurso.
     */
    void setRequestContentType(String contentType);
    
    /**
     * Método abstrato que define o fechamento do cliente de Webservice.
     */
    void close();
}
