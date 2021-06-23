package bank.interfaces.regExp;

public interface CreditRegExp
{
    String PERCENT_REGEXP = "\\b(?<!\\.)(?!0+(?:\\.0+)?%)(?:\\d|[1-9]\\d|100)(?:(?<!100)\\.\\d+)?";
    String LIMIT_REGEXP = "^[0-9]*[.,]?[0-9]+$";
}
