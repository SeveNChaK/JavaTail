import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class TailLauncher {
    @Option(name="-c", metaVar="num", usage="Sets the number of charactes to print")
    private Integer charNumber = -1;

    @Option(name="-n", metaVar="num", usage="Sets the number of strings to print")
    private Integer strNumber = -1;

    @Option(name="-o", metaVar="ofile", usage="Sets the output file")
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
            charNumber = 10;
            strNumber = 0;
        }
        Tail tail = new Tail(charNumber, strNumber, inputFiles, outputFile);
        try {
            FileOutputStream intermadiateOutputStream = new FileOutputStream(outputFile);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(intermadiateOutputStream))) {
                writer.write(""); //Используется чтобы очистить выходной файл после предыдущей работы. не нашел ничего получше:)
                writer.close();
            }
            intermadiateOutputStream.close();
            for (int i=0; i<inputFiles.size(); i++){
                FileOutputStream outputStream = new FileOutputStream(outputFile, true);
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
                    writer.write(inputFiles.get(i) + ":");
                    writer.newLine();
                    writer.close();
                }
                outputStream.close();
                tail.writeTail(inputFiles.get(i), outputFile);
            }
            System.out.println("Done. Check file: " + outputFile);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }









    }
}
