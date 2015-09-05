# azurecat
*azurecat* is a CLI tool to *cat* the [microsoft azure blob storage.](https://azure.microsoft.com/en-us/documentation/articles/storage-introduction/#blob-storage)

# Requirement
Java 6 or higher

# Usage

1. First, you must prepare your [Connection String](https://azure.microsoft.com/en-us/documentation/articles/storage-configure-connection-string/) for your storage account.

2. Download from release

3. Run the command,

	```bash
	azurecat -c <connection-string> http://<account-name>.blob.core.windows.net/<container-name>/<blob-path>
	```
	
	or just issue
	
	```bash
	azurecat http://<account-name>.blob.core.windows.net/<container-name>/<blob-path>
	```
	
	while the connection strings are stored in `~/.azure/storagekeys` line by line.


