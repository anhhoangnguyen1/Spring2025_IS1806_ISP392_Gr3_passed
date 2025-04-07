/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author THC
 */

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderQueue {
    private static final BlockingQueue<OrderTask> queue = new LinkedBlockingQueue<>();

    public static void addOrder(OrderTask task) {
        queue.offer(task);
    }

    public static OrderTask take() throws InterruptedException {
        return queue.take(); // chờ nếu rỗng
    }
}
