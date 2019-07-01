package neo.script.gorm.portal.config.pds

abstract class QueryFormatter implements Comparable<QueryFormatter> {
    abstract String format(String query);

    abstract Integer order();

    @Override
    int compareTo(QueryFormatter qf) {
        return Integer.compare(this.order(), qf.order());
    }
}
