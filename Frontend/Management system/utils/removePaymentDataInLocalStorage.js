const removePaymentDataInLocalStorage = (orderHistories = []) => {
  orderHistories.map((order) => {
    if (order.status == "COMPLETED") {
      localStorage.removeItem(`payment-${order.id_order}`);
    }
  });
};

export default removePaymentDataInLocalStorage;
