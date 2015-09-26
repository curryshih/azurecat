# azurecat
*azurecat* is a CLI tool to *cat* the [microsoft azure blob storage.](https://azure.microsoft.com/en-us/documentation/articles/storage-introduction/#blob-storage)

# Requirement
Java 6 or higher

# Getting Start

1. First, you must prepare your [Connection String](https://azure.microsoft.com/en-us/documentation/articles/storage-configure-connection-string/) for your storage account. The format is 

	```bash
	DefaultEndpointsProtocol=[http|https];AccountName=myAccountName;AccountKey=myAccountKey
	```

2. Download from [release](https://github.com/tenmax/azurecat/releases).

3. Run the command,

	```bash
	azurecat -c <connection-string> http://<account-name>.blob.core.windows.net/<container-name>/<blob-path>
	```
	
# Configuration File

You can put your connection strings at `~/.azure/storagekeys` line by line

Here is the exmaple.

```
DefaultEndpointsProtocol=https;AccountName= myAccountName1;AccountKey=myAccountKey1
DefaultEndpointsProtocol=https;AccountName= myAccountName2;AccountKey=myAccountKey2
```

then, you can print the resource directly without connection string specified.

```bash
azurecat https://<account-name>.blob.core.windows.net/<container-name>/<blob-path>
```
	
# Usage

1. Print a resource

	```bash
	azurecat https://<account-name>.blob.core.windows.net/<container-name>/<blob-path>
	```
	
2. Concatenate and print the resources with prefix

	```bash
	azurecat --prefix https://<account-name>.blob.core.windows.net/<container-name>/<blob-prefix>
	```
3. Concatenate and print the resources with prefix and the resource should match the postfix

	```bash
	azurecat --prefix --postfix csv https://<account-name>.blob.core.windows.net/<container-name>/<blob-prefix>
	```

4. Decode the resource by gzip compression format.

	```bash
	azurecat -z --prefix --postfix gz https://<account-name>.blob.core.windows.net/<container-name>/<blob-prefix>
	```
	
The full help for `azurecat`

```
usage: azurecat [-c <connection-string>] <blob-uri>
 -c <arg>                The connection string
 -h                      The help information
    --postfix <string>   keep only the blob which has the path with the
                         specified postfix. The postfix only be used while
                         prefix is used.
    --prefix             cat all the blobs with the prefix
 -v                      The version
 -z                      The gzip format
```