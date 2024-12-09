import { useDispatch } from "react-redux";
import { useEffect, useState } from "react";
import ChartCompose from "../../components/ChartCompose";
import { Select, SelectItem } from "@nextui-org/select";
import { Card } from "@nextui-org/card";
import { fetchPurchasesPerMonthInLastOneYear } from "../../redux/feature/PurchaseSlice";
// import {
//   fetchDashboard,
//   fetchDashboardStatus,
// } from "../redux/feature/dashboardSlice";
// import ChartCompose from "../../components/ChartCompose";

const AdminDashboard = () => {
  const dispatch = useDispatch();
  //   const { (totalIncome || 20000), (totalIncome || 20000)Month, registerCount } = useSelector(
  //     (state) => state.dashboard
  //   );
  const [filterYear, setFilterYear] = useState(new Date().getFullYear());
  const [filterMonth, setFilterMonth] = useState(new Date().getMonth() + 1);

  useEffect(() => {
    dispatch(fetchPurchasesPerMonthInLastOneYear())
      .unwrap()
      .then((data) => {
        console.log("Fetched purchases:", data);
      })
      .catch((error) => {
        console.error("Error fetching purchases:", error);
      });

    dispatch(
      fetchPurchasesPerMonthInLastOneYear({
        startDate: "2023-01-01",
        endDate: "2024-12-31",
      })
    )
      .unwrap()
      .then((data) => {
        console.log("Fetched purchases:", data);
      })
      .catch((error) => {
        console.error("Error fetching purchases:", error);
      });
  }, []);

  //   useEffect(() => {
  //     dispatch(fetchDashboardStatus({ month: filterMonth, year: filterYear }));
  //     dispatch(fetchDashboard({ month: filterMonth, year: filterYear }));
  //   }, []);

  //   useEffect(() => {
  //     dispatch(fetchDashboardStatus({ month: filterMonth, year: filterYear }));
  //     dispatch(fetchDashboard({ month: filterMonth, year: filterYear }));
  //   }, [filterMonth, filterYear]);

  //   useEffect(() => {
  //     console.log(filterMonth, filterYear);
  //   }, [filterMonth, filterYear]);
  return (
    <section className="flex flex-col gap-6">
      <section className="absolute top-2 right-10 flex gap-6 w-1/4">
        <Select
          label="Year"
          selectedKeys={[String(filterYear)]}
          variant="bordered"
          onChange={(e) => setFilterYear(e.target.value)}>
          {Array.from({ length: 10 }, (_, index) => (
            <SelectItem key={String(new Date().getFullYear() - index)}>
              {String(new Date().getFullYear() - index)}
            </SelectItem>
          ))}
        </Select>

        <Select
          label="Month"
          selectedKeys={[String(filterMonth)]}
          variant="bordered"
          onChange={(e) => setFilterMonth(e.target.value)}>
          {Array.from({ length: 12 }, (_, index) => (
            <SelectItem key={String(index + 1)}>{String(index + 1)}</SelectItem>
          ))}
        </Select>
      </section>
      <h2 className="text-4xl text-mainSoil text-center mb-6 font-extrabold">
        Admin Dashboard
      </h2>
      <section className="flex gap-6 justify-center items-center mb-12">
        <Card className="w-[31%] min-h-[200px]" isPressable isHoverable>
          <section className="w-full h-[200px] bg-green-100 flex justify-center items-center flex-col">
            <section>
              <h3 className="text-start font-semibold text-mainSoil">
                Total Company Income
              </h3>
              <p className="text-start font-extrabold text-mainGreen text-5xl">
                {new Intl.NumberFormat("id-ID", {
                  style: "currency",
                  currency: "IDR",
                }).format(200000)}
              </p>
            </section>
          </section>
        </Card>
        <Card className="w-[31%] min-h-[200px]" isPressable isHoverable>
          <section className="w-full h-[200px] bg-blue-100 flex justify-center items-center flex-col">
            <section>
              <h3 className="text-start font-semibold text-mainSoil">
                Total Income ({filterMonth}/{filterYear})
              </h3>
              <p className="text-start font-extrabold text-mainGreen text-5xl">
                Rp {"200000".toLocaleString("id-ID")}
              </p>
            </section>
          </section>
        </Card>
        <Card className="w-[31%] min-h-[200px]" isPressable isHoverable>
          <section className="w-full h-[200px] bg-yellow-100 flex justify-center items-center flex-col">
            <section>
              <h3 className="text-start font-semibold text-mainSoil">
                New Customers ({filterMonth}/{filterYear})
              </h3>
              <p className="text-start font-extrabold text-mainGreen text-5xl">
                {"200000".toLocaleString("id-ID")} customers
              </p>
            </section>
          </section>
        </Card>
      </section>
      <section className="flex gap-4">
        <section className="bg-neutral-50 w-full rounded-xl py-4 px-6">
          <ChartCompose />
        </section>
      </section>
    </section>
  );
};

export default AdminDashboard;
