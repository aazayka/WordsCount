import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

class Scratch {
    public static void main(String[] args) throws IOException {
        Map<String, Long> words =
                Files.lines(Paths.get("American Beauty-English.srt"))
                        .filter(str -> !str.trim().isBlank())
                        .filter(str -> !str.matches("^\\d+.*"))
                        .map(str -> str.split("[^\\w']"))
                        .flatMap(x -> Arrays.stream(x))
                        .filter(str -> !str.trim().isBlank())
                        .collect(Collectors.groupingBy(
                                s -> s.toLowerCase(),
                                Collectors.counting()));

        System.out.println("Words count: " + words.keySet().size());
        System.out.println("20 most popular words: ");
        PriorityQueue<Word> sortedWords = new PriorityQueue<>(Collections.reverseOrder());
        words.forEach(
                (k, v) -> sortedWords.add(new Word(v, k))
        );

        sortedWords.stream()
                .limit(20)
                .forEach(System.out::println);
    }

    static class Word implements Comparable<Word> {
        public Word(long cnt, String word) {
            this.cnt = cnt;
            this.word = word;
        }

        public long getCnt() {
            return cnt;
        }

        public String getWord() {
            return word;
        }

        private long cnt;
        private String word;

        @Override
        public int compareTo(Word word) {
            return Comparator.comparing(Word::getCnt).thenComparing(Word::getWord).compare(this, word);
        }

        @Override
        public String toString() {
            return String.format("'%s' used %d times", word, cnt);
        }
    }
}