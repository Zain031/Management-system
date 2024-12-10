import { useDispatch, useSelector } from "react-redux";
import { useEffect, useState } from "react";
import { Select, SelectItem } from "@nextui-org/select";
import { Card } from "@nextui-org/card";
import { fetchDashboardData } from "../../redux/feature/dashboardSlice";
import DynamicGroupBarChart from "../../components/DynamicGroupBarChart";
import { numberToIDR } from "../../../utils/numberFormatter/numberToIDR";
import yearOptionRange from "../../../utils/yearOptionRange";

const AdminDashboard = () => {
  const dispatch = useDispatch();
  const {
    monthlyPerformance,
    totalProfit,
    year,
    totalOrder,
    totalPriceOrder,
    totalPriceInventory,
    loading,
  } = useSelector((state) => state.dashboard);

  const [filterYear, setFilterYear] = useState(new Date().getFullYear());

  useEffect(() => {
    dispatch(fetchDashboardData(2024));
  }, []);

  useEffect(() => {
    dispatch(fetchDashboardData(filterYear));
  }, [filterYear]);

  return (
    <section className="flex flex-col gap-6">
      <section className="absolute top-2 right-10 flex gap-6 w-1/4">
        <Select
          label="Filter by year"
          selectedKeys={[filterYear]}
          defaultValue={filterYear}
          variant="bordered"
          onChange={(e) => setFilterYear(e.target.value)}>
          {yearOptionRange().map((year) => (
            <SelectItem key={String(year)} textValue={String(year)}>
              {year}
            </SelectItem>
          ))}
        </Select>
      </section>
      <h2 className="text-4xl text-mainSoil text-center mb-6 font-extrabold">
        Admin Dashboard
      </h2>
      <section className="flex gap-6 justify-center items-center mb-12">
        <Card className="w-[24%] min-h-[200px]" isPressable isHoverable>
          <section className="w-full h-[200px] bg-[#82ca9d] flex justify-center items-center flex-col">
            <section>
              <h3 className="text-start font-semibold text-mainSoil">
                Total Income
              </h3>
              <p className="text-start font-extrabold text-mainGreen text-3xl">
                Rp. {new Intl.NumberFormat("id-ID").format(totalPriceOrder)}
              </p>
            </section>
          </section>
        </Card>
        <Card className="w-[24%] min-h-[200px]" isPressable isHoverable>
          <section className="w-full h-[200px] bg-[#8884d8] flex justify-center items-center flex-col">
            <section>
              <h3 className="text-start font-semibold text-mainSoil">
                Total Profit ({filterYear})
              </h3>
              <p className="text-start font-extrabold text-mainGreen text-3xl">
                Rp. {totalProfit.toLocaleString("id-ID")}
              </p>
            </section>
          </section>
        </Card>
        <Card className="w-[24%] min-h-[200px]" isPressable isHoverable>
          <section className="w-full h-[200px] bg-[#FF8000] flex justify-center items-center flex-col">
            <section>
              <h3 className="text-start font-semibold text-mainSoil">
                Total Order ({filterYear})
              </h3>
              <p className="text-start font-extrabold text-mainGreen text-3xl">
                {totalOrder.toLocaleString("id-ID")} Order
              </p>
            </section>
          </section>
        </Card>
        <Card className="w-[24%] min-h-[200px]" isPressable isHoverable>
          <section className="w-full h-[200px] bg-[#FA4032] flex justify-center items-center flex-col">
            <section>
              <h3 className="text-start font-semibold text-mainSoil">
                Total Purchase ({filterYear})
              </h3>
              <p className="text-start font-extrabold text-mainGreen text-3xl">
                Rp. {totalPriceInventory.toLocaleString("id-ID")}
              </p>
            </section>
          </section>
        </Card>
      </section>
      <section className="flex gap-4">
        <DynamicGroupBarChart
          data={monthlyPerformance}
          chartTitle="Monthly Performance"
          XAxisDataKey="month"
          formatter={numberToIDR}
          bars={[
            {
              dataKey: "profit",
              name: "Profit",
              fill: "#8884d8",
              stackId: "group1",
            },
            {
              dataKey: "totalPriceOrder",
              name: "Income",
              fill: "#82ca9d",
              stackId: "group1",
            },
            {
              dataKey: "totalPriceInventory",
              name: "Purchase",
              fill: "#FA4032",
              stackId: "group2",
            },
          ]}
        />
      </section>
    </section>
  );
};

export default AdminDashboard;
