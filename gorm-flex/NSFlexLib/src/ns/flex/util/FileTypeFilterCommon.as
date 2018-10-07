package ns.flex.util {
import flash.net.FileFilter;

/**
 * 文件选择对话框对应的文件类型
 */
public class FileTypeFilterCommon {
    private static const allFilter:FileFilter =
            new FileFilter("All(*.*)", "*.*");
    private static const imageFilter:FileFilter =
            new FileFilter("Images(*.jpg;*.gif;*.png)", "*.jpg;*.gif;*.png");
    private static const docFilter:FileFilter =
            new FileFilter("Documents(*.pdf;*.doc;*.docx;*.ppt;*.pptx;*.xls;*.xlsx;*.txt;)", "*.pdf;*.doc;*.docx;*.ppt;*.pptx;*.xls;*.xlsx;*.txt;");

    public static const IMAGE:Array = [imageFilter];
    public static const DEFAULT:Array = [imageFilter, docFilter];
    public static const ALL:Array = [allFilter, imageFilter, docFilter];
}
}
