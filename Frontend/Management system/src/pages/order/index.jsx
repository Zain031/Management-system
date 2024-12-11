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
import { exportOrdersPerMonth } from "../../redux/feature/exportSlice";
import SelectMonth from "../../components/SelectMonth";
import SelectYear from "../../components/SelectYear";
import months from "../../constans/months";

const Order = () => {
  const dispatch = useDispatch();
  const { orders, paging } = useSelector((state) => state.order);
  const [search, setSearch] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");

  const currentTime = new Date();
  const [selectedMonth, setSelectedMonth] = useState(
    months[currentTime.getMonth()]
  );
  const [selectedYear, setSelectedYear] = useState(currentTime.getFullYear());

  useEffect(() => {
    dispatch(fetchOrders({ page: 1, size: 10 }));
  }, []);
  useEffect(() => {
    dispatch(fetchOrderByMonth({ month: selectedMonth, year: selectedYear }));
  }, [selectedMonth, selectedYear]);

  useEffect(() => {
    console.log(selectedMonth, selectedYear);
  }, [selectedMonth, selectedYear]);

  const handleEdit = (id) => {
    console.log("Edit data dengan ID:", id);
  };

  const onButtonDeleteClick = (id) => {
    console.log("Hapus data dengan ID:", id);
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

  return (
    <PaginationLayout
      headerTitle={"Order"}
      pagingData={orders}
      search={search}
      setSearch={setSearch}
      onButtonAddClick={onButtonAddClick}
      exportData={exportData}
      onButtonEditClick={handleEdit}
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

          <ButtonExport onPress={() => dispatch(exportOrdersPerMonth())}>
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
              onClick={() => onButtonAddClick(item.id)}>
              <Eye size={28} color="#00d15b" />
            </button>
          </td>
        )}
        page={paging.page}
        notFoundMessage="No orders found"
        excludeColumns={["order_details", "id"]}
      />
    </PaginationLayout>
  );
};

export default Order;
