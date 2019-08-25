package org.pra.nse.csv.writer;

import org.pra.nse.bean.PraBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class CsvWriter2 {
    public void write(List<PraBean> praBeans, String outputPathAndFileName) {
        File csvOutputFile = new File(outputPathAndFileName);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            praBeans.stream()
                    .map(PraBean::toCsvString)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //assertTrue(csvOutputFile.exists());
    }
}
