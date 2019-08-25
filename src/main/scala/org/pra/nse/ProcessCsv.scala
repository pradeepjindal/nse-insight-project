package org.pra.nse

class ProcessCsv {

  def process: Unit = {
    var greetingInJava = new GreetingInJava
    println(greetingInJava.greet())
    println("Month, Income, Expenses, Profit")
    val bufferedSource = scala.io.Source.fromFile("C:/Users/prajinda/pra-nse-fno/fo16AUG2019bhav.csv")
    for (line <- bufferedSource.getLines) {
      val cols = line.split(",").map(_.trim).filter(_.contains("FUTSTK"))
      // do whatever you want with the columns here
      println(s"${cols(0)} | ${cols(1)} | ${cols(2)} | ${cols(3)} | ${cols(4)} | ${cols(5)} | ${cols(6)} | ${cols(7)} | ${cols(8)} | ${cols(9)} | ${cols(10)} | ${cols(11)} | ${cols(12)} | ${cols(13)}")
    }
    bufferedSource.close
  }

}
