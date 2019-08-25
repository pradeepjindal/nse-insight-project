package org.pra.nse

class GreetingInScala {
  def greet(): String = {
    val greeting = "Hello from SCALA!!!, i m going to call Java hello object..........."
    val greetingInJava = new GreetingInJava
    println(greetingInJava.greet())
    greeting
  }
}