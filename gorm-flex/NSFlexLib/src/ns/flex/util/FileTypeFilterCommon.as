package ns.flex.util {
import flash.net.FileFilter;

public class FileTypeFilterCommon {
    private static const imageFilter:FileFilter = new FileFilter("Images", "*.jpg;*.gif;*.png");
    private static const docFilter:FileFilter = new FileFilter("Documents", "*.pdf;*.doc;*.txt");
    public static const IMAGE:Array = [imageFilter];
    public static const DEFAULT:Array = [imageFilter, docFilter];
}
}
