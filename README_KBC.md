#Mailkit Writer
Mailkit writer component for Keboola Connection.

##Funcionality
The component allows creating and updating Mailkit mailing lists. Simply import recipients from your KBC Storage tables.

##Configuration
###Parameters
- **Client ID ** – *(REQ)* your Mailkit client_id
- **Client MD5** – *(REQ)* Mailkit client MD5 hash.
- **List ID ** – *(REQ)* The ID of Mailkit recipients list you want to update. The list ID can be retrieved either from URL parameter in the Mailkit GUI or from a list retrieved by Mailkit extractor.
- **New List** – *(OPT)* If you wish to create a new list and upload data into it, you may specify this parameter. If specified, the `List ID` parameter will be ignored, since the data will be uploaded into the newly created list.
- **Column Mapping** – *(OPT)* Specify column name mapping of your source table if it does not match the Mailkit structure specified here: [mailinglist.import](https://www.mailkit.eu/cz/napoveda-pomoc/dokumentace/api/sprava-seznamu-prijemcu/mailkitmailinglistimport/)
 **NOTE**: Please note that mapping for all source table columns must be specified otherwise the upload will fail.


##Input
Input table should match the structure defined in [mailinglist.import](https://www.mailkit.eu/cz/napoveda-pomoc/dokumentace/api/sprava-seznamu-prijemcu/mailkitmailinglistimport/) otherwise the `Column Mapping` parameter must be specified for each input column. Number of input columns is optional; however, the `email` column must be always specified.

 

##Sample configurations / use cases
###Use case 1
The easiest way to use the writer is to provide input table with correct column names. This way you don’t need to specify column mapping and the configuration involves just credential parameters and List ID.
![](https://raw.githubusercontent.com/davidesner/keboola-mailkit-writer/master/img/use_case1.png)
###Use case 2
Create new mailing list and upload the contacts into it.

**NOTE*:* If a list with specified name already exists, the proccess will fail. Such configuration can be run only one time, to update the newly created list again, you need to specify its ID (returned in the standard output).

![](https://raw.githubusercontent.com/davidesner/keboola-mailkit-writer/master/img/use_case2.png)
###Use case 3
Specify the column mapping if the input table structure does not match. All source columns must be specified in the mapping otherwise the proccess will fail.
![](https://raw.githubusercontent.com/davidesner/keboola-mailkit-writer/master/img/use_case3.png)
