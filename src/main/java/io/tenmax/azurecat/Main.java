package io.tenmax.azurecat;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import org.apache.commons.cli.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String VERSION = "0.1.0";
    private static final int READ_SIZE = 64 * 1024;

    private CommandLine commandLine = null;
    private List<CloudStorageAccount> accounts = new ArrayList<CloudStorageAccount>();

    private void parseArgs(String[] args) {
        // create the command line parser
        CommandLineParser parser = new DefaultParser();

        // create the Options
        Options options = new Options();
        options.addOption( "c", true, "The connection string" );
        options.addOption( "h", false, "The help information" );
        options.addOption( "v", false, "The version" );

        try {
            // parse the command line arguments
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println( "Unexpected exception:" + e.getMessage() );
            System.exit(1);
        }

        if (commandLine.hasOption('v')) {
            printVersion();
        } else if(commandLine.hasOption('h')) {
            printHelp(options);
        } else if (commandLine.getArgs().length != 1) {
            printHelp(options);
        }
    }

    private void printVersion() {
        System.out.println("azurecat version " + VERSION);
        System.exit(0);
    }

    private void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax =
                "azurecat [-c <connection-string>] <blob-uri>";
        formatter.printHelp(cmdLineSyntax, options);
        System.exit(0);
    }

    public void readAccountsFromFile() {
        File file = new File(System.getProperty("user.home") + "/.azure/storagekeys");
        if (!file.exists()) {
            return;
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String line;
            while ( (line = in.readLine()) != null) {
                CloudStorageAccount account = CloudStorageAccount.parse(line);
                accounts.add(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readAccountsFromArgs() {
        if ( !commandLine.hasOption('c')) {
            return;
        }

        CloudStorageAccount account = null;
        try {
            account = CloudStorageAccount.parse(commandLine.getOptionValue('c'));
            accounts.add(account);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public Main(String[] args) throws IOException, URISyntaxException, StorageException, InvalidKeyException {
        parseArgs(args);

        readAccountsFromFile();
        readAccountsFromArgs();

        URI blobUri = URI.create(commandLine.getArgs()[0]);
        String accountName = blobUri.getHost().split("\\.")[0];
        CloudStorageAccount account = null;
        for (CloudStorageAccount tmpAccount : accounts) {
            if(tmpAccount.getCredentials().getAccountName().equals(accountName)) {
                account = tmpAccount;
                break;
            }
        }

        if (account == null) {
            System.err.println("Connection String for " + accountName + " is not defined");
            System.exit(-1);
            return;
        }

        CloudBlockBlob blob = new CloudBlockBlob(blobUri, account.getCredentials());
        if (!blob.exists()) {
            System.err.println("The specified blob does not exist");
            System.exit(-1);
        }

        blob.setStreamMinimumReadSizeInBytes(READ_SIZE);
        BlobInputStream in = blob.openInputStream();

        byte[] buffer = new byte[READ_SIZE];
        int read;
        while((read = in.read(buffer)) > 0) {
            System.out.write(buffer,0, read);
            System.out.flush();

            if(System.out.checkError()) {
                break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Main(args);
    }
}


