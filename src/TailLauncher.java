import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.List;

public class TailLauncher {
    @Option(name = "-c", metaVar = "num", usage = "Sets the number of charactes to print")
    private Integer charNumber = -1;

    @Option(name = "-n", metaVar = "num", usage = "Sets the number of strings to print")
    private Integer strNumber = -1;

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

        try {
            if (charNumber != -1 && strNumber != -1) throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            System.err.println("Check format cmd line");
            return;
        }

        if (charNumber == -1 && strNumber == -1) {
            charNumber = 0;
            strNumber = 10;
        }
        Tail tail = new Tail(charNumber, strNumber);


        try {
            OutputStream out;
            if (!outputFile.equals("")) {
                FileOutputStream intermadiateOutputStream = new FileOutputStream(outputFile);
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(intermadiateOutputStream))) {
                    writer.write(""); //Используется чтобы очистить выходной файл после предыдущей работы. не нашел ничего получше:)
                    writer.close();
                }
                intermadiateOutputStream.close();
                out = new FileOutputStream(outputFile, true);
            } else {
                out = System.out;
            }

            if (inputFiles != null) {
                for (int i = 0; i < inputFiles.size(); i++) {
                    FileInputStream in = new FileInputStream(inputFiles.get(i));
                    if (!outputFile.equals("")) {
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile, true)));
                        writer.write(inputFiles.get(i) + ":");
                        writer.newLine();
                        writer.close();
                    } else {
                        System.out.println(inputFiles.get(i) + ":");
                    }
                    tail.writeTail(in, out);
                }
            } else {
                InputStream in = System.in;
                System.out.println("Write \"/end\" to end the program");
                tail.writeTail(in, out);
            }
            System.out.println("Done.");

        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
    }
}
