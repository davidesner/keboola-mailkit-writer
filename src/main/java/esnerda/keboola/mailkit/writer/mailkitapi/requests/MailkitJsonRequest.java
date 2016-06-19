/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class implementing MailkitRequest interface. Provides default
 * functions and contracts form MailkitJsonRequest objects
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public abstract class MailkitJsonRequest implements MailkitRequest {

    private final String function;

    @JsonProperty("id")
    private String client_id;
    @JsonProperty("md5")
    private String client_md5;
    private final Map<String, Object> parameters;

    public MailkitJsonRequest(String function) {
        this.function = function;

        this.parameters = new HashMap();
    }

    public final void addParameter(String key, String value) {

        this.parameters.put(key, value);
    }

    /**
     *
     * @return - json representation of this object
     * @throws Exception
     */
    @Override
    @JsonIgnore
    public String getStringRepresentation() throws Exception {
        final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        return mapper.writeValueAsString(this);
    }

    @Override
    public String getFunction() {
        return function;
    }

    @Override
    public String getClient_id() {
        return client_id;
    }

    @Override
    public String getClient_md5() {
        return client_md5;
    }

    @Override
    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    @Override
    public void setClient_md5(String client_md5) {
        this.client_md5 = client_md5;
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    @JsonIgnore
    public abstract InputStream getInputStream() throws FileNotFoundException;

    @Override
    @JsonIgnore
    public final String getFunctionCall() {
        String m = this.getFunction() + " with parameters:";
        for (Map.Entry entry : this.getParameters().entrySet()) {
            m += "\n" + entry.getKey() + ", " + entry.getValue();
        }
        return m;
    }

    @Override
    @JsonIgnore
    public boolean isStreaming() {
        return false;
    }

    @Override
    @JsonIgnore
    public long getContentLength() {
        return 0;
    }

}
