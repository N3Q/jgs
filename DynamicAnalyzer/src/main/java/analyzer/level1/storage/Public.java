package analyzer.level1.storage;

/**
 * Created by Nicolas Müller on 02.02.17.
 */
public class Public<Level> extends Types<Level> {
    @Override
    public boolean isPublic() {
        return true;
    }
}
