package app.cli;

import cache.Cache;
import cache.CacheFactory;
import exception.KeyNotFoundException;
import strategy.Strategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CacheTest {

    Cache<Integer, String> cache;
    BufferedReader br;

    public CacheTest(int memoryCacheCapacity, int fileCacheCapacity, Strategy strategy) {
        CacheFactory<Integer, String> builder = new CacheFactory<Integer, String>();
        System.out.printf("" +
                "Create new cache with\n" +
                "\tmemory cache capacity = %d\n" +
                "\tfile system cache capacity = %d\n" +
                "\tcache put strategy = %s\n", memoryCacheCapacity, fileCacheCapacity, strategy.name());
        cache = builder.build(memoryCacheCapacity, fileCacheCapacity, strategy);
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public void start() {

        while (true) {
            String c = showMenu();
            if ("0".equals(c)) {
                cache.clear();
                return;
            } else if ("1".equals(c)) {
                String s = readLine("Enter string");
                int hash = s.hashCode();
                cache.put(hash, s);
                System.out.printf("Add string '%s' with key %d\n", s, hash);
            } else if ("2".equals(c)) {
                String s = readLine("Enter key");
                int key = Integer.parseInt(s);
                try {
                    cache.remove(key);
                    System.out.printf("Remove string with key %d\n", key);
                } catch (KeyNotFoundException e) {
                    System.err.printf("Key %d not found\n", key);
                }
            } else if ("3".equals(c)) {
                String s = readLine("Enter key");
                int key = Integer.parseInt(s);
                try {
                    String s1 = cache.get(key);
                    System.out.printf("String '%s' with key %d\n", s1, key);
                } catch (KeyNotFoundException e) {
                    System.err.printf("Key %d not found\n", key);
                }
            } else if ("4".equals(c)) {
                for (Integer key : cache.getAll()) {
                    System.out.printf("String '%s' with key %d\n", cache.get(key), key);
                }
            } else {
                System.err.println("Incorrect input. Try again\n");
            }
        }
    }

    private String showMenu() {
        System.out.println("\n" +
                "\t0 - exit\n" +
                "\t1 - add\n" +
                "\t2 - remove\n" +
                "\t3 - get\n" +
                "\t4 - print all");
        return readLine("Enter number");
    }

    private String readLine(String line) {
        while (true) {
            System.out.print(line + ">");
            try {
                String s = br.readLine();
                if (s != null) {
                    s = s.trim();
                    if ("".equals(s)) {
                        System.err.println("Empty input. Try again\n");
                    } else {
                        return s;
                    }
                }
            } catch (IOException e) {
                System.err.println("Incorrect input. Try again\n");
            }
        }
    }
}
