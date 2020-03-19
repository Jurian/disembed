package org.uu.nl.embedding.util.config;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;
import org.uu.nl.embedding.util.rnd.ThreadLocalSeededRandom;
import org.uu.nl.embedding.util.rnd.ExtendedRandom;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Configuration {

    public enum EmbeddingMethod {
        GLOVE, PGLOVE
    }

    public enum OptimizationMethod {
        ADAGRAD, AMSGRAD, ADAM
    }

    public enum SimilarityMethod {
        TOKEN, COSINE, JACCARD, JAROWINKLER, LEVENSHTEIN, NUMERIC, DATE
    }

    public enum BCANormalization {
        NONE, UNITY, COUNTS
    }

    private String graph;

    public File getGraphFile() {
        return Paths.get("").toAbsolutePath().resolve(graph).toFile();
    }

    public String getGraph() {
        return graph;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }

    private String method;

    public String getMethod() {
        return  method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    private int dim;

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }

    private int threads;

    public int getThreads() {
        return threads == 0 ? (Runtime.getRuntime().availableProcessors() -1) : threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public EmbeddingMethod getMethodEnum() {
        return EmbeddingMethod.valueOf(this.method.toUpperCase());
    }

    private Map<String, Float> weights;

    public boolean usingWeights() {
        return weights != null && !weights.isEmpty();
    }

    public Map<String, Float> getWeights() {
        return weights;
    }

    public void setWeights(Map<String, Float> weights) {
        this.weights = weights;
    }

    private List<SimilarityGroup> similarity;

    public List<SimilarityGroup> getSimilarity() {
        return similarity;
    }

    public boolean usingSimilarity() {
        return similarity != null && !similarity.isEmpty();
    }

    public void setSimilarity(List<SimilarityGroup> similarity) {
        this.similarity = similarity;
    }

    private BCA bca;

    public BCA getBca() {
        return bca;
    }

    public void setBca(BCA bca) {
        this.bca = bca;
    }

    private Opt opt;

    public Opt getOpt() {
        return opt;
    }

    public void setOpt(Opt opt) {
        this.opt = opt;
    }

    private PCA pca;

    public boolean usingPca(){
        return this.pca != null;
    }

    public PCA getPca() {
        return pca;
    }

    public void setPca(PCA pca) {
        this.pca = pca;
    }

    private Output output;

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    private static ThreadLocalSeededRandom threadLocalRandom;

    public static void setThreadLocalRandom() {
        threadLocalRandom = new ThreadLocalSeededRandom(System.currentTimeMillis());
    }

    public static void setThreadLocalRandom(long seed) {
        threadLocalRandom = new ThreadLocalSeededRandom(seed);
    }

    public static ExtendedRandom getThreadLocalRandom() {
        return threadLocalRandom.get();
    }

    public static ProgressBar progressBar(String name, long max, String unitName) {
        return new ProgressBar (
                name,
                max,
                250,
                System.out,
                ProgressBarStyle.COLORFUL_UNICODE_BLOCK,
                " " + unitName,
                1,
                false
        );
    }

    public static class SimilarityGroup {

        private String predicate;
        private String method;
        private double threshold;
        private int ngram;
        private double smooth;
        private String format;

        @Override
        public String toString() {
            String out = getPredicate() + ": " + getMethod() + ", threshold: " + getThreshold();
            switch (getMethodEnum()) {
                case COSINE:
                case JACCARD: out += ", ngram: " + getNgram();
                break;
                case NUMERIC: out += ", smooth: " + getSmooth();
                break;
                case DATE: out += ", format:" + getFormat() + ", smooth: " + getSmooth();
            }
            return out;
        }

        public SimilarityMethod getMethodEnum() {
            return SimilarityMethod.valueOf(this.method.toUpperCase());
        }

        public String getFormat() {
            return format == null ? "iso" : format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getPredicate() {
            return predicate;
        }

        public void setPredicate(String predicate) {
            this.predicate = predicate;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public double getThreshold() {
            return threshold;
        }

        public void setThreshold(double threshold) {
            this.threshold = threshold;
        }

        public int getNgram() {
            return ngram == 0 ? 3 : ngram;
        }

        public void setNgram(int ngram) {
            this.ngram = ngram;
        }

        public double getSmooth() {return smooth == 0 ? 1 : smooth;}

        public void setSmooth(double smooth) {this.smooth = smooth;}
    }

    public static class BCA {

        private double alpha;
        private double epsilon;
        private boolean directed;
        private String normalize;

        public BCANormalization getNormalizeEnum() {
            return normalize == null ? BCANormalization.NONE : BCANormalization.valueOf(normalize.toUpperCase());
        }

        public String getNormalize() {
            return normalize == null ? "none" : normalize;
        }

        public void setNormalize(String normalize) {
            this.normalize = normalize;
        }

        public double getAlpha() {
            return alpha;
        }

        public void setAlpha(double alpha) {
            this.alpha = alpha;
        }

        public double getEpsilon() {
            return epsilon;
        }

        public void setEpsilon(double epsilon) {
            this.epsilon = epsilon;
        }

        public boolean isDirected() {
            return directed;
        }

        public void setDirected(boolean directed) {
            this.directed = directed;
        }

    }

    public static class Opt {

        private String method;
        private double tolerance;
        private int maxiter;

        public OptimizationMethod getMethodEnum() {
            return OptimizationMethod.valueOf(method.toUpperCase());
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public double getTolerance() {
            return tolerance;
        }

        public void setTolerance(double tolerance) {
            this.tolerance = tolerance;
        }

        public int getMaxiter() {
            return maxiter;
        }

        public void setMaxiter(int maxiter) {
            this.maxiter = maxiter;
        }
    }

    public static class PCA {

        private double variance;

        public double getVariance() {
            return variance;
        }

        public void setVariance(double variance) {
            this.variance = variance;
        }
    }

    public static class Output {

        private String name;
        private List<String> uri;
        private List<String> blank;
        private List<String> predicate;
        private List<String> literal;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean outputUriNodes() {
            return uri != null;
        }

        public boolean outputBlankNodes() {
            return blank != null;
        }

        public boolean outputPredicates() {
            return predicate != null;
        }

        public boolean outputLiteralNodes() {
            return literal != null;
        }

        public List<String> getLiteral() {
            return literal;
        }

        public void setLiteral(List<String> literal) {
            this.literal = literal;
        }

        public List<String> getUri() {
            return uri;
        }

        public void setUri(List<String> uri) {
            this.uri = uri;
        }

        public List<String> getBlank() {
            return blank;
        }

        public void setBlank(List<String> blank) {
            this.blank = blank;
        }

        public List<String> getPredicate() {
            return predicate;
        }

        public void setPredicate(List<String> predicate) {
            this.predicate = predicate;
        }
    }

    public static void check(Configuration config) throws InvalidConfigurationException {
        boolean hasDim = config.dim > 0;
        boolean hasGraph = config.graph != null && !config.graph.isEmpty();
        boolean hasMethod = config.method != null && !config.method.isEmpty();
        boolean hasBca = config.bca != null && config.bca.alpha > 0 && config.bca.epsilon > 0;
        boolean hasOut = config.output != null && (
                config.output.outputPredicates() ||
                config.output.outputBlankNodes() ||
                config.output.outputUriNodes() ||
                config.output.outputLiteralNodes());

        if(!hasDim) throw new InvalidConfigurationException("No dimension specified");
        if(!hasGraph) throw new InvalidConfigurationException("No input graph specified");
        if(!hasMethod) throw new InvalidConfigurationException("Invalid method, choose one of: glove, pglove");
        if(!hasBca) throw new InvalidConfigurationException("Invalid BCA parameters, alpha and epsilon are mandatory");
        if(!hasOut) throw new InvalidConfigurationException("Invalid output parameters, specify at least one group");
    }

    public static class InvalidConfigurationException extends Exception {
        public InvalidConfigurationException(String message) {
            super("Invalid configuration: " + message);
        }
    }
}
