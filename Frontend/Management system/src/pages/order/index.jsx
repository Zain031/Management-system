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
import { Eye, SquarePlus, Trash2 } from "lucide-react";
import ButtonExport from "../../components/ButtonExport";
import {
  exportOrdersPerDate,
  exportOrdersPerMonth,
} from "../../redux/feature/exportSlice";
import SelectMonth from "../../components/SelectMonth";
import SelectYear from "../../components/SelectYear";
import months from "../../constans/months";
import SelectMonthOrDate from "../../components/SelectMonthOrDate";

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
      // await dispatch(
      //   payOrder({
      //     id_order: createdOrder.id_order,
      //     payment_method: "QRIS",
      //     amount: createdOrder.total_price,
      //   })
      // ).unwrap();
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
        <div className="flex justify-end gap-5">
          <label className="form-control max-w-xs">
            <SelectMonth
              selectedMonth={selectedMonth}
              setSelectedMonth={setSelectedMonth}
            />
          </label>
          <label className="form-control max-w-xs">
            <SelectYear
              selectedYear={selectedYear}
              setSelectedYear={setSelectedYear}
            />
          </label>

          <ButtonExport onPress={() => onButtonExportClick()}>
            Export Products
          </ButtonExport>
          <button
            onClick={onButtonAddClick}
            className="tooltip"
            data-tip="Add Product">
            <SquarePlus size={50} color="#00d12a" />
          </button>
        </div>
      }
      paging={paging}>
      <dialog id="export_modal" className="modal">
        <div className="modal-box">
          <form onSubmit={handleExport}>
            <SelectMonthOrDate
              exportPer={exportPer}
              setExportPer={setExportPer}
            />
            {exportPer === "month" && (
              <>
                <SelectMonth
                  selectedMonth={selectedMonth}
                  setSelectedMonth={setSelectedMonth}
                />
                <SelectYear
                  selectedYear={selectedYear}
                  setSelectedYear={setSelectedYear}
                />
              </>
            )}
            {exportPer === "date" && (
              <input
                type="date"
                className="input input-bordered input-ghost w-full my-2"
                value={selectedDate}
                onChange={(e) => setSelectedDate(e.target.value.split("T")[0])}
              />
            )}
            <button className="btn btn-outline btn-primary w-full">
              Submit
            </button>
          </form>
          <div className="modal-action">
            <form method="dialog">
              <button className="btn">Close</button>
            </form>
          </div>
        </div>
      </dialog>

      <dialog id="show_detail_modal" className="modal">
        <div className="modal-box">
          <h3 className="font-bold text-3xl">Order</h3>
          <section className="overflow-x-auto">
            <h3 className="font-bold text-lg">Order</h3>
            <Table
              arrayData={[selectedOrderForShowingDetail]}
              tableHeader={[
                "Id Order",
                "Customer Name",
                "Order Date",
                "Status",
                "Total",
              ]}
              excludeColumns={["details"]}
              customRender={{
                orderDate: (value) => (
                  <span
                    className="tooltip w-32 text-start"
                    data-tip="Order Date">
                    {value}
                  </span>
                ),
              }}
            />
          </section>
          <section className="overflow-x-auto">
            <h3 className="font-bold text-lg">Order details:</h3>
            <Table
              arrayData={selectedOrderForShowingDetail?.details}
              tableHeader={["Product name", "quantity", "price"]}
              excludeColumns={["id"]}
            />
          </section>
          <div className="modal-action">
            <form method="dialog">
              <button className="btn" onClick={onShowDetailClose}>
                Close
              </button>
            </form>
          </div>
        </div>
      </dialog>

      <dialog id="add_new_order_modal" className="modal">
        <div className="modal-box">
          <form onSubmit={handleAddNewOrder}>
            <input
              type="name"
              name="customerName"
              className="input input-bordered input-ghost w-full my-2"
              placeholder="Customer Name"
              value={customerNameForCreatingOrder}
              onChange={(e) => setCustomerNameForCreatingOrder(e.target.value)}
            />
            <section className="flex gap-2 justify-center items-center">
              <select
                name="customer_name"
                value={idProductForCreatingOrder}
                onChange={(e) => setIdProductForCreatingOrder(e.target.value)}
                className="select select-bordered w-full">
                <option selected value="">
                  Select Product Name
                </option>
                {products.map((product) => (
                  <option key={product.id_product} value={product.id_product}>
                    {product.product_name}
                  </option>
                ))}
              </select>
              <input
                type="number"
                name="quantity"
                value={quantityForCreatingOrder}
                onChange={(e) => setQuantityForCreatingOrder(e.target.value)}
                className="input input-bordered input-ghost w-full my-2"
                placeholder="Quantity"
              />
              <button
                type="button"
                className="tooltip"
                data-tip="Add Product"
                onClick={addProductForOrder}>
                <SquarePlus size={50} color="#00d12a" />
              </button>
            </section>
            <section>
              {cart.orderDetails.map((detail) => (
                <div
                  key={detail.id_order_details}
                  className="flex gap-2 justify-center items-center">
                  <span>{detail.product_name}</span>
                  <span>{detail.quantity}</span>
                  <button
                    type="button"
                    className="tooltip"
                    data-tip="Remove Product">
                    <Trash2 size={50} color="red" />
                  </button>
                </div>
              ))}
              {cart.totalPrice > 0 && (
                <div className="flex gap-2 justify-center items-center">
                  <span>Total:</span>
                  <span>{numberToIDR(cart.totalPrice)}</span>
                </div>
              )}
            </section>

            {isSuccessAddOrder ? (
              <button
                className="btn btn-outline btn-primary w-full"
                type="button"
                onClick={() => {
                  if (redirectURL) {
                    window.location.href = redirectURL;
                  } else {
                    alert("Redirect URL tidak ditemukan!");
                  }
                }}>
                PAY
              </button>
            ) : (
              <button
                className="btn btn-outline btn-primary w-full"
                type="submit">
                Submit
              </button>
            )}
          </form>
          <div className="modal-action">
            <form method="dialog">
              <button className="btn" onClick={onButtonCloseAddNewOrder}>
                Close
              </button>
            </form>
          </div>
        </div>
      </dialog>

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
