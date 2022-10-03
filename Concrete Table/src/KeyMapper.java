import java.sql.Statement;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Class to connect to the key identity table in the DB that controls which keys we give out to newly created objects
 */
public class KeyMapper {
    private static KeyMapper keyMapper;

    private final AtomicLong key = new AtomicLong();

    private KeyMapper() {}

    public static KeyMapper getInstance() {
        if (keyMapper == null) {
            keyMapper = new KeyMapper();
        }
        return keyMapper;
    }

    public synchronized long getNewKey() {
        // get current key from the db, increment, then push
        return key.get();
    }

}
