import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Tail {

    private final int charNumber;
    private final int strNumber;
    private final List<String> inputFiles;
    private final String outputFile;

    public Tail(int charNumber, int strNumber, List<String> inputFiles, String outputFile) {
        this.charNumber = charNumber;
        this.strNumber = strNumber;
        this.inputFiles = inputFiles;
        this.outputFile = outputFile;
    }

    private String lastChars(InputStream in) throws IOException {
        List<Integer> firstPart = new ArrayList<>();
        List<Integer> secondPart = new ArrayList<>();
        List<Integer> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            int symbol = -1;
            int i = 0;
            do {
                secondPart.clear();
                secondPart.addAll(firstPart);
                firstPart.clear();
                do {
                    symbol = reader.read();
                    if (symbol == 10 || symbol == 13) continue;
                    if (symbol == -1) {
                        if (!secondPart.isEmpty()) {
                            final Integer num = charNumber - firstPart.size();
                            result.addAll(secondPart.subList(secondPart.size() - num, charNumber));
                        }
                        result.addAll(firstPart);
                        break;
                    }
                    firstPart.add(symbol);
                    i++;
                } while (i < charNumber);
                i = 0;
            } while (symbol != -1);
            String string = "";
            for (int j : result) {
                string = string + (char) (int) j;
            }
            return string;
        }
    }

    private List<String> lastStrings(InputStream in) throws IOException {
        List<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String intermediate = reader.readLine();
            while (intermediate != null) {
                if (result.size() == strNumber) {
                    result.remove(0);
                }
                result.add(intermediate);
                intermediate = reader.readLine();
            }
        }
        return result;
    }

    public void writeTail(InputStream in, OutputStream out) throws IOException {
        if (charNumber > 0) {
            String result = lastChars(in);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
                writer.write(result);
                writer.newLine();
                writer.newLine();
            }
        }
        if (strNumber > 0) {
            List<String> result = new ArrayList<>();
            result.addAll(lastStrings(in));
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
                for (int i = 0; i < result.size(); i++) {
                    writer.write(result.get(i));
                    writer.newLine();
                    writer.newLine();
                }
            }
        }
    }

    public void writeTail(String inputNameFile, String outputNameFile) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(inputNameFile)) {
            try (FileOutputStream outputStream = new FileOutputStream(outputNameFile, true)) {
                writeTail(inputStream, outputStream);
            }
        }
    }
}
