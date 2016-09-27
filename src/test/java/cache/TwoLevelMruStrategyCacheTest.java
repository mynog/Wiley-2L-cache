package cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import strategy.Strategy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TwoLevelMruStrategyCacheTest {

    private Cache<Integer, String> cache;

    @After
    public void clear() {
        cache.clear();
    }

    @Before
    public void init() {
        CacheFactory<Integer, String> builder = new CacheFactory<Integer, String>();
        cache = builder.build(1, 2, Strategy.MRU);
    }

    @Test
    public void putObjectTest() {
        cache.put(0, "John");
        assertTrue(cache.contains(0));
        assertFalse(cache.contains(1));
        assertTrue("John".equals(cache.get(0)));
    }

    @Test
    public void removeObjectTest() {
        cache.put(0, "Joan");
        cache.remove(0);
        assertFalse(cache.contains(0));
        assertFalse(cache.contains(1));
    }

    @Test
    public void putAndRemoveObjectsTest() {
        cache.put(0, "John");
        cache.put(1, "Joan");
        assertTrue("John".equals(cache.get(0)));
        assertTrue("Joan".equals(cache.get(1)));
        cache.remove(0);
        assertTrue(cache.contains(1));
        assertFalse(cache.contains(0));
    }

    @Test
    public void cacheClearTest() {
        cache.put(0, "John");
        cache.put(1, "Joan");
        cache.put(2, "Michale");
        assertTrue("John".equals(cache.get(0)));
        assertTrue("Joan".equals(cache.get(1)));
        assertTrue("Michale".equals(cache.get(2)));
        assertTrue(cache.contains(0));
        assertTrue(cache.contains(1));
        assertTrue(cache.contains(2));
        cache.clear();
        assertFalse(cache.contains(0));
        assertFalse(cache.contains(1));
        assertFalse(cache.contains(2));
    }

    @Test
    public void removeLastMruStrategyTest() {
        cache.put(0, "John");
        cache.put(1, "Joan");
        cache.put(2, "Michale");
        cache.put(3, "Carl");
        assertTrue(cache.contains(0));
        assertTrue(cache.contains(1));
        assertFalse(cache.contains(2));
        assertTrue(cache.contains(3));
    }
}
