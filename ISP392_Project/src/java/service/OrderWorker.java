package service;

import dal.OrdersDAO;
import dal.OrderDetailsDAO;
import dal.customerDAO;
import dal.debtDAO;
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
                String balanceAction = task.getBalanceAction(); // 👈 Lấy từ task

                OrdersDAO ordersDAO = new OrdersDAO();
                OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAO();

                // Insert đơn hàng
                int orderId = ordersDAO.insertOrder(order);

                if (orderId > 0) {
                    for (OrderDetails od : details) {
                        od.setOrderID(order);
                        od.getOrderID().setId(orderId);
                        orderDetailsDAO.insertOrderDetail(od);
                    }

                    // ✅ Cập nhật trạng thái đơn hàng thành SUCCESS
                    ordersDAO.updateOrderStatus(orderId, "SUCCESS");

                    // ✅ Nếu đơn hàng có nợ thì cộng vào balance của khách hàng
                    double debt = order.getAmount() - order.getPaidAmount();
                    if (Math.abs(debt) > 1e-6) {
                        customerDAO customersDAO = new customerDAO();
                        debtDAO debtNoteDAO = new debtDAO();
                        String note = "Order ID: " + orderId;
                        int storeId = order.getStoreId().getId();

                        if ("Export".equalsIgnoreCase(order.getType())) {
                            // Xuất kho, khách hàng nợ
                            if ("debt".equalsIgnoreCase(balanceAction)) {
                                customersDAO.updateCustomerDebt(order.getCustomerID().getId(), debt);
                                debtNoteDAO.insertDebtNote(order.getCustomerID().getId(), -debt, note, order.getUserID().getName(), storeId);
                            }
                        } else if ("Import".equalsIgnoreCase(order.getType())) {
                            // Nhập kho, cửa hàng nợ nhà cung cấp
                            if ("debt".equalsIgnoreCase(balanceAction)) {
                                customersDAO.updateCustomerDebt(order.getCustomerID().getId(), debt); // dùng lại customer nếu nhà cung cấp cùng bảng
                                debtNoteDAO.insertDebtNote(order.getCustomerID().getId(), -debt, note, order.getUserID().getName(), storeId);
                            }
                        }
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
