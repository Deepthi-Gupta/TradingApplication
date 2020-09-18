import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TradeServiceTest {

    List<Trade> tradeList = new ArrayList<>();
    TradeService service = new TradeService();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String format = dateFormat.format(new Date());

    TradeServiceTest() throws ParseException {
    }

    @Test
    void addTrade() throws ParseException {

        tradeList.add(new Trade("T2",
                1,
                "CP-1",
                "B1",
                dateFormat.parse("20/05/2021"),
                dateFormat.parse("14/3/2015"),
                false
        ));
        tradeList.add(new Trade("T3",
                1,
                "CP-1",
                "B1",
                dateFormat.parse("20/05/2014"),
                dateFormat.parse(format),
                true
        ));
        //Add these list of trades into the trade store
        service.addTrade(tradeList);
        service.printAllTrades();
        assertEquals(1, service.getAllTradesList().size());
    }

    @Test
    void testAddTrade() throws ParseException {
        //Success scenario

        Trade trade1 = new Trade("T2",
                2,
                "CP-2",
                "B1",
                dateFormat.parse("20/05/2021"),
                dateFormat.parse(format),
                false
        );
        service.printAllTrades();
        assertEquals(service.addTrade(trade1), "success" );

        //Scenario 1.1: Now try adding version 1 again after version 2, it should error out.
        Trade trade2 = new Trade("T2",
                1,
                "CP-1",
                "B1",
                dateFormat.parse("20/05/2021"),
                dateFormat.parse("14/3/2015"),
                false
        );
        assertEquals(service.addTrade(trade2), "Trade has been rejected current version= 1 < than the maxVersion= 2" );

        //Scenario 1.2(Now try adding version 2(max version of existing trade), it should replace the existing trade out)
        Trade trade3 = new Trade("T2",
                2,
                "CP-2",
                "B1.2",
                dateFormat.parse("20/05/2021"),
                dateFormat.parse("14/3/2015"),
                false
        );
        service.printAllTrades();
        assertEquals(service.addTrade(trade3), "success");

        //Scenario 2: Store should not allow the trade which has less maturity date than today date.
        Trade trade4 = new Trade("T1",
                1,
                "CP-1",
                "B1",
                dateFormat.parse("20/05/2020"),
                dateFormat.parse(format),
                false
        );

        assertEquals(service.addTrade(trade4), "Trade is already Matured. Would not be adding into the trade store" );
    }

    @Test
    void setExpiredFlag() throws ParseException {
        //Scenario 1.2(Now try adding version 2(max version of existing trade), it should replace the existing trade out)
        Trade trade = new Trade("T2",
                2,
                "CP-2",
                "B1.2",
                dateFormat.parse("21/12/2021"),
                dateFormat.parse("14/3/2015"),
                false
        );

        service.addTrade(trade);
        Date currentDay = dateFormat.parse("21/12/2021");
        service.setCurrentDate(currentDay);
        System.out.println("Trade before setting the expire flag to true");
        service.printAllTrades();
        assertFalse(service.getAllTradesList().get(0).isExpired());
        service.setExpiredFlag();
        System.out.println("Trade after setting the expire flag to true");
        service.printAllTrades();
        assertTrue(service.getAllTradesList().get(0).isExpired());
    }
}