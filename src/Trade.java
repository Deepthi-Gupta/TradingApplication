/**
 *
 * This is a Trade pojo class that defines all the trade attributes
 */

import java.util.Date;

public class Trade {
    private String tradeId;
    private int version;
    private String counterPartyId;
    private String bookId;
    private Date maturityDate;
    private Date createdDate;
    private boolean expired;

    public Trade(String tradeId, int version, String counterPartyId, String bookId, Date maturityDate, Date createdDate, boolean expired) {
        this.tradeId = tradeId;
        this.version = version;
        this.counterPartyId = counterPartyId;
        this.bookId = bookId;
        this.maturityDate = maturityDate;
        this.createdDate = createdDate;
        this.expired = expired;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCounterPartyId() {
        return counterPartyId;
    }

    public void setCounterPartyId(String counterPartyId) {
        this.counterPartyId = counterPartyId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "tradeId : " + this.tradeId + "\t" +
                "version : " + this.version + "\t" +
                "counterPartyId : " + this.counterPartyId + "\t" +
                "bookId : " + this.bookId + "\t" +
                "maturityDate : " + this.maturityDate + "\t" +
                "createdDate : " + this.createdDate + "\t" +
                "expired; : " + (this.expired? "Y" : "N" )+ "\n";
    }
}
