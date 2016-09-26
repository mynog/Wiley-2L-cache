Two-level cache project
=======================

Summary
-------
Create a configurable two-level cache (for caching Objects).  Level 1 is memory, level 2 is filesystem. Config params should let one specify the cache strategies and max sizes of level  1 and 2

Usage
-----
mvn clean package  
cd target  
java -jar wiley-cache-1.0-SNAPSHOT.jar <memory_cache_capacity> <file_cache_capacity> <cache_strategy (LRU | MRU)>  
for example:  
java -jar wiley-cache-1.0-SNAPSHOT.jar 2 5 LRU  

