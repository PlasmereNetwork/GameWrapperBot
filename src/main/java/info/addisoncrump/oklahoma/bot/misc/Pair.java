package info.addisoncrump.oklahoma.bot.misc;

import lombok.Value;

@Value
public class Pair<K, V> {
    K key;
    V value;
}
