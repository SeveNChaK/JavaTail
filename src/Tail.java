import javafx.event.Event;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Tail {

    private final int charNumber;
    private final int strNumber;

    public Tail(int charNumber, int strNumber) {
        this.charNumber = charNumber;
        this.strNumber = strNumber;
    }

    private String lastChars(InputStream in) throws IOException {
        List<Integer> firstPart = new ArrayList<>();
        List<Integer> secondPart = new ArrayList<>();
        List<Integer> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            int symbol = -1;
            int i = 0;
            boolean markEnd = false;
            do {
                secondPart.clear();
                secondPart.addAll(firstPart);
                firstPart.clear();
                do {
                    reader.mark(255); //Узнать как точно это работает
                    if (reader.readLine() == null) {
                        markEnd = true;
                        if (!secondPart.isEmpty()) {
                            final Integer num = charNumber - firstPart.size();
                            result.addAll(secondPart.subList(secondPart.size() - num, charNumber));
                        }
                        result.addAll(firstPart);
                        break;
                    } else {
                        reader.reset();
                        if (reader.readLine().equals("/end")) {
                            markEnd = true;
                            if (!secondPart.isEmpty()) {
                                final Integer num = charNumber - firstPart.size();
                                result.addAll(secondPart.subList(secondPart.size() - num, charNumber));
                            }
                            result.addAll(firstPart);
                            break;
                        } else reader.reset();
                    }
                    symbol = reader.read();
                    if (symbol == 10 || symbol == 13) continue;
                    firstPart.add(symbol);
                    i++;
                } while (i < charNumber);
                i = 0;
            } while (symbol != -1 && markEnd != true);
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
            while (intermediate != null && !intermediate.equals("/end")) {
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
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(result);
            writer.newLine();
            writer.newLine();
            writer.flush();

        }
        if (strNumber > 0) {
            List<String> result = new ArrayList<>();
            result.addAll(lastStrings(in));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            for (int i = 0; i < result.size(); i++) {
                writer.write(result.get(i));
                writer.newLine();
            }
            writer.flush();
        }
    }
}
