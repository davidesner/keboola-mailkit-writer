/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import esnerda.keboola.mailkit.writer.mailkitapi.requests.MailkitRequest;
import esnerda.keboola.mailkit.writer.mailkitapi.responses.JsonResponseFactory;
import esnerda.keboola.mailkit.writer.mailkitapi.responses.MailkitResponse;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public class MailkitJsonAPIClient implements MailkitClient {

    private static final String ENDPOINT_URL = "https://api.mailkit.eu/json.fcgi";

    private final static long BACKOFF_INTERVAL = 500;
    private final static int RETRIES = 5;

    private final CloseableHttpClient httpClient;
    private final String persistFolderPath;
    private final String client_id;
    private final String client_md5;
    private File logFile;

    /**
     * Mailkit Json Api client
     * Uses Apache commons HttpClient and persists the result in temporary files
     * stored in filesystem.
     *
     * @param client_id
     * @param client_md5
     * @param persistFolderPath - temporary folder for result data
     */
    public MailkitJsonAPIClient(String client_id, String client_md5, String persistFolderPath) {
        this.client_id = client_id;
        this.client_md5 = client_md5;

        //set default headers
        List<Header> headers = new ArrayList();
        headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        headers.add(new BasicHeader(HttpHeaders.ACCEPT, "application/json"));
        this.httpClient = HttpClients.custom().setDefaultHeaders(headers).disableAutomaticRetries().build();

        this.persistFolderPath = persistFolderPath;
    }

    /**
     *
     * @param req - mailkit json request as instance of
     * MailkitJsonRequest class
     * @param log
     * @return MailkitJsonResponse instance implementing MailkitResponse
     * interface
     * @throws ClientException
     */
    @Override
    public MailkitResponse executeRequest(MailkitRequest req, boolean log) throws ClientException {

        //set credentials header in request
        req.setClient_id(client_id);
        req.setClient_md5(client_md5);
        System.out.println("Executing request");
        //build request
        HttpPost httppost = new HttpPost(ENDPOINT_URL);
        StringEntity stringEntity = null;
        InputStreamEntity inEntity = null;
        try {

            if (req.isStreaming()) {
                inEntity = new InputStreamEntity(req.getInputStream());
                httppost.setEntity(inEntity);
            } else {
                stringEntity = new StringEntity(req.getStringRepresentation());
                httppost.setEntity(stringEntity);
            }
        } catch (Exception ex) {
            throw new ClientException("Error parsing the request. " + ex.getLocalizedMessage());
        }

        String stdout = "";
        String stderr = "";
        try {


            File targetFile = new File("/data/targetFile.tmp");
            OutputStream outStream = new FileOutputStream(targetFile);
            byte[] buffer = new byte[req.getInputStream().available()];
            req.getInputStream().read(buffer);
            outStream.write(buffer);
            IOUtils.closeQuietly(outStream);

            Runtime rt = Runtime.getRuntime();

            ProcessBuilder pb = new ProcessBuilder("/usr/bin/curl", "-v", "-X", "POST", "-H", "Accept: application/json", "-H", "Content-Type: application/json", "-d", "@/data/targetFile.tmp", "https://api.mailkit.eu/json.fcgi");
            Process proc = pb.start();

            stdout = IOUtils.toString(proc.getInputStream());
            stderr = IOUtils.toString(proc.getErrorStream());
            if (stdout.contains("\"error\"")) {
            	throw new IOException(stdout);
            }
            
            //System.out.println(stderr);
            //System.out.println(stdout);
            

        } catch (IOException ex) {
            throw new ClientException("Error sending request to API. " + ex.getLocalizedMessage());
        } catch (Exception ex) {
            Logger.getLogger(MailkitJsonAPIClient.class.getName()).log(Level.SEVERE, "other ex", ex);
        }

        String shortResp = stdout;


        FileOutputStream fos = null;
        String resTmpFilePath = getUniqueTmpFilePath(req.getClass().getSimpleName());
        try {
            fos = new FileOutputStream(resTmpFilePath);
            fos.write(shortResp.getBytes());
            /*
            byte[] buffer = new byte[1024];

            if (entity != null) {
                long len = entity.getContentLength();

                if (len != -1 && len < 2048) {
                    System.out.println("Not in a loop");
                    try {
                        //possibly error response
                        shortResp = EntityUtils.toString(entity);

                    } catch (IOException ex) {
                        throw new ClientException("Unable to read the API response. For "
                                + req.getClass().getSimpleName() + "\n" + ex.getMessage());
                    } catch (ParseException ex) {
                        throw new ClientException("Unable to read the API response.For "
                                + req.getClass().getSimpleName() + "\n" + ex.getMessage());
                    }
                    //write to file as well
                    fos.write(shortResp.getBytes());

                } else {
                    System.out.println("Before while loop");
                    InputStream is = entity.getContent();
                    int inByte;
                    while ((inByte = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, inByte);
                    }
                }

            }
            */
        } catch (FileNotFoundException ex) {
            throw new ClientException("Unable to write response to filesystem. For "
                    + req.getClass().getSimpleName() + "\n" + ex.getMessage());
        } catch (IOException ex) {
            throw new ClientException("Unable to write response to filesystem. For "
                    + req.getClass().getSimpleName() + "\n" + ex.getMessage());
        } finally {
            try {
                //response.close();
                fos.close();
            } catch (Exception ex) {

            }

        }
        System.out.println("After entity process");
        if (log) {
            System.out.println("Log on");
            FileInputStream fis = null;
            BufferedWriter out = null;
            try {
                setLogFile("log.txt");
                File fin = new File(resTmpFilePath);
                fis = new FileInputStream(fin);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));
                FileWriter fs = new FileWriter(logFile, true);
                out = new BufferedWriter(fs);
                //write request
                out.write("Request:");
                out.newLine();
                out.write(req.getStringRepresentation());
                //write response
                out.newLine();
                out.write("Response:");
                out.newLine();
                String aLine = null;
                System.out.println("Before loop");
                while ((aLine = in.readLine()) != null) {
                    //Process each line and add output to Dest.txt file
                    out.write(aLine);
                    out.newLine();
                }
                System.out.println("After loop");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MailkitJsonAPIClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MailkitJsonAPIClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(MailkitJsonAPIClient.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fis.close();
                    out.close();
                } catch (Exception ex) {
                }
            }
        }
        System.out.println("Before JRF getResponse");

        return JsonResponseFactory.getResponse(resTmpFilePath, shortResp, req.getFunction(), req.getClass());
    }

    public void setLogFile(String path) {
        try {
            this.logFile = new File(path);
            logFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(MailkitJsonAPIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Deletes all files from temporary folder
     */
    public void cleanupTempFolder() {
        try {
            FileUtils.cleanDirectory(new File(this.persistFolderPath));
        } catch (IOException ex) {
        }
    }

    /**
     * Returns unique file name that doesnt already exist in temp direcotry
     *
     * @param name - prefix of the temporary file
     * @return
     */
    private String getUniqueTmpFilePath(String name) {
        String path = this.persistFolderPath + File.separator + name + UUID.randomUUID() + ".tmp";
        File f = new File(path);
        if (f.exists() && !f.isDirectory()) {
            return getUniqueTmpFilePath(name);
        } else {
            f.getParentFile().mkdirs();
            return path;
        }
    }

}
