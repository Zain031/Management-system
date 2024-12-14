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
import {
  exportOrdersPerDate,
  exportOrdersPerMonth,
  generateReceipt,
} from "../../redux/feature/exportSlice";
import months from "../../constans/months";
import ExportModal from "../../components/ExportModal";
import ShowDetailOrderModal from "../../components/order/ShowDetailOrderModal";
import AddNewOrderModal from "../../components/order/AddNewOrderModal";
import HeaderAction from "../../components/order/HeaderAction";
import ButtonEye from "../../components/buttons/ButtonEye";
import removePaymentDataInLocalStorage from "../../../utils/removePaymentDataInLocalStorage";
import Swal from "sweetalert2";
import {
  dynamicValidation,
  isNotEmpty,
  validateName,
} from "../../../utils/validation/inputValidation";
import ButtonExport from "../../components/ButtonExport";

const Order = () => {
  const dispatch = useDispatch();
  const { orders, paging, cart, products, page, redirectURL } = useSelector(
    (state) => state.order
  );
  const [selectedCategory, setSelectedCategory] = useState("");
  const [exportPer, setExportPer] = useState("");
  const [selectedDate, setSelectedDate] = useState("");
  const [selectedStatus, setSelectedStatus] = useState("");
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
  const initialActions = async () => {
    await dispatch(fetchOrders({ page, size: 10 })).unwrap();
    await dispatch(fetchAllProducts()).unwrap();
    removePaymentDataInLocalStorage(orders);
  };
  const [isCustomerNameValid, setIsCustomerNameValid] = useState(true);
  const [isOrderDetailsValid, setIsOrderDetailsValid] = useState(true);
  const [isAmountValid, setIsAmountValid] = useState(true);
  const [payWith, setPayWith] = useState("");
  const [amount, setAmount] = useState(0);

  useEffect(() => {
    initialActions();
  }, [dispatch]);

  useEffect(() => {
    dispatch(
      fetchOrderByMonth({
        month: selectedMonth,
        year: selectedYear,
        status: selectedStatus,
      })
    );
  }, [selectedMonth, selectedYear, selectedStatus]);

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
    if (!idProductForCreatingOrder || !quantityForCreatingOrder) return;
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
    dispatch(clearProductCartState());
    setIsSuccessAddOrder(false);
    setCustomerNameForCreatingOrder("");
  };

  const handleAddNewOrder = async (e) => {
    e.preventDefault();
    const data = {
      customer_name: cart.customerName,
      order_details: cart.orderDetails.map((detail) => ({
        id_product: detail.id_product,
        quantity: Number(detail.quantity),
      })),
      amount,
    };
    const validation = [
      {
        name: "customer_name",
        validator: (name) => {
          return isNotEmpty(name) && validateName(name);
        },
        negativeImpact: () => setIsCustomerNameValid(false),
        positiveImpact: () => setIsCustomerNameValid(true),
      },
      {
        name: "order_details",
        validator: (details) => {
          return details.length > 0;
        },
        negativeImpact: () => setIsOrderDetailsValid(false),
        positiveImpact: () => setIsOrderDetailsValid(true),
      },
      {
        name: "amount",
        validator: (amount) => {
          return amount >= cart?.totalPrice;
        },
        negativeImpact: () => setIsAmountValid(false),
        positiveImpact: () => setIsAmountValid(true),
      },
    ];
    let allValid = dynamicValidation(validation, data);
    if (!allValid) {
      return;
    }
    document.getElementById("add_new_order_modal").close();
    const result = await Swal.fire(
      {
        title: "Are you sure?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, Add new order!",
        customClass: {
          popup: "highest-z-index",
        },
      },
      data
    );
    if (!result.isConfirmed) return;
    document.getElementById("add_new_order_modal").showModal();
    const Toast = Swal.mixin({
      toast: true,
      position: "top-end",
      showConfirmButton: false,
      timer: 1500,
      timerProgressBar: true,
      didOpen: (toast) => {
        toast.onmouseenter = Swal.stopTimer;
        toast.onmouseleave = Swal.resumeTimer;
      },
    });
    try {
      await dispatch(
        createOrder({
          order: {
            customer_name: data.customer_name,
            order_details: data.order_details,
          },
          payment_method: payWith,
          amount: data.amount,
        })
      ).unwrap();
      setIsSuccessAddOrder(true);
      resetStateForCreatingOrder();

      Toast.fire({
        icon: "success",
        title: "Material has been added",
      });
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
          selectedStatus={selectedStatus}
          setSelectedStatus={setSelectedStatus}
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
        isCustomerNameValid={isCustomerNameValid}
        isOrderDetailsValid={isOrderDetailsValid}
        isAmountValid={isAmountValid}
        payWith={payWith}
        setPayWith={setPayWith}
        amount={amount}
        setAmount={setAmount}
      />
      <Table
        page={page}
        arrayData={orders.map((item) => ({
          id: item.id_order,
          name: item.customer_name,
          orderDate: formatDate(item.order_date.split(" ")[0]),
          status: item.status,
          total: numberToIDR(item.total_price),
          method: item?.method,
          qrisResponse: item.link_qris,
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
            <ButtonEye
              className="tooltip"
              data-tip="Details"
              onClick={() => onButtonShowDetailClick(item.id)}
            />
            {item.status === "COMPLETED" && (
              <ButtonExport
                className="tooltip"
                data-tip="Export"
                onClick={() => dispatch(generateReceipt(item.id))}>
                Export Receipt
              </ButtonExport>
            )}
            {item.status.toLowerCase() === "pending" &&
              item.method &&
              item.method.toLowerCase() === "qris" && (
                <button
                  className="btn btn-outline btn-primary px-8"
                  type="button"
                  onClick={() => {
                    console.log("Link QRIS", item);
                    const redirectURL = JSON.parse(
                      item.qrisResponse
                    ).redirect_url;
                    if (redirectURL) {
                      window.open(redirectURL, "_blank");
                    } else {
                      alert("Redirect URL tidak ditemukan!");
                    }
                  }}>
                  PAY
                </button>
              )}
          </td>
        )}
        notFoundMessage="No orders found"
        excludeColumns={["order_details", "id", "qrisResponse", "method"]}
      />
    </PaginationLayout>
  );
};

export default Order;
