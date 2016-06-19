/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi.requests;

import java.io.InputStream;
import java.util.Map;

/**
 * Contract for MailkitRequest objects
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public interface MailkitRequest {

    public String getStringRepresentation() throws Exception;

    public String getClient_id();

    public String getClient_md5();

    public void setClient_id(String client_id);

    public void setClient_md5(String client_md5);

    public String getFunctionCall();

    public String getFunction();

    public Map<String, Object> getParameters();

    public boolean isStreaming();

    public InputStream getInputStream() throws Exception;

    public long getContentLength();
}
