import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import PaginationLayout from "../../components/HOC/PaginationLayout";
import Table from "../../components/Table";
import { useEffect } from "react";
import { fetchOrderByMonth, fetchOrders } from "../../redux/feature/orderSlice";
import { numberToIDR } from "../../../utils/numberFormatter/numberToIDR";
import formatDate from "../../../utils/formatDate";
import { Eye, SquarePlus } from "lucide-react";
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
  const { orders, paging } = useSelector((state) => state.order);
  const [search, setSearch] = useState("");
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
  useEffect(() => {
    dispatch(fetchOrders({ page: 1, size: 10 }));
  }, []);
  useEffect(() => {
    dispatch(fetchOrderByMonth({ month: selectedMonth, year: selectedYear }));
  }, [selectedMonth, selectedYear]);

  useEffect(() => {
    console.log(selectedMonth, selectedYear);
  }, [selectedMonth, selectedYear]);

  const handleShowDetail = (id) => {
    console.log("Edit data dengan ID:", id);
  };

  const onButtonShowDetailClick = (id) => {
    const selectedOrder = orders.find((order) => order.id_order === id);
    console.log(selectedOrder);

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

  const handleDelete = (id) => {
    console.log("Hapus data dengan ID:", id);
  };

  const onButtonAddClick = (id) => {
    console.log("Tambah data", id);
  };

  const exportData = () => {
    console.log("Export data");
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

  return (
    <PaginationLayout
      headerTitle={"Order"}
      pagingData={orders}
      search={search}
      setSearch={setSearch}
      onButtonAddClick={onButtonAddClick}
      exportData={exportData}
      onButtonEditClick={handleShowDetail}
      onButtonDeleteClick={handleDelete}
      selectedCategory={selectedCategory}
      setSelectedCategory={setSelectedCategory}
      page={paging.page}
      setPage={(page) =>
        dispatch(fetchOrders({ page, size: 10, search, selectedCategory }))
      }
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
      dispatch={dispatch}
      paging={paging}>
      <Table
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
        page={paging.page}
        notFoundMessage="No orders found"
        excludeColumns={["order_details", "id"]}
      />
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
                "Product Name",
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
              tableHeader={["Id Order Details", "name", "quantity", "price"]}
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
    </PaginationLayout>
  );
};

export default Order;
