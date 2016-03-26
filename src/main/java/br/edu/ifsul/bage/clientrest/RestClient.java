package br.edu.ifsul.bage.clientrest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A classe RestClient é utilizada para implementar os comportamentos definidos
 * na interface RestClientInterface. Dessa forma, provendo mecanismos para
 * acesso aos recursos GET, POST, PUT e DELETE de um servidor Webservice REST.
 *
 * @author Carlos Emilio Padilla Severo.
 * @since 03/2016.
 * @version 1.0
 * @param <T> A classe é genérica.
 */
public class RestClient<T> implements RestClientInterface<T> {

    private URL url;
    private HttpURLConnection con;
    private RequestContentType contentType;
    private List<T> objects;
    private BufferedReader request;
    private StringBuilder data;
    private OutputStream output;
    private String line;
    private Gson gson;
    private Class<T[]> tipo;

    /**
     * O construtor da classe recebe como parâmetro uma string que define a URL
     * do recurso acessado e outra string que identifica o tipo de conteúdo
     * gerado. O conteúdo pode ser no formato JSON ou XML.
     *
     * @param address       String que define a URL do recurso.
     * @param contentType   String que define o tipo do recurso.
     */    
    public RestClient(String address, RequestContentType contentType) {
        gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        try {
            this.url = new URL(address);
            this.contentType = contentType;
        } catch (MalformedURLException ex) {
            Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    /**
     * O construtor da classe recebe como parâmetro uma string que define a URL
     * do recurso acessado, uma string que identifica o tipo de conteúdo e uma
     * classe que identifica o tipo dos objetos manipulados pelo Webservice.
     *  O conteúdo pode ser no formato JSON ou XML.
     *
     * @param address       String que define a URL do recurso.
     * @param contentType   String que define o tipo do recurso.
     * @param tipo          Classe dos objetos manipulados pelo cliente de Webservice.
     */
    public RestClient(String address, RequestContentType contentType, Class<T[]> tipo) {
        this(address, contentType);
        this.tipo =  tipo;
    }
    
    /**
     * O método setUrl(String url) é utilizado para alteração da URL de um
     * recurso. Após, a criação de uma instância de RestCliet.
     *
     * @param url String que define a URL do recurso.
     */
    @Override
    public void setUrl(String url) {
        gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        try {
            this.url = new URL(url);
        } catch (MalformedURLException ex) {
            Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * O Método setRequestContentType(String contentType) é utilizado para
     * redefinição do tipo de conteúdo gerado por um recurso. O tipo de conteúdo
     * gerado pode ser: RestClientInterface.TYPE_XML - para conteúdo no formato
     * XML. RestClientInterface.TYPE_JSON - para conteúdo no formato JSON.
     *
     * @param contentType String informando o tipo de conteúdo.
     */
    @Override
    public void setRequestContentType(RequestContentType contentType) {
        con.setRequestProperty("Content-Type", contentType.getType());
    }

    /**
     * O método post(T object) é utilizado para o envio de um novo recurso ao
     * servidor. Para isso, recebe como argumento o objeto a ser enviado.
     *
     * @param object Objeto a ser inserido no servidor.
     */
    @Override
    public void post(T object) {
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", contentType.getType());
            con.setRequestMethod(HttpMethod.POST.name());
            sendRequest(object);
        } catch (MalformedURLException ex) {
            Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
    }

    /**
     * O método put(T object) é utilizado para o envio de recurso que será
     * atualizado no servidor. Para isso, recebe como argumento o objeto a ser
     * enviado.
     *
     * @param object Objeto a ser alterado no servidor.
     */
    @Override
    public void put(T object) {
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", contentType.getType());
            con.setRequestMethod(HttpMethod.PUT.name());
            sendRequest(object);
        } catch (MalformedURLException ex) {
            Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
    }

    /**
     * O método delete(T object) é utilizado para o envio de uma requisição de
     * exclusão de um recurso do servidor. Dessa forma, recebe como argumento o
     * objeto que representa o recurso a ser removido.
     *
     */
    @Override
    public void delete() {
        try {
            con = (HttpURLConnection) url.openConnection();
            //con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestMethod(HttpMethod.DELETE.name());
            con.setDoOutput(true);            
            con.setConnectTimeout(60000);
            con.connect();
            Logger.getLogger(RestClient.class.getName()).log(Level.INFO, "\nResponse HTTP code: {0}", String.valueOf(con.getResponseCode()));
        } catch (MalformedURLException ex) {
            Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
    }
        
    private void sendRequest(T object) throws IOException {        
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setConnectTimeout(60000);
        con.setReadTimeout(60000);     
        output = con.getOutputStream();
        output.write(gson.toJson(object).getBytes());
        output.flush();
        request = new BufferedReader(new InputStreamReader((con.getInputStream())));
        while ((line = request.readLine()) != null) {
            System.out.println(output);
        }
        Logger.getLogger(RestClient.class.getName()).log(Level.INFO, "\nResponse HTTP code: {0}", String.valueOf(con.getResponseCode()));
        request.close();
        output.close();
    }

    /**
     * O método retorna uma lista de objetos do servidor.
     *
     * @return A lista de objetos retornada.
     */
    @Override
    public List<T> get() {
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", contentType.getType());
            con.setRequestMethod(HttpMethod.GET.name());            

            //Define o método de uma requisição HTTP. Neste caso, o método será um GET.
            request = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            data = new StringBuilder();

            while ((line = request.readLine()) != null) {
                data.append(line);
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
        objects = getList(tipo, data.toString());                
        return objects;
    }    
    
    /**
     * O método close() é utilizado para fechar o cliente de Webservice.
     */
    @Override
    public void close() {
        url = null;
        //gson = null;
        try {
            con.disconnect();
        } catch (Throwable ex) {
            Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Transforma a String contendo o mapa de objetos no formato JSON em uma List<T>.
    private <T> List<T> getList(final Class<T[]> clazz, final String json) {
        final T[] jsonToObject = gson.fromJson(json, clazz);
        return Arrays.asList(jsonToObject);
    }

}
