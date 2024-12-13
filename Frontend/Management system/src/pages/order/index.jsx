import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import PaginationLayout from "../../components/HOC/PaginationLayout";
import Table from "../../components/Table";
import { useEffect } from "react";
import {
  addProductToCart,
  clearProductCartState,
  createOrder,
  fetchAllProducts,
  fetchOrderByMonth,
  fetchOrders,
  setCustomerNameForCart,
  setPage,
} from "../../redux/feature/orderSlice";
import { numberToIDR } from "../../../utils/numberFormatter/numberToIDR";
import formatDate from "../../../utils/formatDate";
import { Eye } from "lucide-react";
import {
  exportOrdersPerDate,
  exportOrdersPerMonth,
} from "../../redux/feature/exportSlice";
import months from "../../constans/months";
import ExportModal from "../../components/ExportModal";
import ShowDetailOrderModal from "../../components/order/ShowDetailOrderModal";
import AddNewOrderModal from "../../components/order/AddNewOrderModal";
import HeaderAction from "../../components/order/HeaderAction";

const Order = () => {
  const dispatch = useDispatch();
  const { orders, paging, cart, products, page, redirectURL } = useSelector(
    (state) => state.order
  );
  const [selectedCategory, setSelectedCategory] = useState("");
  const [exportPer, setExportPer] = useState("");
  const [selectedDate, setSelectedDate] = useState("");

  const currentTime = new Date();
  const [selectedMonth, setSelectedMonth] = useState(
    months[currentTime.getMonth()]
  );
  const [selectedYear, setSelectedYear] = useState(currentTime.getFullYear());
  const [selectedOrderForShowingDetail, setSelectedOrderForShowingDetail] =
    useState([]);

  const [customerNameForCreatingOrder, setCustomerNameForCreatingOrder] =
    useState("");
  const [idProductForCreatingOrder, setIdProductForCreatingOrder] =
    useState("");
  const [quantityForCreatingOrder, setQuantityForCreatingOrder] = useState(1);

  const [isSuccessAddOrder, setIsSuccessAddOrder] = useState(false);

  useEffect(() => {
    dispatch(fetchOrders({ page, size: 10 }));
    dispatch(fetchAllProducts());
  }, [dispatch]);

  useEffect(() => {
    dispatch(fetchOrderByMonth({ month: selectedMonth, year: selectedYear }));
  }, [selectedMonth, selectedYear]);

  useEffect(() => {
    dispatch(fetchOrders({ page, size: 10 }));
  }, [page]);

  const onButtonShowDetailClick = (id) => {
    const selectedOrder = orders.find((order) => order.id_order === id);
    if (selectedOrder) {
      setSelectedOrderForShowingDetail({
        id: selectedOrder.id_order,
        name: selectedOrder.customer_name,
        orderDate: formatDate(selectedOrder.order_date.split(" ")[0]),
        status: selectedOrder.status,
        total: numberToIDR(selectedOrder.total_price),
        details: selectedOrder.order_details.map((detail) => ({
          id: detail.id_order_detail,
          name: detail.product_name,
          quantity: detail.quantity,
          price: numberToIDR(detail.price_per_unit),
        })),
      });
    } else {
      console.error("Order not found for the given ID:", id);
    }
    document.getElementById("show_detail_modal").showModal();
  };

  const onShowDetailClose = () => {
    setSelectedOrderForShowingDetail([]);
  };

  const onButtonExportClick = () => {
    document.getElementById("export_modal").showModal();
  };

  const handleExport = (e) => {
    e.preventDefault();
    if (exportPer == "date") {
      dispatch(exportOrdersPerDate({ date: selectedDate }));
    } else {
      dispatch(
        exportOrdersPerMonth({ month: selectedMonth, year: selectedYear })
      );
    }
  };

  const onButtonAddClick = () => {
    document.getElementById("add_new_order_modal").showModal();
  };

  const resetStateForCreatingOrder = () => {
    setIdProductForCreatingOrder("");
    setQuantityForCreatingOrder(1);
  };

  const addProductForOrder = async () => {
    if (!idProductForCreatingOrder || !quantityForCreatingOrder) {
      console.error("Product ID and quantity are required.");
      return;
    }
    dispatch(setCustomerNameForCart(customerNameForCreatingOrder));
    dispatch(
      addProductToCart({
        id: idProductForCreatingOrder,
        quantity: quantityForCreatingOrder,
      })
    );
    resetStateForCreatingOrder();
  };

  const onButtonCloseAddNewOrder = () => {
    document.getElementById("add_new_order_modal").close();
  };

  const handleAddNewOrder = async (e) => {
    e.preventDefault();
    const data = {
      customer_name: cart.customerName,
      order_details: cart.orderDetails.map((detail) => ({
        id_product: detail.id_product,
        quantity: Number(detail.quantity),
      })),
    };
    try {
      await dispatch(createOrder({ order: data })).unwrap();
      dispatch(clearProductCartState());
      setIsSuccessAddOrder(true);
      resetStateForCreatingOrder();
      setCustomerNameForCreatingOrder("");
    } catch (error) {
      console.error(error);
      setIsSuccessAddOrder(false);
    }
  };

  return (
    <PaginationLayout
      headerTitle={"Order"}
      pagingData={orders}
      selectedCategory={selectedCategory}
      setSelectedCategory={setSelectedCategory}
      page={page}
      dispatch={dispatch}
      setPage={setPage}
      headerRenderAction={
        <HeaderAction
          selectedCategory={selectedCategory}
          setSelectedCategory={setSelectedCategory}
          selectedMonth={selectedMonth}
          setSelectedMonth={setSelectedMonth}
          selectedYear={selectedYear}
          setSelectedYear={setSelectedYear}
          onButtonExportClick={onButtonExportClick}
          onButtonAddClick={onButtonAddClick}
        />
      }
      paging={paging}>
      <ExportModal
        handleExport={handleExport}
        exportPer={exportPer}
        setExportPer={setExportPer}
        selectedMonth={selectedMonth}
        setSelectedMonth={setSelectedMonth}
        selectedYear={selectedYear}
        setSelectedYear={setSelectedYear}
        selectedDate={selectedDate}
        setSelectedDate={setSelectedDate}
      />
      <ShowDetailOrderModal
        onShowDetailClose={onShowDetailClose}
        selectedOrderForShowingDetail={selectedOrderForShowingDetail}
      />
      <AddNewOrderModal
        handleAddNewOrder={handleAddNewOrder}
        setCustomerNameForCreatingOrder={setCustomerNameForCreatingOrder}
        customerNameForCreatingOrder={customerNameForCreatingOrder}
        products={products}
        idProductForCreatingOrder={idProductForCreatingOrder}
        setIdProductForCreatingOrder={setIdProductForCreatingOrder}
        quantityForCreatingOrder={quantityForCreatingOrder}
        setQuantityForCreatingOrder={setQuantityForCreatingOrder}
        addProductForOrder={addProductForOrder}
        cart={cart}
        isSuccessAddOrder={isSuccessAddOrder}
        redirectURL={redirectURL}
        onButtonCloseAddNewOrder={onButtonCloseAddNewOrder}
      />
      <Table
        page={page}
        arrayData={orders.map((item) => ({
          id: item.id_order,
          name: item.customer_name,
          orderDate: formatDate(item.order_date.split(" ")[0]),
          status: item.status,
          total: numberToIDR(item.total_price),
        }))}
        tableHeader={[
          "Customer Name",
          "Order Date",
          "status",
          "Total",
          "Action",
        ]}
        renderActions={(item) => (
          <td className="flex gap-8">
            <button
              className="tooltip"
              data-tip="Details"
              onClick={() => onButtonShowDetailClick(item.id)}>
              <Eye size={28} color="#00d15b" />
            </button>
          </td>
        )}
        notFoundMessage="No orders found"
        excludeColumns={["order_details", "id"]}
      />
    </PaginationLayout>
  );
};

export default Order;
