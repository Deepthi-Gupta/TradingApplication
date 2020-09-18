import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TradeService {

    //<TradeID, Trade> map
    Map<String, List<Trade>> tradesMap = new HashMap<>();
    //<MaturityDate, List<TradeID>> map
    Map<Date, List<String>> maturityDateMap = new HashMap<>();

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    Date currentDate = sdf.parse(sdf.format(new Date()));

    public TradeService() throws ParseException {
    }

    public void addTrade(List<Trade> trades) throws ParseException {
        for (Trade trade : trades ) {
            addTrade(trade);
        }
    }

    public String addTrade(Trade trade) throws ParseException {
        //check if the new tradeId is present in the current set of trades
        String newTradeId = trade.getTradeId();
        List<Trade> tradesList = new ArrayList<>();
        if(tradesMap.containsKey(newTradeId)) {
            //check the version of the trade before insertion
            tradesList.addAll(tradesMap.get(newTradeId));

            //fetch all the available versions and get the max version.
            List<Integer> versions = getTradeVersions(tradesList);
            int maxVersion = Collections.max(versions);

            //compare the current version with the max version
            //case1: current version > max version
            if(trade.getVersion() > maxVersion) {
                if (checkMaturityDate(trade.getMaturityDate()) >= 0) {
                    tradesList.add(trade);
                } else {
                    return ("Trade is already Matured. Would not be adding into the trade store");
                }
                addTradesToMap(trade, tradesList);
            } //case2: Current version == max version
            else if (trade.getVersion() == maxVersion){
                tradesList.removeIf( trade1 -> {
                    int version = trade1.getVersion();
                    return version==maxVersion;
                });
                tradesList.add(trade);
                addTradesToMap(trade, tradesList);
            } //case3: Current version < max version
            else {
                //reject the trade
                return("Trade has been rejected current version= " + trade.getVersion() + " < than the maxVersion= " + maxVersion);
            }
        } //if its a new trade add the trade into the trade map
        else {
            if(checkMaturityDate(trade.getMaturityDate())>= 0) {
                tradesList.add(trade);
                addTradesToMap(trade, tradesList);
            } else {
                return ("Trade is already Matured. Would not be adding into the trade store");
            }
        }
        return ("success");
    }

    private void addTradesToMap(Trade trade, List<Trade> tradesList) {
        //add new trade to the trade map
        String newTradeId = trade.getTradeId();
        tradesMap.put(newTradeId, tradesList);

        //update the maturityDate map as well
        List<String> tradesIDs = new ArrayList<>();
        Date maturityDate = trade.getMaturityDate();
        if(maturityDateMap.containsKey(maturityDate)) {
            tradesIDs = maturityDateMap.get(maturityDate);
        }
            tradesIDs.add(newTradeId);
        maturityDateMap.put(maturityDate, tradesIDs);
    }

    //If there is no current date specified, then consider today's date as current date
    protected int checkMaturityDate(Date maturity) throws ParseException {
        Date maturityDate = sdf.parse(sdf.format(maturity));
        checkMaturityDate(maturityDate, currentDate);//
        //If maturity Date is lesser than the current date, then its not a valid trade
        return maturityDate.compareTo(currentDate);
    }

    //Overloading this method to make testing easier by passing current date
    public int checkMaturityDate(Date maturity, Date currentDate) throws ParseException {
        Date maturityDate = sdf.parse(sdf.format(maturity));
        //If maturity Date is lesser than the current date, then its not a valid trade
        return maturityDate.compareTo(currentDate);
    }

    private List<Integer> getTradeVersions(List<Trade> tradesList) {
        //available trade versions
        List<Integer> versionList = new ArrayList<>();
        for ( Trade t : tradesList ) {
            versionList.add(t.getVersion());
        }
        return versionList;
    }

    public void printAllTrades() {
        System.out.println("PRINTING THE TRADES");
        for (String tradeId : tradesMap.keySet() ) {
            List<Trade> tradesList = tradesMap.get(tradeId);
            for (Trade trade: tradesList) {
                System.out.println(trade.toString());
            }
        }
    }

    public List<Trade> getAllTradesList() {
        List<Trade> completeTradeList = new ArrayList<>();
        for (String tradeId : tradesMap.keySet() ) {
            List<Trade> tradesList = tradesMap.get(tradeId);
            completeTradeList.addAll(tradesList);
        }
        return completeTradeList;
    }

    public void setExpiredFlag() throws ParseException {
        for ( Date date: maturityDateMap.keySet()) {
            if(checkMaturityDate(date, currentDate) == 0) {
                List<String> tradesIdList = maturityDateMap.get(date);
                //for each of these tradeIds set expired flag to true
                for (String id : tradesIdList) {
                    List<Trade> trades = tradesMap.get(id);
                    for (Trade trade : trades) {
                        //set the expired flag to Y
                        trade.setExpired(true);
                    }
                    tradesMap.put(id, trades);
                }
            }
        }
    }
}
