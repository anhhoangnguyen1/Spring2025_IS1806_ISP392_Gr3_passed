package service;

import dal.OrdersDAO;
import dal.OrderDetailsDAO;
import entity.Orders;
import entity.OrderDetails;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OrderWorker extends Thread {

    private static boolean running = false;
    private static final Map<Integer, String> processedStatus = new ConcurrentHashMap<>();

    public static void startWorker() {
        if (!running) {
            OrderWorker worker = new OrderWorker();
            worker.start();
            running = true;
        }
    }

    public static void clearProcessedOrder(int userId) {
        processedStatus.remove(userId);
    }

    public static String getStatus(int userId) {
        return processedStatus.getOrDefault(userId, "processing");
    }

    @Override
    public void run() {
        while (true) {
            try {
                OrderTask task = OrderQueue.take();
                Orders order = task.getOrder();
                List<OrderDetails> details = task.getOrderDetails();

                OrdersDAO ordersDAO = new OrdersDAO();
                OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO();

                // Insert đơn hàng, lấy ra orderId vừa được tạo
                int orderId = ordersDAO.insertOrder(order);

                if (orderId > 0) {
                    // Gán order ID vào từng OrderDetails trước khi lưu
                    for (OrderDetails od : details) {
                        od.setOrderID(order); // hoặc set Order chứa ID
                        od.getOrderID().setId(orderId); // đảm bảo có ID đúng
                        orderDetailsDAO.insertOrderDetail(od);
                    }

                    processedStatus.put(order.getUserID().getId(), "done");
                } else {
                    processedStatus.put(order.getUserID().getId(), "error");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
