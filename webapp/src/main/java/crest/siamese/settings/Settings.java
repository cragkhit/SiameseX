package crest.siamese.settings;

public class Settings {

	public static class MethodParserType {
		public static String METHOD = "METHOD-LEVEL";
		public static String FILE = "FILE-LEVEL";
	}

    public static class ErrorMeasure {
        public static String ARP = "ARP";
        public static String MAP = "MAP";
    }

    public static class RankingFunction {
        public static int TFIDF = 1;
        public static int BM25 = 2;
        public static int DFR = 3;
        public static int IB = 4;
        public static int LMD = 5;
        public static int LMJ = 6;
    }

	public class Normalizer {
		public static final String JAVA_CLASS_FILE = "references/JavaClass.siamese";
		public static final String JAVA_PACKAGES_FILE = "references/JavaPackages.siamese";
	}

	public class Normalize {
		public static final int WORD_NORM_ON = 1;
		public static final int WORD_NORM_OFF = 0;
		public static final int DATATYPE_NORM_ON = 1;
		public static final int DATATYPE_NORM_OFF = 0;
		public static final int JAVACLASS_NORM_ON = 1;
		public static final int JAVACLASS_NORM_OFF = 0;
		public static final int KEYWORD_NORM_ON = 1;
		public static final int KEYWORD_NORM_OFF = 0;
		public static final int KEYWORD_REMOVE = -1;
		public static final int ESCAPE_ON = 1;
		public static final int ESCAPE_OFF = 0;
		public static final int JAVAPACKAGE_NORM_ON = 1;
		public static final int JAVAPACKAGE_NORM_OFF = 0;
		public static final int STRING_NORM_ON = 1;
		public static final int STRING_NORM_OFF = 0;
		public static final int VALUE_NORM_ON = 1;
		public static final int VALUE_NORM_OFF = 0;
		public static final int OPERATOR_NORM_ON = 1;
		public static final int OPERATOR_NORM_OFF = 0;
	}
	
	public class IndexingMode {
		public static final String BULK = "bulk";
		public static final String SEQUENTIAL = "sequential";
	}
}
