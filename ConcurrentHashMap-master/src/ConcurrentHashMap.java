import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentHashMap {
    class Entry<K, V>{
        K key;
        V value;
        int hash;
        Entry<K, V> next;
        public Entry(K key, int hash, V value, Entry<K, V> next){
            this.key=key;
            this.value=value;
            this.next=next;
            this.hash=hash;
        }

    }
    class Segment<K, V> extends ReentrantLock{
        Entry<K, V> [] table = new Entry[10];
        void put(K key, int hash, V value){
            lock();
            try {
                Entry<K, V> part=table[hash & (table.length-1)];
                while (part!=null && part.key!=key){
                    part=part.next;
                }
                if (part!=null){
                    if (part.key==key){
                        part.value=value;
                    }
                    else {
                        part.next=new Entry<>(key, hash, value, null);
                    }
                    table[hash & (table.length-1)]=part;
                }
                else {
                    table[hash & (table.length-1)]=new Entry<>(key, hash, value, null);

                }

            }
            finally {
                unlock();
            }
        }
        V get(K key, int hash){
            V value = null;
            Entry<K, V> part=getPartition(hash);
            while (part!=null){
               // System.out.println(part.key);
                if (part.key==key){
                    value=part.value;
                    break;
                }
                part=part.next;
            }
            return value;
        }
        V remove(K key, int hash){
            lock();
            try{
                int index=hash & table.length-1;
                Entry<K, V> part=table[hash & (table.length-1)];
                Entry<K, V> prev = null;
                while (part.key!=key && part!=null){
                   prev=part;
                   part=part.next;
                }
                if (prev==null){
                    table[hash & (table.length-1)]=part.next;
                    return part.value;
                }
                else {
                    prev.next=part.next;
                    table[hash & (table.length-1)]=prev;
                    return part.value;
                }

            }
            finally {
                unlock();
            }
        }


        Entry<K, V> getPartition(int hash){
            return table[hash & (table.length-1)];
        }
    }
    Segment [] segments=new Segment[32];

    Segment partSegment(int hash){
        return segments[(hash & 0x1F)];
    }

    static int hash(Object x) {
        int h = x.hashCode();
        return (h << 7) - h + (h >>> 9) + (h >>> 17);
    }
    void put(Object key, Object value){
        int hash=hash(key);
        partSegment(hash).put(key, hash, value);
    }
    Object get(Object key){
        int hash=hash(key);
        Object value=partSegment(hash).get(key, hash);
        return value;
    }
    Object remove(Object key){
        int hash=hash(key);
        return partSegment(hash).remove(key, hash);
    }




    ConcurrentHashMap(){
        for (int i=0; i<segments.length; i++) segments[i]=new Segment();
    }
}
