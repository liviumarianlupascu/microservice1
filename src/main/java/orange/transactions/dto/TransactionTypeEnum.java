package orange.transactions.dto;

public enum TransactionTypeEnum {

    IBAN_TO_IBAN("1"),
    IBAN_TO_WALLET("2"),
    WALLET_TO_IBAN("3"),
    WALLET_TO_WALLET("4");

    private String type;

    TransactionTypeEnum(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

    static public boolean isMember(String aValue) {
        TransactionTypeEnum[] transactionTypes = TransactionTypeEnum.values();
        for (TransactionTypeEnum aTransactionType : transactionTypes) {
            if (aTransactionType.type.equals(aValue)) {
                return true;
            }
        }
        return false;
    }
}
