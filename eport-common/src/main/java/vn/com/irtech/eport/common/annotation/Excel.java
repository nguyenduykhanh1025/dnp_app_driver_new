package vn.com.irtech.eport.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom export Excel data annotation
 * 
 * @author admin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel
{
    /**
     * Sort in excel when exporting
     */
    public int sort() default Integer.MAX_VALUE;

    /**
     * Name exported to Excel.
     */
    public String name() default "";

    /**
     * Date format, such as: yyyy-MM-dd
     */
    public String dateFormat() default "";

    /**
     * If it is a dictionary type, please set the type value of the dictionary (eg: sys_user_sex)
     */
    public String dictType() default "";

    /**
     * Read content to expression (e.g.: 0=male, 1=female, 2=unknown)
     */
    public String readConverterExp() default "";

    /**
     * Separator, read the contents of the string group
     */
    public String separator() default ",";

    /**
     * Export type (0 number 1 string)
     */
    public ColumnType cellType() default ColumnType.STRING;

    /**
     * The height of each column in excel when exporting in characters
     */
    public double height() default 14;

    /**
     * When exporting, the width of each column in excel is in characters
     */
    public double width() default 16;

    /**
     * Text suffix, such as% 90 becomes 90%
     */
    public String suffix() default "";

    /**
     * When the value is empty, the default value of the field
     */
    public String defaultValue() default "";

    /**
     * Prompt information
     */
    public String prompt() default "";

    /**
     * You can only select the column content that cannot be entered.
     */
    public String[] combo() default {};

    /**
     * Whether to export data and respond to needs: Sometimes we need to export a template, which is required for the title but the content needs to be filled in manually by the user.
     */
    public boolean isExport() default true;

    /**
     * The attribute name in another class supports multi-level acquisition, separated by decimal points
     */
    public String targetAttr() default "";

    /**
     * Field type (0: export and import; 1: export only; 2: import only)
     */
    Type type() default Type.ALL;

    public enum Type
    {
        ALL(0), EXPORT(1), IMPORT(2);
        private final int value;

        Type(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }

    public enum ColumnType
    {
        NUMERIC(0), STRING(1);
        private final int value;

        ColumnType(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }
}
