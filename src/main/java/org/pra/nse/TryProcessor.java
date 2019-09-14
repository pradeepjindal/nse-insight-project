package org.pra.nse;

import org.pra.nse.bean.in.EqnBean;
import org.pra.nse.csv.read.EqnCsvReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


//@Component
public class TryProcessor implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(TryProcessor.class);
    private EqnCsvReader eqnCsvReader;

    public TryProcessor(EqnCsvReader eqnCsvReader) {
        this.eqnCsvReader = eqnCsvReader;
    }

    public void process(LocalDate downloadFromDate, LocalDate processForDate) throws IOException {
        List<EqnBean> beans = eqnCsvReader.readCsv(null);
        AtomicInteger ctr = new AtomicInteger();

        int boughtCounter = 0;
        double[] boughtArray = new double[5];

        double[] closeVector = new double[10];
        for(int i=0; i<beans.size(); i++) {
            closeVector[(i%10)] = beans.get(i).getClosePrice();
            if(i<10) {
                System.out.println("skipping");
            } else {
                double avgPrice = Arrays.stream(closeVector).average().getAsDouble();
                double d = calcAvgBoughtPrice(boughtArray) - (calcAvgBoughtPrice(boughtArray)/100);
                //System.out.println("nxt buy = " + d + " | low" + beans.get(i).getClosePrice());
                if(boughtCounter == 0) {
                    System.out.println("bought, avg:" + avgPrice);
                    boughtCounter++;
                    boughtArray[boughtCounter] = avgPrice - (avgPrice/100);
                } else if(boughtCounter < 5
                        && beans.get(i).getLowPrice() < calcAvgBoughtPrice(boughtArray) - (calcAvgBoughtPrice(boughtArray)/100)) {
                    System.out.println("bought, avg:" + avgPrice);
                    boughtArray[boughtCounter] = avgPrice - (avgPrice/100);
                    boughtCounter++;
                }
                if(boughtCounter > 0) {
                    double avgBoughtPrice = Arrays.stream(boughtArray).filter(price -> price>0).average().getAsDouble();
                    System.out.println("avg pirce = " + avgBoughtPrice + " | high" + beans.get(i).getHighPrice());
                    if(beans.get(i).getHighPrice() > avgBoughtPrice + (avgBoughtPrice/100)) {
                        System.out.println("==========sold all, avgBought:" + avgBoughtPrice
                                + " | avgSale:" + avgBoughtPrice + (avgBoughtPrice/100)
                                + " | High:" + beans.get(i).getHighPrice());
                    }
                }
            }
        }
    }

    private double calcAvgBoughtPrice(double[] boughtArray) {
        if(Arrays.stream(boughtArray).filter(price -> price>0).average().isPresent()) {
            return Arrays.stream(boughtArray).filter(price -> price>0).average().getAsDouble();
        } else {
            return 0d;
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        LOGGER.info("Pradeep Processor | ============================== | Kicking");
		try {
            process(ApCo.DOWNLOAD_FROM_DATE, LocalDate.now());
		} catch(Exception e) {
			e.printStackTrace();
		}
        LOGGER.info("Pradeep Processor | ============================== | Finished");
    }
}
