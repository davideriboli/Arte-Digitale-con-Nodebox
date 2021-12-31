package nodebox.node;import com.google.common.collect.ImmutableList;import com.google.common.collect.ImmutableMap;import com.google.common.collect.ImmutableTable;import nodebox.graphics.*;import java.util.List;public class TypeConversions {    private final static ImmutableTable<Class, String, ListConverter> conversionTable;    private final static ImmutableMap<String,Class> typeClassMap;    static {        ImmutableTable.Builder<Class, String, ListConverter> builder = ImmutableTable.builder();        builder.put(Long.class, Port.TYPE_FLOAT, new IntToFloatConverter());        builder.put(Long.class, Port.TYPE_STRING, new ObjectToStringConverter());        builder.put(Long.class, Port.TYPE_BOOLEAN, new IntToBooleanConverter());        builder.put(Long.class, Port.TYPE_COLOR, new IntToColorConverter());        builder.put(Long.class, Port.TYPE_POINT, new IntToPointConverter());        builder.put(Long.class, Port.TYPE_LIST, new NoOpConverter());        builder.put(Double.class, Port.TYPE_INT, new FloatToIntConverter());        builder.put(Double.class, Port.TYPE_STRING, new ObjectToStringConverter());        builder.put(Double.class, Port.TYPE_BOOLEAN, new FloatToBooleanConverter());        builder.put(Double.class, Port.TYPE_COLOR, new FloatToColorConverter());        builder.put(Double.class, Port.TYPE_POINT, new FloatToPointConverter());        builder.put(Double.class, Port.TYPE_LIST, new NoOpConverter());        builder.put(String.class, Port.TYPE_INT, new StringToIntConverter());        builder.put(String.class, Port.TYPE_FLOAT, new StringToFloatConverter());        builder.put(String.class, Port.TYPE_BOOLEAN, new StringToBooleanConverter());        builder.put(String.class, Port.TYPE_COLOR, new StringToColorConverter());        builder.put(String.class, Port.TYPE_POINT, new StringToPointConverter());        builder.put(String.class, Port.TYPE_LIST, new NoOpConverter());        builder.put(Boolean.class, Port.TYPE_INT, new BooleanToIntConverter());        builder.put(Boolean.class, Port.TYPE_FLOAT, new BooleanToFloatConverter());        builder.put(Boolean.class, Port.TYPE_STRING, new ObjectToStringConverter());        builder.put(Boolean.class, Port.TYPE_COLOR, new BooleanToColorConverter());        builder.put(Boolean.class, Port.TYPE_LIST, new NoOpConverter());        builder.put(Color.class, Port.TYPE_STRING, new ObjectToStringConverter());        builder.put(Color.class, Port.TYPE_LIST, new NoOpConverter());        builder.put(Point.class, Port.TYPE_STRING, new ObjectToStringConverter());        builder.put(Point.class, Port.TYPE_LIST, new NoOpConverter());        builder.put(Geometry.class, Port.TYPE_STRING, new ObjectToStringConverter());        builder.put(Geometry.class, Port.TYPE_POINT, new GeometryToPointsConverter());        builder.put(Geometry.class, Port.TYPE_LIST, new NoOpConverter());        builder.put(Path.class, Port.TYPE_STRING, new ObjectToStringConverter());        builder.put(Path.class, Port.TYPE_POINT, new GeometryToPointsConverter());        builder.put(Path.class, Port.TYPE_LIST, new NoOpConverter());        builder.put(Contour.class, Port.TYPE_STRING, new ObjectToStringConverter());        builder.put(Contour.class, Port.TYPE_POINT, new GeometryToPointsConverter());        builder.put(Contour.class, Port.TYPE_LIST, new NoOpConverter());        conversionTable = builder.build();        ImmutableMap.Builder<String, Class> b = ImmutableMap.builder();        b.put(Port.TYPE_INT, Long.class);        b.put(Port.TYPE_FLOAT, Double.class);        b.put(Port.TYPE_STRING, String.class);        b.put(Port.TYPE_BOOLEAN, Boolean.class);        b.put(Port.TYPE_POINT, Point.class);        b.put(Port.TYPE_COLOR, Color.class);        b.put(Port.TYPE_GEOMETRY, Geometry.class);        typeClassMap = b.build();    }    public static List<?> convert(Class sourceType, String targetType, List<?> values) {        ListConverter converter = conversionTable.get(sourceType, targetType);        if (converter != null) {            return converter.convert(values);        } else {            return values;        }    }    public static boolean canBeConverted(Class sourceType, String targetType) {        return conversionTable.contains(sourceType, targetType);    }    public static boolean canBeConverted(String sourceType, String targetType) {        Class sourceClass = typeClassMap.get(sourceType);        return sourceClass != null && conversionTable.contains(sourceClass, targetType);    }    private TypeConversions() {}    private static interface ListConverter {        public List<?> convert(List<?> values);    }    private abstract static class ValueConverter implements ListConverter {        public List<?> convert(List<?> values) {            ImmutableList.Builder<Object> b = ImmutableList.builder();            for (Object v : values) {                b.add(convertValue(v));            }            return b.build();        }        public abstract Object convertValue(Object value);    }    private static class NoOpConverter implements ListConverter {        @Override        public List<?> convert(List<?> values) {            return values;        }    }    private static class IntToFloatConverter extends ValueConverter {        public Object convertValue(Object value) {            return ((Long) value).doubleValue();        }    }    private static class ObjectToStringConverter extends ValueConverter {        public Object convertValue(Object value) {            return value.toString();        }    }    private static class IntToBooleanConverter extends ValueConverter {        public Object convertValue(Object value) {            // TODO Which values are true vs false?            return ((Long) value) > 0;        }    }    private static class IntToColorConverter extends ValueConverter {        public Object convertValue(Object value) {            long v = (Long) value;            return new Color(v / 255.0, v / 255.0, v / 255.0);        }    }    private static class IntToPointConverter extends ValueConverter {        public Object convertValue(Object value) {            long v = (Long) value;            return new Point(v, v);        }    }    private static class FloatToIntConverter extends ValueConverter {        public Object convertValue(Object value) {            return Math.round((Double) value);        }    }    private static class FloatToBooleanConverter extends ValueConverter {        public Object convertValue(Object value) {            return ((Double) value) > 0;        }    }    private static class FloatToColorConverter extends ValueConverter {        public Object convertValue(Object value) {            double v = (Double) value;            return new Color(v / 255.0, v / 255.0, v / 255.0);        }    }    private static class FloatToPointConverter extends ValueConverter {        public Object convertValue(Object value) {            double v = (Double) value;            return new Point(v, v);        }    }    private static class StringToIntConverter extends ValueConverter {        public Object convertValue(Object value) {            return Long.parseLong((String) value);        }    }    private static class StringToFloatConverter extends ValueConverter {        public Object convertValue(Object value) {            return Double.parseDouble((String) value);        }    }    private static class StringToBooleanConverter extends ValueConverter {        public Object convertValue(Object value) {            return Boolean.parseBoolean((String) value);        }    }    private static class StringToColorConverter extends ValueConverter {        public Object convertValue(Object value) {            return Color.parseColor((String) value);        }    }    private static class StringToPointConverter extends ValueConverter {        public Object convertValue(Object value) {            return Point.parsePoint((String) value);        }    }    private static class BooleanToIntConverter extends ValueConverter {        public Object convertValue(Object value) {            return ((Boolean) value) ? 1 : 0;        }    }    private static class BooleanToFloatConverter extends ValueConverter {        public Object convertValue(Object value) {            return ((Boolean) value) ? 1.0 : 0.0;        }    }    private static class BooleanToColorConverter extends ValueConverter {        public Object convertValue(Object value) {            return ((Boolean) value) ? Color.WHITE : Color.BLACK;        }    }    private static class GeometryToPointsConverter implements ListConverter {        public List<?> convert(List<?> values) {            ImmutableList.Builder<Object> b = ImmutableList.builder();            for (Object v : values) {                b.addAll(((IGeometry) v).getPoints());            }            return b.build();        }    }}