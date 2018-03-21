/*
 */
package esnerda.keboola.mailkit.writer.mailkitapi.requests;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang3.EnumUtils;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2016
 */
public class StreamingListImport extends MailkitJsonRequest {

    private final String rqFilePath;
    private final Map<String, ListImportColumns> columnMapping;
    private final long contentLength;

    public StreamingListImport(String client_id, String client_md5, Map<String, ListImportColumns> columnMapping, String ID_user_list, File recipientsCsv) throws InvalidParamsException, IOException {

        super("mailkit.mailinglist.import");

        addParameter("ID_user_list", ID_user_list);
        this.columnMapping = new CaseInsensitiveMap();
        if (columnMapping != null) {
            this.columnMapping.putAll(columnMapping);
        }
        this.setClient_id(client_id);
        this.setClient_md5(client_md5);
        System.out.println("Building");

        this.rqFilePath = buildJsonFile(recipientsCsv, ID_user_list);
        if (this.rqFilePath != null) {
            this.contentLength = new File(this.rqFilePath).length();
        } else {
            throw new IOException("Unable to build json. ");
        }
        System.out.println("JSON built");
    }

    private String buildJsonFile(File recipientsCsv, String ID_user_list) throws InvalidParamsException, FileNotFoundException, IOException {

        boolean hasMapping = !this.columnMapping.isEmpty();
        String resFilePath = null;
        ICsvMapReader csvreader = null;
        JsonGenerator jGenerator = null;
        FileOutputStream fs;
        try {
            csvreader = new CsvMapReader(new FileReader(recipientsCsv), CsvPreference.STANDARD_PREFERENCE);
            // the header elements are used to map the values to the bean (names must match)
            final String[] header = csvreader.getHeader(true);

            /*validate mapping*/
            validImportColumns(header);

            JsonFactory jfactory = new JsonFactory();

            /** * Build json request into a file ** */
            resFilePath
                    = recipientsCsv.getParent() + File.separator + recipientsCsv.getName() + "rq.json";

            fs = new FileOutputStream(new File(resFilePath));
            jGenerator = jfactory.createGenerator(fs, JsonEncoding.UTF8);
            /*START object*/
            jGenerator.writeStartObject();
            //credentials
            jGenerator.writeStringField("function", this.getFunction());
            jGenerator.writeStringField("id", this.getClient_id());
            jGenerator.writeStringField("md5", this.getClient_md5());
            /*start PARAMETERS*/
            jGenerator.writeFieldName("parameters");
            jGenerator.writeStartObject();

            jGenerator.writeNumberField("ID_user_list", Long.valueOf(ID_user_list));
            /*start RECIPIENTS ARRAY*/
            jGenerator.writeFieldName("recipients");
            jGenerator.writeStartArray();

            /*recipients objects*/
            Map<String, String> recipients;
            while ((recipients = csvreader.read(header)) != null) {
                jGenerator.writeStartObject();
                for (Entry<String, String> field : recipients.entrySet()) {
                    if (hasMapping) {
                        jGenerator.writeStringField(columnMapping.get(field.getKey().toLowerCase()).name(), field.getValue());
                    } else {
                        jGenerator.writeStringField(field.getKey().toLowerCase(), field.getValue());
                    }
                }
                jGenerator.writeEndObject();
            }
            /*end RECIPIENTS ARRAY*/
            jGenerator.writeEndArray();

            /*end PARAMETERS*/
            jGenerator.writeEndObject();
            /*end JSON*/
            jGenerator.writeEndObject();

        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            try {
                csvreader.close();
                jGenerator.close();
            } catch (RuntimeException | IOException e) {
                //do nothing
            }
        }

        return resFilePath;
    }

    private boolean validImportColumns(String[] header) throws InvalidParamsException {

        String mappingError = "";
        for (String h : header) {

            if (this.columnMapping.isEmpty()) {
                if (!EnumUtils.isValidEnum(ListImportColumns.class, h.toLowerCase())) {
                    mappingError += "Column name " + h + " is invalid. \n";
                }
            } else if (!this.columnMapping.containsKey(h.toLowerCase())) {
                mappingError += "Column name " + h + " is invalid, it is not specified in column mapping. \n";
            }
        }
        if (mappingError.equals("")) {
            return true;
        } else {
            throw new InvalidParamsException(mappingError);
        }

    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(new File(this.rqFilePath));
    }

    @Override
    public boolean isStreaming() {
        return true;
    }

    public String getRqFilePath() {
        return rqFilePath;
    }

    public static class InvalidParamsException extends Exception {

        public InvalidParamsException(String message) {
            super(message);
        }

    }

    @Override
    public long getContentLength() {
        return contentLength;
    }

    public static enum ListImportColumns {
        first_name,
        last_name,
        email,
        status,
        company,
        prefix,
        vocative,
        reply_to,
        nick_name,
        gender,
        phone,
        mobile,
        fax,
        street,
        city,
        state,
        country,
        zip,
        custom1,
        custom2,
        custom3,
        custom4,
        custom5,
        custom6,
        custom7,
        custom8,
        custom9,
        custom10,
        custom11,
        custom12,
        custom13,
        custom14,
        custom15,
        custom16,
        custom17,
        custom18,
        custom19,
        custom20,
        custom21,
        custom22,
        custom23,
        custom24,
        custom25
    }
}
