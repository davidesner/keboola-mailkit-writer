/*
 */
package esnerda.keboola.mailkit.writer;

import esnerda.keboola.mailkit.writer.config.JsonConfigParser;
import esnerda.keboola.mailkit.writer.config.KBCConfig;
import esnerda.keboola.mailkit.writer.config.KBCParameters;
import esnerda.keboola.mailkit.writer.mailkitapi.ClientException;
import esnerda.keboola.mailkit.writer.mailkitapi.MailkitJsonAPIClient;
import esnerda.keboola.mailkit.writer.mailkitapi.requests.CreateList;
import esnerda.keboola.mailkit.writer.mailkitapi.requests.MailkitRequest;
import esnerda.keboola.mailkit.writer.mailkitapi.requests.StreamingListImport;
import esnerda.keboola.mailkit.writer.mailkitapi.responses.CreateListResponse;
import esnerda.keboola.mailkit.writer.mailkitapi.responses.MailkitResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public class Writer {

    private final static int REQUEST_WAIT_INTERVAL = 0;
    private final static boolean LOG = true;
    private final static String logName = "log.txt";

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.print("No parameters provided.");
            System.exit(1);
        }

        String dataPath = args[0];
        String outTablesPath = dataPath + File.separator + "out" + File.separator + "tables";
        String inTablesPath = dataPath + File.separator + "in" + File.separator + "tables"; //parse config

        KBCConfig config = null;
        File confFile = new File(args[0] + File.separator + "config.json");
        if (!confFile.exists()) {
            System.out.println("config.json does not exist!");
            System.err.println("config.json does not exist!");
            System.exit(1);
        }
        //Parse config file
        try {
            if (confFile.exists() && !confFile.isDirectory()) {
                config = JsonConfigParser.parseFile(confFile);
            }
        } catch (Exception ex) {
            System.out.println("Failed to parse config file");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        if (!config.validate()) {
            System.out.println(config.getValidationError());
            System.err.println(config.getValidationError());
            System.exit(1);
        }
        //get source file
        if (config.getStorage().getInputTables().getTables().isEmpty()) {

            System.err.println("No input tables found. Have you specified input mapping?");
            System.exit(1);
        }
        String sourceFileName = config.getStorage().getInputTables().getTables().get(0).getDestination();
        File sourceFile = new File(inTablesPath + File.separator + sourceFileName);
        if (!sourceFile.exists()) {

            System.err.println("Source file " + sourceFileName + " does not exist!");
            System.exit(1);
        }

        KBCParameters parameters = config.getParams();
        MailkitJsonAPIClient client = new MailkitJsonAPIClient(config.getParams().getClientId(), config.getParams().getClientMd5(), "/tmp/");

        String listId = "";
        /*Create new list*/
        if (parameters.getNewList() != null) {
            System.out.println("Creating new list \"" + parameters.getNewList().getName() + "\"");
            try {
                MailkitRequest createList = new CreateList(parameters.getNewList().getName(), parameters.getNewList().getDesc());
                MailkitResponse cListResp = client.executeRequest(createList, LOG);
                if (checkResponseStatus(cListResp, createList)) {
                    listId = ((CreateListResponse) cListResp).getListId();
                } else {
                    System.exit(1);
                }
                System.out.println(cListResp.getResultMessage());
            } catch (ClientException ex) {
                System.err.println(ex.getMessage());
                System.exit(1);
            }

            if (listId == null || listId.equals("")) {
                System.exit(1);
            }
        } else {
            listId = config.getParams().getListId();
        }

        /*Execute import list*/
        MailkitRequest req;
        MailkitResponse iListResp;
        System.out.println("\nUpdating user list with ID: " + listId);
        try {
            req = new StreamingListImport(parameters.getClientId(), parameters.getClientMd5(), parameters.getColumnMapping(), listId, sourceFile);
            System.out.println("Execute request");
            iListResp = client.executeRequest(req, LOG);
            System.out.println("Check response status");
            checkResponseStatus(iListResp, req);
            System.out.println(iListResp.getResultMessage());

        } catch (ClientException | IOException | StreamingListImport.InvalidParamsException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        //client.cleanupTempFolder();

        System.out.println("Import finished sucessfuly.");

    }

    private static boolean checkResponseStatus(MailkitResponse res, MailkitRequest rq) {
        if (res.isError()) {
            String err = "";
            if (res.getErrorMessage().equalsIgnoreCase("Unauthorized")) {
                System.err.println("Unauthorized. Check credentials.");
                System.exit(1);
            } else {
                err = "WARNING:" + res.getErrorMessage() + "\n Call failed with parameter: ";
                for (Map.Entry entry : rq.getParameters().entrySet()) {
                    err += "\n" + entry.getKey() + ", " + entry.getValue();
                }
                System.out.println(err);
                return false;
            }
        }
        return true;
    }
}
