/**
 * Class to connect to the key identity table in the DB that controls which keys we give out to newly created objects
 */
public class KeyMapper {
    private static KeyMapper keyMapper;

    long currentKey;
    private KeyMapper() {}

    public static KeyMapper getInstance() {
        if (keyMapper == null) {
            keyMapper = new KeyMapper();
        }
        return keyMapper;
    }

    public long getKey() {
        // get current key from the db, increment, then push
        return currentKey;
    }
}
