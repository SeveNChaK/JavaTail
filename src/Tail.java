import javafx.event.Event;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


public class Tail {

    private final int charNumber;
    private final int strNumber;

    public Tail(int charNumber, int strNumber) {
        this.charNumber = charNumber;
        this.strNumber = strNumber;
    }

    private List<String> lastChars(BufferedReader in) throws IOException {
        Deque<Character> resultDeque = new ArrayDeque<>();
        List<String> result = new ArrayList<>();
        int symbol = -1;
        symbol = in.read();
        while (symbol != -1) {
            if (resultDeque.size() == charNumber) {
                resultDeque.removeFirst();
            }
            resultDeque.addLast((char) symbol);
            symbol = in.read();
        }
        StringBuilder string = new StringBuilder();
        for (Character i : resultDeque) {
            string.append(i);
        }
        result.add(string.toString()+"\r\n");
        return result;
    }

    private List<String> lastStrings(BufferedReader in) throws IOException {
        Deque<String> resultDeque = new ArrayDeque<>();
        List<String> result = new ArrayList<>();
        String line = in.readLine();
        while (line != null) {
            if (resultDeque.size() == strNumber) {
                resultDeque.removeFirst();
            }
            resultDeque.addLast(line+"\r\n");
            line = in.readLine();
        }
        result.addAll(resultDeque);
        return result;
    }

    public List<String> getTail(BufferedReader in, BufferedWriter out) throws IOException {
        List<String> result = new ArrayList<>();
        if (charNumber > 0) {
            result.addAll(lastChars(in));
            return result;
        }
        if (strNumber > 0) {
            result.addAll(lastStrings(in));
            return result;
        }
        return result;
    }
}

