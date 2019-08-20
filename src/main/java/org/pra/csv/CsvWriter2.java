package org.pra.csv;

import org.pra.bean.PraBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvWriter2 {
    public void write(List<PraBean> praBeans, String outputPathAndFileName) {
        File csvOutputFile = new File(outputPathAndFileName);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            praBeans.stream()
                    .map(praBean -> praBean.toCsvString())
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //assertTrue(csvOutputFile.exists());
    }
}
