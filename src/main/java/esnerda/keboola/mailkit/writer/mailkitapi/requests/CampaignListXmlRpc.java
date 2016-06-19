/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi.requests;

import java.io.InputStream;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public class CampaignListXmlRpc extends MailkitXmlRpcRequest {

    public CampaignListXmlRpc(String campaignId) {
        super("mailkit.campaigns.list");
        if (campaignId != null) {
            this.addParameter("ID_message", campaignId);
        }

    }

    @Override
    public String getFunctionCall() {
        String m = this.getFunction() + " with parameters:";
        for (Object par : this.getParameters().values()) {
            m += "\n" + par + ", " + par;
        }
        return m;
    }

    @Override
    public InputStream getInputStream() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getContentLength() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
