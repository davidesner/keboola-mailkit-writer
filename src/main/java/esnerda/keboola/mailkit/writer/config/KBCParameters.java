/*
 */
package esnerda.keboola.mailkit.writer.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import esnerda.keboola.mailkit.writer.mailkitapi.requests.StreamingListImport;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.EnumUtils;

/**
 *
 * @author David Esner <esnerda at gmail.com>
 * @created 2015
 */
public class KBCParameters {

    private final static String[] REQUIRED_FIELDS = {"clientId", "clientMd5"};
    private final Map<String, Object> parametersMap;

    @JsonProperty("clientId")
    private String clientId;
    @JsonProperty("#clientMd5")
    private String clientMd5;
    @JsonProperty("listId")
    private String listId;

    @JsonProperty("columnMapping")
    private List<ColumnMapping> colMapping;
    @JsonProperty("newList")
    private NewList newList;

    private Map<String, StreamingListImport.ListImportColumns> columnMapping;

    public KBCParameters() {
        parametersMap = new HashMap();
    }

    @JsonCreator
    public KBCParameters(@JsonProperty("clientId") String clientId, @JsonProperty("#clientMd5") String clientMd5,
            @JsonProperty("listId") String listId, @JsonProperty("columnMapping") List<ColumnMapping> colMapping,
            @JsonProperty("newList") NewList newList) {
        parametersMap = new HashMap();
        this.clientId = clientId;
        this.clientMd5 = clientMd5;
        this.listId = listId;
        this.colMapping = colMapping;
        this.newList = newList;
        if (colMapping != null) {
            this.columnMapping = new HashMap();
            for (ColumnMapping cm : this.colMapping) {
                if (EnumUtils.isValidEnum(StreamingListImport.ListImportColumns.class, cm.getDestCol().toLowerCase())) {
                    this.columnMapping.put(cm.srcCol, StreamingListImport.ListImportColumns.valueOf(cm.destCol.toLowerCase()));
                }
            }
        } else {
            this.columnMapping = new HashMap();
        }

        //set param map
        parametersMap.put("clientId", clientId);
        parametersMap.put("clientMd5", clientMd5);

    }

    /**
     * Returns list of required fields missing in config
     *
     * @return
     */
    private List<String> getMissingFields() {
        List<String> missing = new ArrayList<String>();
        for (int i = 0; i < REQUIRED_FIELDS.length; i++) {
            Object value = parametersMap.get(REQUIRED_FIELDS[i]);
            if (value == null) {
                missing.add(REQUIRED_FIELDS[i]);
            }
        }

        if (missing.isEmpty()) {
            return null;
        }
        return missing;
    }

    private String missingFieldsMessage() {
        List<String> missingFields = getMissingFields();
        String msg = "";
        if (missingFields != null && missingFields.size() > 0) {
            msg = "Required config fields are missing: ";
            int i = 0;
            for (String fld : missingFields) {
                if (i < missingFields.size()) {
                    msg += fld + ", ";
                } else {
                    msg += fld;
                }
            }
        }
        return msg;
    }

    public boolean validateParametres() throws ValidationException {
        //validate date format
        String error = "";

        error += missingFieldsMessage();

        if ((this.listId == null || this.listId.equals("")) && (this.newList == null || this.newList.name == null)) {
            error += "\n List ID parameter is missing. Either specify new list or list ID";
        }

        if (this.newList != null && (this.newList.name != null && this.newList.name.equals(""))) {
            error += "\n New list name cannot be empty string!";
        }
        if (this.colMapping != null && !this.colMapping.isEmpty()) {
            //validate dest fields
            for (ColumnMapping cm : this.colMapping) {
                if (!EnumUtils.isValidEnum(StreamingListImport.ListImportColumns.class, cm.getDestCol().toLowerCase())) {
                    error += "Column " + cm.destCol + " is not valid destination column name. ";
                }
            }

        }
        if (error.equals("")) {
            return true;
        } else {

            throw new ValidationException("Validation error: " + error);
        }
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientMd5() {
        return clientMd5;
    }

    public void setClientMd5(String clientMd5) {
        this.clientMd5 = clientMd5;
    }

    public Map<String, Object> getParametersMap() {
        return parametersMap;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public Map<String, StreamingListImport.ListImportColumns> getColumnMapping() {
        return columnMapping;
    }

    public static class ColumnMapping {

        private String srcCol;
        private String destCol;

        public ColumnMapping() {
        }

        public String getSrcCol() {
            return srcCol;
        }

        public void setSrcCol(String srcCol) {
            this.srcCol = srcCol;
        }

        public String getDestCol() {
            return destCol;
        }

        public void setDestCol(String destCol) {
            this.destCol = destCol;
        }
    }

    public static class NewList {

        private String name;
        private String desc;

        public NewList() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

    }

    public List<ColumnMapping> getColMapping() {
        return colMapping;
    }

    public NewList getNewList() {
        return newList;
    }

}
