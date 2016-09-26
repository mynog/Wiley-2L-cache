package app;

import app.cli.CacheTest;
import strategy.Strategy;

public class App {

    public static void main(String[] args) {
        try {
            int memoryCacheCapacity = Integer.parseInt(args[0]);
            int fileCacheCapacity = Integer.parseInt(args[1]);
            Strategy strategy = Strategy.valueOf(args[2].toUpperCase());
            CacheTest test = new CacheTest(memoryCacheCapacity, fileCacheCapacity, strategy);
            test.start();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage: java -jar wiley-cache-1.0-SNAPSHOT.jar <memory cache capacity> <file cache capacity> <cache strategy (LRU | MRU)>");
        }

    }
}
