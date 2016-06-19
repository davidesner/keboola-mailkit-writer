/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi;

import esnerda.keboola.mailkit.writer.mailkitapi.responses.MailkitResponse;
import esnerda.keboola.mailkit.writer.mailkitapi.requests.MailkitRequest;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public interface MailkitClient {

    public MailkitResponse executeRequest(MailkitRequest rq, boolean log) throws ClientException;
}
