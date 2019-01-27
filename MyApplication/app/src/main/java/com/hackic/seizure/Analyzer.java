package com.hackic.seizure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;

public class Analyzer {

  public static double get_avg(Queue<Double> floatqueue) {
    double total = 0;
    for (double num : floatqueue) {
      total += Math.abs(num);
    }
    double avg = total / floatqueue.size();
    return avg;
  }
  public static double get_avg_list( List<Double> floatArray) {
    double total = 0;
    for (double num : floatArray) {
      total += Math.abs(num);
    }
    double avg = total / floatArray.size();
    return avg;
  }


  public static double square(double num) {
    return Math.pow(num, 2);
  }





  public static Boolean are_you_ok(Queue<Double> accx, Queue<Double> accy, Queue<Double> accz, List<Double> history_of_avg_acc) {
    double curr_avg_accx = get_avg(accx);
    double curr_avg_accy = get_avg(accy);
    double curr_avg_accz = get_avg(accz);

    double square_curr_avg_accx = square(curr_avg_accx);
    double square_curr_avg_accy = square(curr_avg_accy);
    double square_curr_avg_accz = square(curr_avg_accz);

    double current_avg_acc_magnitude = Math.sqrt(square_curr_avg_accx + square_curr_avg_accy + square_curr_avg_accz);

    double history_mean_avg = get_avg_list(history_of_avg_acc);


    Boolean okay = true;
    if (current_avg_acc_magnitude > 3 * history_mean_avg) {
      okay = false;
    }


    if (history_of_avg_acc.size()>20){
      history_of_avg_acc.remove(0);
    }
    history_of_avg_acc.add(current_avg_acc_magnitude);


    return okay;
  }




  public static void main(String[] args){
    List<Double> hist= new ArrayList<Double>();
    Queue<Double> accx = new LinkedList<Double>();
    double dummy =5.1;
    accx.add(dummy);

    for (int i = 0; i < 30; i++) {

      Boolean yo=are_you_ok(accx,accx,accx,hist);
      System.out.println(yo);
    }


    for (double arr : hist) {
      System.out.println(arr);
    }

  }





}
