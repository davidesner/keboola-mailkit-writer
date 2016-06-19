/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi;

import esnerda.keboola.mailkit.writer.mailkitapi.responses.MailkitResponse;
import esnerda.keboola.mailkit.writer.mailkitapi.requests.CampaignListXmlRpc;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public class XmlRpcResponseFactory {

    /**
     * Factory method returning correct implementation of MailkitResponse
     * according to mailkit request type
     *
     * @param result
     * @param type
     * @return
     * @throws ClientException
     */
    public static MailkitResponse getResponse(Object result, Class type) throws ClientException {
        if (type.equals(CampaignListXmlRpc.class)) {
            return new XmlRpcCampaignListResponse(result);
        } else {
            throw new ClientException("Unsupported request type");

        }
    }
}
