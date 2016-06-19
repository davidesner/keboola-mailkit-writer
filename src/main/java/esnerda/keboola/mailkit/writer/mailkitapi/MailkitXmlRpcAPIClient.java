/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi;

import esnerda.keboola.mailkit.writer.mailkitapi.responses.MailkitResponse;
import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import esnerda.keboola.mailkit.writer.mailkitapi.requests.MailkitRequest;
import esnerda.keboola.mailkit.writer.mailkitapi.requests.MailkitXmlRpcRequest;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.common.TypeFactoryImpl;
import org.apache.xmlrpc.common.XmlRpcController;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.apache.xmlrpc.serializer.TypeSerializer;
import org.apache.xmlrpc.serializer.TypeSerializerImpl;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public class MailkitXmlRpcAPIClient implements MailkitClient {

    private static final String ENDPOINT_URL = "https://api.mailkit.eu/rpc.fcgi";

    private final XmlRpcClient xmlRpcClient;
    private final String client_id;
    private final String client_md5;

    /**
     * Mailkit Xml-Rpc api client
     *
     * @param client_id
     * @param client_md5
     * @throws MalformedURLException
     */
    public MailkitXmlRpcAPIClient(String client_id, String client_md5) throws MalformedURLException {
        this.client_id = client_id;
        this.client_md5 = client_md5;

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(ENDPOINT_URL));

        xmlRpcClient = new XmlRpcClient();
        xmlRpcClient.setConfig(config);
//        xmlRpcClient.setTypeFactory(new MyTypeFactory(xmlRpcClient));
//        final XmlRpcTransportFactory transportFactory = new XmlRpcTransportFactory() {
//            public XmlRpcTransport getTransport() {
//                return new MessageLoggingTransport(xmlRpcClient);
//            }
//        };
//        xmlRpcClient.setTransportFactory(transportFactory);

    }

    /**
     *
     * @param req
     * @return returns MailkitResponse object
     * @throws ClientException
     */
    public MailkitResponse executeRequest(MailkitRequest req, boolean log) throws ClientException {

        //build request
        List<Object> params = new ArrayList();
        //set credentials
        params.add(client_id);
        params.add(client_md5);
        //add custom parameters
        params.addAll(req.getParameters().values());
        Object result;
        MailkitResponse xmlResp = null;
        try {
            result = xmlRpcClient.execute(req.getFunction(), params);
            xmlResp = XmlRpcResponseFactory.getResponse(result, req.getClass());

        } catch (XmlRpcException | ClientException ex) {
            throw new ClientException("Error sending request to API. " + ex.getLocalizedMessage());
        } catch (RuntimeException ex) {
            throw new ClientException("Runtime error. " + ex.getLocalizedMessage());
        }

        return xmlResp;

    }

    public class MyTypeFactory extends TypeFactoryImpl {

        public MyTypeFactory(XmlRpcController pController) {
            super(pController);
        }

        @Override
        public TypeSerializer getSerializer(XmlRpcStreamConfig pConfig, Object pObject) throws SAXException {
            if (pObject instanceof String) {
                return new CustomStringSerializer();
            } else if (pObject instanceof Integer) {
                return new TypeSerializerImpl() {
                    @Override
                    public void write(ContentHandler pHandler, Object pObject) throws SAXException {
                        write(pHandler, "int", pObject.toString());
                    }
                };
            } else {
                return super.getSerializer(pConfig, pObject);
            }
        }
    }

    public class CustomStringSerializer extends TypeSerializerImpl {

        public static final String STRING_TAG = "string";

        @Override
        public void write(ContentHandler pHandler, Object pObject) throws SAXException {
            write(pHandler, "string", pObject.toString());
        }
    }

}
