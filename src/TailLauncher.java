import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.List;

public class TailLauncher {
    @Option(name = "-c", metaVar = "num", usage = "Sets the number of charactes to print")
    private int charNumber = -1;

    @Option(name = "-n", metaVar = "num", usage = "Sets the number of strings to print")
    private int strNumber = -1;

    @Option(name = "-o", metaVar = "ofile", usage = "Sets the output file")
    private String outputFile = "";

    @Argument(metaVar = "InputName", usage = "Input file name")
    private List<String> inputFiles;

    public static void main(String[] args) {
        new TailLauncher().launch(args);
    }

    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar Tail.jar -c num -n num -o ofile file0, file1, file2...");
            parser.printUsage(System.err);
            return;
        }

        if (charNumber != -1 && strNumber != -1) {
            System.err.println("Check format cmd line");
            return;
        }

        if (charNumber == -1 && strNumber == -1) {
            charNumber = 0;
            strNumber = 10;
        }
        Tail tail = new Tail(charNumber, strNumber);


        try {
            BufferedWriter out;
            if (!outputFile.equals("")) {
                File output = new File(outputFile);
                if (output.isFile()) output.delete();
                out = new BufferedWriter(new FileWriter(outputFile));
            } else {
                out = new BufferedWriter(new OutputStreamWriter(System.out));
            }

            BufferedReader in;
            if (inputFiles != null) {
                for (int i = 0; i < inputFiles.size(); i++) {
                    in = new BufferedReader(new FileReader(inputFiles.get(i)));
                    out.write(inputFiles.get(i) + ":");
                    out.newLine();
                    for (String j : tail.getTail(in, out)) {
                        out.write(j);
                    }
                    out.newLine();
                }
            } else {
                in = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Write \"Ctrl+C\" to end the program");
                for (String j : tail.getTail(in, out)) {
                    out.write(j);
                }
            }
            out.close();
            System.out.println("Done.");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
    }
}
