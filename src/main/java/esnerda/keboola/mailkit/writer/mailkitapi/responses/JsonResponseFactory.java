/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi.responses;

import esnerda.keboola.mailkit.writer.mailkitapi.ClientException;
import esnerda.keboola.mailkit.writer.mailkitapi.XmlRpcCampaignListResponse;
import esnerda.keboola.mailkit.writer.mailkitapi.responses.MailkitResponse;
import esnerda.keboola.mailkit.writer.mailkitapi.requests.CampaignListXmlRpc;
import esnerda.keboola.mailkit.writer.mailkitapi.requests.CreateList;
import esnerda.keboola.mailkit.writer.mailkitapi.requests.StreamingListImport;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public class JsonResponseFactory {

    /**
     * Factory method returning correct implementation of MailkitResponse
     * according to mailkit request type
     *
     * @param filePath
     * @param shortResponse
     * @param requestType
     * @param type
     * @return
     * @throws ClientException
     */
    public static MailkitResponse getResponse(String filePath, String shortResponse, String requestType, Class type) throws ClientException {
        if (type.equals(StreamingListImport.class)) {
            return new ImportListResponse(filePath, shortResponse, requestType);
        } else if (type.equals(CreateList.class)) {
            return new CreateListResponse(filePath, shortResponse, requestType);
        } else {
            throw new ClientException("Unsupported request type");

        }
    }
}
